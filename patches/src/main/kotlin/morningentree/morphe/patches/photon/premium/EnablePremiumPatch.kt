package morningentree.morphe.patches.photon.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.photon.shared.Constants

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Photon Camera Pro",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        classDefForEach { classDef ->
            if (classDef.type != "Lcom/pairip/licensecheck/LicenseClient;") return@classDefForEach

            mutableClassDefBy(classDef).methods
                .filter { it.name == "checkLicense" && it.returnType == "V" }
                .forEach { it.addInstruction(0, "return-void") }
        }

        QueryPurchasesResultFingerprint.method.addInstructions(
            0,
            """
                iget-object v0, p0, Lrd;->a:Lj8;
                iget-object v0, v0, Lj8;->l:Ljava/lang/Object;
                check-cast v0, LUO0;
                const/4 v1, 0x0
                sget-object v2, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                invoke-virtual {v0, v1, v2}, LUO0;->j(Ljava/lang/Object;Ljava/lang/Object;)Z
                return-void
            """,
        )
    }
}
