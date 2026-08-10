package morningentree.morphe.patches.appsales.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables Appsales Premium.",
) {
    compatibleWith(
        Compatibility(
            name = "AppSales",
            packageName = "net.tsapps.appsales",
            appIconColor = 0x428BCA,
            targets = listOf(AppTarget("12.3")),
        ),
    )

    execute {
        GetBooleanPrefFingerprint.method.addInstructions(
            0,
            """
            const-string v0, "pu"
            invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
            move-result v0
            if-eqz v0, :cond_skip
            const/4 v0, 0x1
            return v0
            :cond_skip
            """,
        )

        GetStringPrefFingerprint.method.addInstructions(
            0,
            """
            const-string v0, "premium_sku"
            invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
            move-result v0
            if-eqz v0, :cond_skip_str
            const-string v0, "as_premium_yearly"
            return-object v0
            :cond_skip_str
            """,
        )

        ClearPremiumUserFingerprint.method.addInstructions(0, "return-void")

        ShowPremiumDialogFingerprint.method.addInstructions(0, "return-void")
    }
}
