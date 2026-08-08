package morningentree.morphe.patches.smartaudiobook.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import morningentree.morphe.patches.smartaudiobook.shared.Constants
import morningentree.morphe.util.getReference

private const val LICENSE_TYPE = "Lak/alizandro/smartaudiobookplayer/Billings\$LicenseType;"

private fun Method.isLicenseGetter() = returnType == LICENSE_TYPE && parameterTypes.isEmpty()

@Suppress("unused")
val enableFullVersionPatch = bytecodePatch(
    name = "Enable Full Version",
    description = "Unlocks the full version of Smart AudioBook Player",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        var fullField: String? = null
        classDefForEach { classDef ->
            if (classDef.type != LICENSE_TYPE) return@classDefForEach

            var lastString: String? = null
            val clinit = classDef.methods.firstOrNull { it.name == "<clinit>" }
                ?: return@classDefForEach

            clinit.implementation?.instructions?.forEach { instruction ->
                instruction.getReference<StringReference>()?.let { lastString = it.string }

                if (instruction.opcode != Opcode.SPUT_OBJECT) return@forEach
                val field = instruction.getReference<FieldReference>() ?: return@forEach
                if (field.type == LICENSE_TYPE && lastString == "Full") fullField = field.name
            }
        }

        val fullFieldName = fullField ?: throw PatchException(
            "Could not resolve the Full constant of $LICENSE_TYPE. Re-derive.",
        )

        var patchedCount = 0
        classDefForEach { classDef ->
            if (classDef.type == LICENSE_TYPE) return@classDefForEach
            if (classDef.methods.none { it.isLicenseGetter() }) return@classDefForEach

            mutableClassDefBy(classDef).methods
                .filter { it.isLicenseGetter() }
                .forEach { method ->
                    method.addInstructions(
                        0,
                        """
                            sget-object v0, $LICENSE_TYPE->$fullFieldName:$LICENSE_TYPE
                            return-object v0
                        """,
                    )
                    patchedCount++
                }
        }

        if (patchedCount == 0) throw PatchException(
            "No no-argument getter returning $LICENSE_TYPE was found. Re-derive.",
        )
    }
}
