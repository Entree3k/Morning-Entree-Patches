package morningentree.morphe.patches.photon.premium

import app.morphe.patcher.Fingerprint

internal object QueryPurchasesResultFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("Purchases queried: ", "acknowledged"),
)
