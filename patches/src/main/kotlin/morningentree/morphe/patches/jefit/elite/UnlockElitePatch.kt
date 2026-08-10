package morningentree.morphe.patches.jefit.elite

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.jefit.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val unlockElitePatch = bytecodePatch(
    name = "Unlock Elite",
    description = "Unlocks JEFIT Elite",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val retElite = "const/4 v0, 0x2\nreturn v0"
        IsEliteFingerprint.method.returnEarly(true)
        IsProFingerprint.method.returnEarly(true)
        GetAccountTypeFingerprint.method.addInstructions(0, retElite)
        FunctionAccountTypeFingerprint.method.addInstructions(0, retElite)
        IsEliteUserFingerprint.method.returnEarly(true)
    }
}
