package morningentree.morphe.patches.macrodroid.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.macrodroid.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks MacroDroid Pro, Use With Spoof App Signature",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        ProStatusFingerprint.method.returnEarly(true)

        SignatureCheckFingerprint.method.returnEarly(false)

        TemplateStoreSignatureCheckFingerprint.method.returnEarly(false)
    }
}
