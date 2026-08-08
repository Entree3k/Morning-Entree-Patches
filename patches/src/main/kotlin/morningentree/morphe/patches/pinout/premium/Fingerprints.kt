package morningentree.morphe.patches.pinout.premium

import app.morphe.patcher.Fingerprint

private const val ANDROID_STORE = "Lcom/mediocre/pinout/AndroidStore;"

internal object IsProductIdRestoredFingerprint : Fingerprint(
    name = "isProductIdRestored",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
    custom = { _, classDef -> classDef.type == ANDROID_STORE },
)

internal object GetStatusAsStringFingerprint : Fingerprint(
    name = "getStatusAsString",
    returnType = "Ljava/lang/String;",
    parameters = emptyList(),
    custom = { _, classDef -> classDef.type == ANDROID_STORE },
)
