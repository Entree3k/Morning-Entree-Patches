package morningentree.morphe.patches.volumestyles.premium

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

internal object SetEntitlementStateFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "L"),
    strings = listOf("Unknown SKU "),
)
