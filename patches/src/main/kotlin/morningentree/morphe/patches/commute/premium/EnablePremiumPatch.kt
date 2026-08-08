package morningentree.morphe.patches.commute.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.commute.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Does Not Commute Premium. Just Tap Get Premium and Restore Purchase When Prompted",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsProductIdRestoredFingerprint.method.returnEarly(true)

        GetStatusAsStringFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "2"
                return-object v0
            """,
        )
    }
}
