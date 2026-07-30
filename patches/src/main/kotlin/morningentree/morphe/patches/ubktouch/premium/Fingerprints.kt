package morningentree.morphe.patches.ubktouch.premium

import app.morphe.patcher.Fingerprint

internal object MainPrefUnlockedFingerprint : Fingerprint(
    returnType = "Ljava/lang/Boolean;",
    parameters = emptyList(),
    custom = { method, classDef ->
        classDef.type == "Leu/toneiv/ubktouch/model/preferences/MainPref;" && method.name == "unlocked"
    },
)
