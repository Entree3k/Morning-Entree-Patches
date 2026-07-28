package morningentree.morphe.patches.all.misc.installer

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import java.util.logging.Logger

private const val PLAY_STORE = "com.android.vending"

private const val PACKAGE_MANAGER = "Landroid/content/pm/PackageManager;"
private const val INSTALL_SOURCE_INFO = "Landroid/content/pm/InstallSourceInfo;"
private const val SESSION_INFO = "Landroid/content/pm/PackageInstaller\$SessionInfo;"

private fun Instruction.methodReferenceOrNull(): MethodReference? =
    (this as? ReferenceInstruction)?.reference as? MethodReference

private fun MethodReference.isInstallerGetter() =
    (definingClass == PACKAGE_MANAGER &&
        name == "getInstallerPackageName" &&
        parameterTypes.size == 1 &&
        parameterTypes[0].toString() == "Ljava/lang/String;" &&
        returnType == "Ljava/lang/String;") ||
        (definingClass == INSTALL_SOURCE_INFO &&
            name in setOf(
                "getInitiatingPackageName",
                "getInstallingPackageName",
                "getOriginatingPackageName",
                "getUpdateOwnerPackageName",
            ) &&
            parameterTypes.isEmpty() &&
            returnType == "Ljava/lang/String;") ||
        (definingClass == SESSION_INFO &&
            name in setOf(
                "getInstallerPackageName",
                "getInstallInitiatingPackageName",
                "getInstallOriginatingPackageName",
            ) &&
            parameterTypes.isEmpty() &&
            returnType == "Ljava/lang/String;")

private fun Instruction.isInstallSourceTarget() =
    opcode in setOf(Opcode.INVOKE_VIRTUAL, Opcode.INVOKE_VIRTUAL_RANGE) &&
        methodReferenceOrNull()?.isInstallerGetter() == true

private fun Method.hasInstallSourceTarget() =
    instructionsOrNull?.any { it.isInstallSourceTarget() } == true

/**
 * Universal "Spoof install source" patch — makes the app believe it was installed from a chosen
 * store.
 *
 * Based on Rushi's patch.
 */
@Suppress("unused")
val spoofInstallSourcePatch = bytecodePatch(
    name = "Spoof install source",
    description = "Makes the app think it was installed from a specific store (default: Google " +
        "Play). Useful when an app blocks features or errors because it detects it was not " +
        "installed from the Play Store. Only affects what the app sees, not the real system record.",
    default = false,
) {
    val installerPackageName by stringOption(
        key = "installerPackageName",
        default = PLAY_STORE,
        values = mapOf(
            "Google Play Store" to PLAY_STORE,
            "Samsung Galaxy Store" to "com.sec.android.app.samsungapps",
            "Huawei AppGallery" to "com.huawei.appmarket",
            "Amazon Appstore" to "com.amazon.venezia",
            "F-Droid" to "org.fdroid.fdroid",
        ),
        title = "Store to impersonate",
        description = "Most apps only check for the Google Play Store, so the default is usually " +
            "correct. Pick from the list, or type any package name directly.",
        required = true,
    ) { it == null || it.matches(Regex("^[a-z]\\w*(\\.[a-z]\\w*)+\$")) }

    execute {
        val logger = Logger.getLogger(this::class.java.name)
        val targetInstaller = installerPackageName ?: PLAY_STORE
        var patchedCount = 0

        classDefForEach { classDef ->
            if (classDef.methods.none { it.hasInstallSourceTarget() }) return@classDefForEach

            mutableClassDefBy(classDef).methods.forEach { method ->
                if (!method.hasInstallSourceTarget()) return@forEach

                val instructionList = method.instructionsOrNull?.toList() ?: return@forEach

                instructionList.forEachIndexed { index, instruction ->
                    if (!instruction.isInstallSourceTarget()) return@forEachIndexed

                    val moveResult = instructionList.getOrNull(index + 1) as? OneRegisterInstruction
                        ?: return@forEachIndexed
                    if (moveResult.opcode != Opcode.MOVE_RESULT_OBJECT) return@forEachIndexed

                    method.replaceInstruction(
                        index + 1,
                        "const-string v${moveResult.registerA}, \"$targetInstaller\"",
                    )
                    patchedCount++
                }
            }
        }

        logger.info("Spoof install source: $patchedCount call site(s) patched -> \"$targetInstaller\".")
    }
}
