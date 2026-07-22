package morningentree.morphe.patches.ling.premium

import app.morphe.patcher.patch.rawResourcePatch
import morningentree.morphe.patches.ling.shared.Constants
import morningentree.morphe.patches.shared.misc.hermes.hermesPatch

const val RETURN_TRUE = "78 00 5C 00"

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium",
    description = "Unlocks Ling Pro"
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(hermesPatch {
        // Selector `state => state.payments.isProUser`. The two 2-byte LE values
        // embedded in the pattern are Hermes string table IDs for "payments" and
        // "isProUser" respectively; they shift on every rebuild of the JS bundle
        // even when the selector's source/logic is unchanged, so this pattern must
        // be re-derived per app version. See ling/notes/hermes-string-id-drift.md.
        val selectIsProUser =
            "6C 00 01 37 00 00 01 50 7B 37 00 00 02 29 7C 5C 00" to RETURN_TRUE

        setOf(selectIsProUser)
    })
}
