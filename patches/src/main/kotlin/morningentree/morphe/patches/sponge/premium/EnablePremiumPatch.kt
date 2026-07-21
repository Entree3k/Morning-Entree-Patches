package morningentree.morphe.patches.sponge.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlock Sponge Premium"
) {
    compatibleWith(
        Compatibility(
            name = "Sponge",
            packageName = "com.prismtree.sponge",
            // Cosmetic only (patcher UI accent).
            appIconColor = 0x2AB7A9,
            targets = listOf(AppTarget("2.8.0")),
        ),
    )

    execute {
        IsPremiumUserFingerprint.method.returnEarly(true)
    }
}
