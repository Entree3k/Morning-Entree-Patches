package morningentree.morphe.patches.listy.premium

import app.morphe.patcher.Fingerprint

internal object IsSubscribedFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("unlimited"),
)
