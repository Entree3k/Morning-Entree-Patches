package morningentree.morphe.patches.all.detection.signature.pms

import app.morphe.patcher.patch.resourcePatch
import morningentree.morphe.util.get
import morningentree.morphe.util.set

private const val EXTENSION_CLASS_NAME =
    "morningentree.morphe.extension.all.detection.signature.pms.SignatureHookApp"

val replaceSubApplicationPatch = resourcePatch(
    description = "Sets the sub-application with our SignatureHookApp."
) {
    execute {
        androidManifest {
            val application = this[APPLICATION_NODE]
            val subApplicationName = application[ANDROID_NAME_ATTR]

            if (subApplicationName.isEmpty()) {
                application[ANDROID_NAME_ATTR] = EXTENSION_CLASS_NAME
            }
        }
    }
}
