package morningentree.morphe.patches.nzb360.premium

import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import morningentree.morphe.patches.nzb360.shared.Constants
import morningentree.morphe.util.getReference
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val unlockAllAccessPatch = bytecodePatch(
    name = "Unlock All Access",
    description = "Unlocks nzb360",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        listOf(
            IsAASubscriptionActiveFingerprint,
            IsUnlockedFingerprint,
            IsSubscribedFingerprint,
        ).forEach { it.methodOrNull?.returnEarly(true) }

        listOf(
            IsLockedTwoArgFingerprint,
            IsLockedOneArgFingerprint,
        ).forEach { it.methodOrNull?.returnEarly(false) }

        SubscriptionSectionDefaultPlanFingerprint.methodOrNull?.apply {
            val insns = instructions.toList()
            val idx = insns.indexOfFirst {
                it.opcode == Opcode.CONST_STRING &&
                    it.getReference<StringReference>()?.string == "Monthly"
            }
            if (idx >= 0) {
                val reg = (insns[idx] as OneRegisterInstruction).registerA
                replaceInstruction(idx, "const-string v$reg, \"Yearly\"")
            }
        }
    }
}
