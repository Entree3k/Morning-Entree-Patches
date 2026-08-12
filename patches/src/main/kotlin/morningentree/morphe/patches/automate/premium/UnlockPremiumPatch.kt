package morningentree.morphe.patches.automate.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.automate.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks Automate Premium",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        AutomatePremiumGateFingerprint.method.returnEarly(true)

        AutomatePremiumQueryFingerprint.methodOrNull?.addInstructions(
            0,
            """
            const/4 v0, 0x3
            iput v0, p0, Lcom/llamalab/automate/AutomateService;->Y1:I
            """.trimIndent(),
        )
    }
}
