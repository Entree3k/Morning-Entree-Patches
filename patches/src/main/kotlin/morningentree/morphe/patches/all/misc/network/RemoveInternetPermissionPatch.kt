package morningentree.morphe.patches.all.misc.network

import app.morphe.patcher.patch.resourcePatch
import morningentree.morphe.util.asElementSequence
import morningentree.morphe.util.get

/**
 * Universal "Remove internet permission".
 * Ported from adobo (`dev.jkcarino.adobo...network.RemoveInternetPermissionPatch`).
 *
 * Strips `<uses-permission android:name="android.permission.INTERNET"/>` from the manifest. With no
 * INTERNET permission the OS blocks every socket the app opens, so bundled ad/analytics/telemetry SDKs
 */
@Suppress("unused")
val removeInternetPermissionPatch = resourcePatch(
    name = "Remove internet permission",
    description = "Removes the INTERNET permission so the app cannot access the network at all. " +
        "Blocks all trackers, analytics and ads from phoning home, but also disables any legitimate " +
        "online features. Only enable for apps you want fully offline.",
    default = false,
) {
    execute {
        document("AndroidManifest.xml").use { document ->
            val manifest = document["manifest"]

            manifest.getElementsByTagName("uses-permission")
                .asElementSequence()
                .filter { it.getAttribute("android:name") == "android.permission.INTERNET" }
                .toList()
                .forEach { it.parentNode.removeChild(it) }
        }
    }
}
