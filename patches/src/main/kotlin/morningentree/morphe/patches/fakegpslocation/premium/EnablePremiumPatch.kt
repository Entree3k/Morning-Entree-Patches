package morningentree.morphe.patches.fakegpslocation.premium

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import morningentree.morphe.patches.fakegpslocation.shared.Constants
import morningentree.morphe.util.getReference
import java.util.logging.Logger

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Fake GPS Location Premium",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger(this::class.java.name)
        val activityType = "Lcom/adevinta/leku/LocationPickerActivity;"
        var patchedReads = 0

        classDefForEach { classDef ->
            if (classDef.type != activityType) return@classDefForEach

            val mutableClass = mutableClassDefBy(classDef)

            var purchasedField: FieldReference? = null
            for (method in mutableClass.methods) {
                val insns = method.instructionsOrNull?.toList() ?: continue
                val keyIndex = insns.indexOfFirst {
                    it.getReference<StringReference>()?.string == "purchased"
                }
                if (keyIndex < 0) continue
                val iputIndex = (keyIndex + 1 until insns.size).firstOrNull {
                    insns[it].opcode == Opcode.IPUT_BOOLEAN
                } ?: continue
                purchasedField = insns[iputIndex].getReference<FieldReference>()
                if (purchasedField != null) break
            }

            val field = purchasedField
                ?: throw PatchException(
                    "Could not locate the obfuscated 'purchased' boolean field in LocationPickerActivity.",
                )

            for (method in mutableClass.methods) {
                val insns = method.instructionsOrNull?.toList() ?: continue
                insns.withIndex().reversed().forEach { (index, insn) ->
                    if (insn.opcode != Opcode.IGET_BOOLEAN) return@forEach
                    val ref = insn.getReference<FieldReference>() ?: return@forEach
                    if (ref.name != field.name || ref.definingClass != field.definingClass) {
                        return@forEach
                    }
                    val register = (insn as OneRegisterInstruction).registerA
                    method.replaceInstruction(index, "const/16 v$register, 0x1")
                    patchedReads++
                }
            }
        }

        if (patchedReads == 0) {
            throw PatchException("Fake GPS Location: no 'purchased' flag reads were found to patch.")
        }
        logger.info("Fake GPS Location: forced $patchedReads purchased-flag read(s) to true.")
    }
}
