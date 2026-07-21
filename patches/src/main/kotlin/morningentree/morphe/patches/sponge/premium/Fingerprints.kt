package morningentree.morphe.patches.sponge.premium

import app.morphe.patcher.Fingerprint

internal object IsPremiumUserFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("is_premium_user"),
)
