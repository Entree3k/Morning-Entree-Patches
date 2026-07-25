package morningentree.morphe.patches.lifesum.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlock Lifesum Premium",
) {
    compatibleWith(
        Compatibility(
            name = "Lifesum",
            packageName = "com.sillens.shapeupclub",
            appIconColor = 0x41CD8C,
            targets = listOf(AppTarget("20.8.0")),
        ),
    )

    execute {
        ApiUserProfileGetPremiumFingerprint.method.addInstructions(
            0,
            """
                sget-object p0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                return-object p0
            """.trimIndent(),
        )

        // Semantic gate used directly by feature checks; force it true
        HasPremiumFingerprint.method.returnEarly(true)
    }
}
