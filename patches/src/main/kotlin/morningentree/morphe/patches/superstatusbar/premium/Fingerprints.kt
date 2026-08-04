package morningentree.morphe.patches.superstatusbar.premium

import app.morphe.patcher.Fingerprint

internal object EntitlementStateEnumFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf(
        "UNKNOWN",
        "UNPURCHASED",
        "PENDING",
        "PURCHASED",
        "PURCHASED_AND_ACKNOWLEDGED",
    ),
)
