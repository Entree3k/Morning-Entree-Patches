package morningentree.morphe.patches.philauncher.premium

import app.morphe.patcher.Fingerprint

internal object IsProUserFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    custom = { method, classDef ->
        classDef.type == "Lcom/launcher/hype/data/local/prefs/Prefs;" &&
            method.name == "getProUser"
    },
)
