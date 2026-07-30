package morningentree.morphe.patches.pinnit.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import morningentree.morphe.patches.pinnit.shared.Constants
import morningentree.morphe.util.getReference

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Pinnit Pro. Just Tap Restore Purchase When Prompted. Must be installed with either ADB/InstallerX Revived/Inure so it is not in Restricted Mode",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val purchased = PurchaseStatusFingerprint.method.instructions
            .firstNotNullOfOrNull { insn ->
                if (insn.opcode == Opcode.SGET_OBJECT) insn.getReference<FieldReference>() else null
            }
            ?: throw PatchException("Could not find the Purchased status field in the mapper.")

        val purchasedRef = "${purchased.definingClass}->${purchased.name}:${purchased.type}"
        val forcePurchased = "sget-object v0, $purchasedRef\nreturn-object v0"

        PurchaseStatusGetterFingerprint.method.addInstructions(0, forcePurchased)
        RestorePurchaseFingerprint.method.addInstructions(0, forcePurchased)

        PurchaseStatusFingerprint.method.addInstructions(0, forcePurchased)
    }
}
