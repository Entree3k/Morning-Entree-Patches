package morningentree.morphe.patches.boosted.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import morningentree.morphe.patches.boosted.shared.Constants
import morningentree.morphe.util.getReference

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Boosted Premium",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        PremiumStateInitFingerprint.method.apply {
            val insns = instructions.toList()
            val falseIndex = insns.indexOfFirst {
                val ref = it.getReference<FieldReference>()
                ref?.definingClass == "Ljava/lang/Boolean;" && ref.name == "FALSE"
            }
            if (falseIndex < 0) {
                throw PatchException("Could not find the Boolean.FALSE relay seed in the constructor.")
            }
            val register = (insns[falseIndex] as OneRegisterInstruction).registerA
            replaceInstruction(
                falseIndex,
                "sget-object v$register, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;",
            )
        }

        PremiumStateWriteFingerprint.method.apply {
            val insns = instructions.toList()
            val valueOfIndex = insns.indexOfFirst {
                val ref = it.getReference<MethodReference>()
                ref?.definingClass == "Ljava/lang/Boolean;" &&
                    ref.name == "valueOf" &&
                    ref.returnType == "Ljava/lang/Boolean;"
            }
            if (valueOfIndex < 0) {
                throw PatchException("Could not find the Boolean.valueOf boxing before the relay push.")
            }
            val argRegister = (insns[valueOfIndex] as FiveRegisterInstruction).registerC
            addInstruction(valueOfIndex, "const/4 v$argRegister, 0x1")
        }
    }
}
