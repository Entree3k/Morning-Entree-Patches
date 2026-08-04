package morningentree.morphe.patches.listy.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.listy.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Listy Premium",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsSubscribedFingerprint.method.returnEarly(true)
    }
}
