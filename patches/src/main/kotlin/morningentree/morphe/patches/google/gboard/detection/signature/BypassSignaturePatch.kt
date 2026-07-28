package morningentree.morphe.patches.google.gboard.detection.signature

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.google.gboard.fixes.applyWorkaroundPatch
import morningentree.morphe.patches.google.gboard.shared.COMPATIBILITY_GBOARD
import morningentree.morphe.util.returnEarly

val bypassSignaturePatch = bytecodePatch(
    description = "Bypasses the signature verification checks.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(applyWorkaroundPatch)

    execute {
        CheckSignatureFingerprint.method.returnEarly(true)
    }
}
