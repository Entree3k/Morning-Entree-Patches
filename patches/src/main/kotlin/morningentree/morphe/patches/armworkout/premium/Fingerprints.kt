package morningentree.morphe.patches.armworkout.premium

import app.morphe.patcher.Fingerprint

internal object PremiumStateGetterFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    custom = { method, classDef ->
        classDef.type == "Lod/c;" && method.name == "H"
    },
)
