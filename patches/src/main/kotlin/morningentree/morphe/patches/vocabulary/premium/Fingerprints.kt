package morningentree.morphe.patches.vocabulary.premium

import app.morphe.patcher.Fingerprint

internal object IsUserPremiumFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("premium_com.hrd.vocabulary"),
)

internal object SetUserPremiumFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Z"),
    custom = { method, classDef ->
        classDef.type == "Lcom/hrd/managers/S1;" && method.name == "Z1"
    },
)
