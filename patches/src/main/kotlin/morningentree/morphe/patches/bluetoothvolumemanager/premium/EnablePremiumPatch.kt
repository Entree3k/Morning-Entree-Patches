package morningentree.morphe.patches.bluetoothvolumemanager.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import morningentree.morphe.patches.bluetoothvolumemanager.shared.Constants

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Unlock Pro",
    description = "Unlocks the Pro",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val iputIndex = InfoConstructorFingerprint.instructionMatches[0].index
        val valueReg = InfoConstructorFingerprint.method
            .getInstruction<TwoRegisterInstruction>(iputIndex).registerA

        InfoConstructorFingerprint.method.addInstructions(
            iputIndex,
            "const/4 v$valueReg, 0x1",
        )
    }
}
