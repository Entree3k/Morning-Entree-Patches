package morningentree.morphe.patches.homeworkout.premium

import app.morphe.patcher.Fingerprint

internal object IapSpFingerprint : Fingerprint(
    returnType = "Landroid/content/SharedPreferences;",
    parameters = emptyList(),
    strings = listOf("iap_sp"),
)
