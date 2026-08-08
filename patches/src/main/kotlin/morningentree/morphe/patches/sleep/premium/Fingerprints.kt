package morningentree.morphe.patches.sleep.premium

import app.morphe.patcher.Fingerprint

private const val TRIAL_FILTER = "Lcom/urbandroid/sleep/TrialFilter;"
private const val PREFERENCES_UTILS = "Lcom/urbandroid/common/util/PreferencesUtils;"

internal object HasUnlockFingerprint : Fingerprint(
    name = "hasUnlock",
    returnType = "Z",
    parameters = emptyList(),
    custom = { _, classDef -> classDef.type == TRIAL_FILTER },
)

internal object IsUnlockAckedFingerprint : Fingerprint(
    name = "isUnlockAcked",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    custom = { _, classDef -> classDef.type == PREFERENCES_UTILS },
)

internal object IsTrialFingerprint : Fingerprint(
    name = "isTrial",
    returnType = "Z",
    parameters = emptyList(),
    custom = { _, classDef -> classDef.type == TRIAL_FILTER },
)
