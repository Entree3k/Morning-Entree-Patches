package morningentree.morphe.patches.all.detection.signature.pms

import app.morphe.patcher.patch.ResourcePatchContext
import org.w3c.dom.Document

internal const val MANIFEST_NODE = "manifest"
internal const val APPLICATION_NODE = "application"
internal const val ANDROID_NAME_ATTR = "android:name"

internal fun ResourcePatchContext.androidManifest(
    block: Document.() -> Unit,
): Document = document("AndroidManifest.xml").use { document ->
    document.apply(block)
}
