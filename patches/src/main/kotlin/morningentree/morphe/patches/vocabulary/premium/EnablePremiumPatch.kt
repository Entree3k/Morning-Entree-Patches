package morningentree.morphe.patches.vocabulary.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.vocabulary.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Vocabulary Premium",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsUserPremiumFingerprint.method.returnEarly(true)
        SetUserPremiumFingerprint.method.addInstructions(0, "return-void")
    }
}
