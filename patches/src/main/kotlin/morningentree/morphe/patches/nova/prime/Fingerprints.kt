package morningentree.morphe.patches.nova.prime

import app.morphe.patcher.Fingerprint

object SetPrimeFromPreferencesFingerprint : Fingerprint(
    strings = listOf("android.os.SystemProperties", "widget_reset_ids"),
)

object LicenseCheckEntryFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
    custom = { method, classDef ->
        classDef.type == "Lvu/y0;" && method.name == "c"
    },
)
