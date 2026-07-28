package morningentree.morphe.patches.google.gboard.fixes

import app.morphe.patcher.patch.resourcePatch
import morningentree.morphe.patches.google.gboard.shared.COMPATIBILITY_GBOARD
import morningentree.morphe.util.get

/**
 * Removes an Android-15 input-method attribute that current AAPT cannot link. Applies to Gboard 14.1.x and later. Ported from the adobo patches.
 */
val applyWorkaroundPatch = resourcePatch(
    description = "Applies a workaround for the patcher to fix missing or unsupported resources.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    execute {
        document("res/xml/method.xml").use { document ->
            val inputMethod = document["input-method"]

            // error: attribute android:supportsConnectionlessStylusHandwriting not found.
            // https://developer.android.com/sdk/api_diff/v-dp2-incr/changes/android.R.attr
            inputMethod.removeAttribute("android:supportsConnectionlessStylusHandwriting")
        }
    }
}
