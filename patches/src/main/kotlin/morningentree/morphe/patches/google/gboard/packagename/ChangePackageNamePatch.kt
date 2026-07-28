package morningentree.morphe.patches.google.gboard.packagename

import app.morphe.patcher.patch.ResourcePatchContext
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import morningentree.morphe.patches.google.gboard.shared.COMPATIBILITY_GBOARD
import morningentree.morphe.util.asElementSequence
import morningentree.morphe.util.get
import morningentree.morphe.util.set
import org.w3c.dom.Document
import org.w3c.dom.NodeList

private const val MANIFEST_NODE = "manifest"
private const val ANDROID_NAME_ATTR = "android:name"

private fun ResourcePatchContext.androidManifest(
    block: Document.() -> Unit,
): Document = document("AndroidManifest.xml").use { document ->
    document.apply(block)
}

@Suppress("unused")
val changePackageNamePatch = resourcePatch(
    name = "Change package name",
    description = "Installs Gboard as a clone by appending \".clone\" to the package name " +
        "(configurable), so it installs next to the stock Gboard. Changing an app's package name " +
        "can lead to unexpected issues.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    val packageNameOption =
        stringOption(
            key = "packageName",
            default = "Default",
            values = mapOf("Default" to "Default"),
            title = "Package name",
            description = "The package name to rename the app to. \"Default\" appends \".clone\".",
            required = true,
        ) { packageName ->
            val packageNamePattern = """^[a-z]\w*(\.[a-z]\w*)+$""".toRegex()
            packageName == "Default" || packageName!!.matches(packageNamePattern)
        }

    val updatePermissions by booleanOption(
        key = "updatePermissions",
        default = true,
        title = "Update permissions",
        description = "Update compatibility receiver permissions. Enabling this can fix " +
            "installation errors, but can also break features in certain apps.",
    )

    val updateOtherPermissions by booleanOption(
        key = "updateOtherPermissions",
        default = true,
        title = "Update other permissions",
        description = "Update other permissions declared by the app. Enabling this can fix " +
            "installation errors, but can also break features in certain apps.",
    )

    val updateProviders by booleanOption(
        key = "updateProviders",
        default = true,
        title = "Update content providers",
        description = "Update content provider URI authorities. Enabling this can fix " +
            "installation errors, but can also break features in certain apps.",
    )

    finalize {
        androidManifest {
            val manifest = this[MANIFEST_NODE]
            val packageName = manifest["package"]
            val replacementPackageName = packageNameOption.value!!
            val newPackageName =
                if (replacementPackageName == packageNameOption.default) {
                    "$packageName.clone"
                } else {
                    replacementPackageName
                }

            manifest["package"] = newPackageName

            if (updatePermissions == true || updateOtherPermissions == true) {
                val receiverNotExported = "DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"

                setOf("permission", "uses-permission")
                    .asSequence()
                    .map(::getElementsByTagName)
                    .flatMap(NodeList::asElementSequence)
                    .filter { element ->
                        val nameAttrValue = element[ANDROID_NAME_ATTR]
                        val isReceiverPermission = updatePermissions == true &&
                            nameAttrValue == "$packageName.$receiverNotExported"
                        val isCustomPermission = updateOtherPermissions == true &&
                            nameAttrValue.startsWith(packageName)

                        isReceiverPermission || isCustomPermission
                    }
                    .forEach { element ->
                        element[ANDROID_NAME_ATTR] = element[ANDROID_NAME_ATTR].replace(
                            oldValue = packageName,
                            newValue = newPackageName,
                        )
                    }
            }

            if (updateProviders == true) {
                val authoritiesAttr = "android:authorities"

                manifest
                    .getElementsByTagName("provider")
                    .asElementSequence()
                    .filter { it[authoritiesAttr].startsWith(packageName) }
                    .forEach { provider ->
                        provider[authoritiesAttr] = provider[authoritiesAttr].replace(
                            oldValue = packageName,
                            newValue = newPackageName,
                        )
                    }
            }
        }
    }
}
