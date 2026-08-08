package morningentree.morphe.patches.all.detection.signature.pms

import app.morphe.patcher.patch.resourcePatch
import morningentree.morphe.util.get

internal lateinit var appPackageName: String
    private set

val packageNamePatch = resourcePatch(
    description = "Extracts the package name of the app."
) {
    execute {
        androidManifest {
            val manifest = this[MANIFEST_NODE]
            appPackageName = manifest["package"]
        }
    }
}
