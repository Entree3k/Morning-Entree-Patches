package morningentree.morphe.patches.smashhit.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.smashhit.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Smash Hit. Just Tap Get Premium and Restore Purchase When Prompted",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        OwnsPremiumProductFingerprint.method.returnEarly(true)

        IsProductOwnedFingerprint.method.addInstructions(
            1,
            """
                const/4 p1, 0x1
                monitor-exit p0
                return p1
            """,
        )

        listOf(
            HasRefreshedOwnedProductsFingerprint,
            IsPremiumProductRestoredFingerprint,
        ).forEach { fingerprint ->
            fingerprint.method.addInstructions(
                0,
                """
                    const-string p1, "true"
                    return-object p1
                """,
            )
        }
    }
}
