package morningentree.morphe.patches.boosted.premium

import app.morphe.patcher.Fingerprint

internal object PremiumStateInitFingerprint : Fingerprint(
    strings = listOf("BILLING_REFRESH_WORKER"),
)

internal object PremiumStateWriteFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;"),
    strings = listOf(
        "inapp.boosted.premium",
        "subs.boosted.premium.yearly",
        "subs.boosted.premium.monthly",
    ),
)
