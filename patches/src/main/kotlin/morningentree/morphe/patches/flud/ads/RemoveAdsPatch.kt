package morningentree.morphe.patches.flud.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.flud.shared.Constants

private fun dismissActivity() =
    "invoke-super {p0, p1}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V\n" +
        "invoke-virtual {p0}, Landroid/app/Activity;->finish()V\n" +
        "return-void"

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
    name = "Remove Ads",
    description = "Ad Free Flud",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        AdLoaderFingerprint.methodOrNull?.addInstructions(0, "return-void")

        InterstitialTriggerFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return-object v0
            """,
        )

        LicenseClientFingerprint.methodOrNull?.addInstructions(0, "return-void")

        LicenseActivityOnCreateFingerprint.methodOrNull?.addInstructions(0, dismissActivity())
    }
}
