package morningentree.morphe.patches.panels.misc

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

object MainActivitySignatureCheckFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/fossor/panels/MainActivity;" && method.name == "onCreate"
    },
    filters = listOf(
        methodCall(definingClass = "Landroid/content/pm/Signature;", name = "toCharsString"),
        methodCall(definingClass = "Ljava/lang/String;", name = "equals"),
    ),
)
