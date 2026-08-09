package morningentree.morphe.patches.paisa.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.paisa.shared.Constants
import morningentree.morphe.util.injectActiveRevenueCatEntitlements
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Paisa Premium. Use With Spoof Install Source",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        EntitlementInfosMapperFingerprint.method.injectActiveRevenueCatEntitlements()

        EntitlementInfoIsActiveFingerprint.method.returnEarly(true)
    }
}
