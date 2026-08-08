package morningentree.morphe.patches.obdmary.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.obdmary.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Obd Mary's Paid Features, Including the Diagnostics Edition",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsFullAppSubscriptionPurchasedFingerprint.method.returnEarly(true)
        IsDiagnosticsEditionOwnedFingerprint.method.returnEarly(true)
        IsFreeAppFingerprint.method.returnEarly(false)
    }
}
