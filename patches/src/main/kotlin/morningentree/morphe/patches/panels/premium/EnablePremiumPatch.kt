package morningentree.morphe.patches.panels.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.panels.misc.disableSignatureVerificationPatch
import morningentree.morphe.patches.panels.shared.Constants

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Premium. Use With Spoof Install Source."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(disableSignatureVerificationPatch)

    execute {
        FullVersionCheckFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """
        )
    }
}
