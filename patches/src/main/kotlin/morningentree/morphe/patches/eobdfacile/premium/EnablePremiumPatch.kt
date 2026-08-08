package morningentree.morphe.patches.eobdfacile.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.eobdfacile.shared.Constants

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks EOBD Facile Premium",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        LicenseLevelFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x3
                return v0
            """,
        )
    }
}
