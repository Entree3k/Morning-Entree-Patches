package morningentree.morphe.patches.nova.privacy

import app.morphe.patcher.patch.resourcePatch
import morningentree.morphe.patches.nova.shared.Constants
import morningentree.morphe.util.asElementSequence
import morningentree.morphe.util.get
import org.w3c.dom.Element
import java.util.logging.Logger

private val TRACKER_PERMISSIONS = setOf(
    "com.google.android.gms.permission.AD_ID",
    "android.permission.ACCESS_ADSERVICES_AD_ID",
    "android.permission.ACCESS_ADSERVICES_ATTRIBUTION",
    "android.permission.ACCESS_ADSERVICES_TOPICS",
)

private val COLLECTION_FLAGS_OFF = mapOf(
    "firebase_analytics_collection_enabled" to "false",
    "firebase_analytics_collection_deactivated" to "true",
    "firebase_crashlytics_collection_enabled" to "false",
    "google_analytics_adid_collection_enabled" to "false",
    "google_analytics_default_allow_ad_personalization_signals" to "false",
)

@Suppress("unused")
val removeTrackersPatch = resourcePatch(
    name = "Disable Analytics & AD Tracking",
    description = "Turns off analytics, collection and removes the advertising-ID / ad-services permissions. Pair with \"Block Internet Patch\" to fully block trackers.",
    default = false,
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger(this::class.java.name)

        document("AndroidManifest.xml").use { document ->
            val manifest = document["manifest"]
            val application = document["application"]

            COLLECTION_FLAGS_OFF.forEach { (name, value) ->
                val existing = application.getElementsByTagName("meta-data")
                    .asElementSequence()
                    .firstOrNull { it.getAttribute("android:name") == name }

                if (existing != null) {
                    existing.removeAttribute("android:resource")
                    existing.setAttribute("android:value", value)
                } else {
                    val meta = document.createElement("meta-data") as Element
                    meta.setAttribute("android:name", name)
                    meta.setAttribute("android:value", value)
                    application.appendChild(meta)
                }
            }

            var removedPermissions = 0
            manifest.getElementsByTagName("uses-permission")
                .asElementSequence()
                .filter { it.getAttribute("android:name") in TRACKER_PERMISSIONS }
                .toList()
                .forEach {
                    it.parentNode.removeChild(it)
                    removedPermissions++
                }

            logger.info(
                "Nova disable analytics: collection flags forced off, " +
                    "$removedPermissions ad permission(s) removed.",
            )
        }
    }
}
