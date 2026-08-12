package morningentree.morphe.patches.thefor.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.thefor.shared.Constants
import morningentree.morphe.util.injectActiveRevenueCatEntitlements
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks TheFor Pro",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        EntitlementInfosMapperFingerprint.method.injectActiveRevenueCatEntitlements()

        EntitlementInfoIsActiveFingerprint.method.returnEarly(true)
    }
}
