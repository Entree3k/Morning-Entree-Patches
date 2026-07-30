package morningentree.morphe.patches.panels.misc

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import morningentree.morphe.patches.panels.shared.Constants

@Suppress("unused")
val disableSignatureVerificationPatch = bytecodePatch(
    name = "Disable Signature Verification",
    description = "Disable Signature Verification So The App Doesn't Crash When The APK Signature Changes After Patching",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        MainActivitySignatureCheckFingerprint.apply {
            val equalsIndex = instructionMatches[1].index
            val resultReg = method
                .getInstruction<OneRegisterInstruction>(equalsIndex + 1).registerA
            method.addInstructions(equalsIndex + 2, "const/4 v$resultReg, 0x1")
        }
    }
}
