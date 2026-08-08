package morningentree.morphe.patches.armworkout.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.armworkout.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Arm Workout Premium. Use with Spoof App Signature",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        PremiumStateGetterFingerprint.method.returnEarly(true)
    }
}
