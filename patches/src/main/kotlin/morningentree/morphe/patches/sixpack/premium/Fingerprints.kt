package morningentree.morphe.patches.sixpack.premium

import app.morphe.patcher.Fingerprint

internal object RemoveAdsGateFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    strings = listOf("sixpack.sixpackabs.absworkout.removeads"),
)
