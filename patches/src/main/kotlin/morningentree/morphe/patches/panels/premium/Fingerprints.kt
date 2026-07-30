package morningentree.morphe.patches.panels.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string

object FullVersionCheckFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        string("fullVersion"),
        methodCall(name = "getBoolean"),
    )
)
