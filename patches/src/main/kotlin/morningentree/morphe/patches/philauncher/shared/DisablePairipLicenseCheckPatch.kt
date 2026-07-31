package morningentree.morphe.patches.philauncher.shared

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import java.util.logging.Logger

@Suppress("unused")
val disablePairipLicenseCheckPatch = bytecodePatch(
    description = "Neutralizes the Pairip client-side license check that blocks the re-signed APK at launch.",
) {
    execute {
        val logger = Logger.getLogger(this::class.java.name)
        var patched = false

        classDefForEach { classDef ->
            if (classDef.type != "Lcom/pairip/licensecheck/LicenseClient;") return@classDefForEach

            mutableClassDefBy(classDef).methods
                .filter { it.name == "checkLicense" && it.returnType == "V" }
                .forEach { method ->
                    // Short-circuit before the license service is ever contacted.
                    method.addInstruction(0, "return-void")
                    patched = true
                }
        }

        if (patched) {
            logger.info("Pairip: disabled LicenseClient.checkLicense (no-op).")
        } else {
            logger.warning("Pairip: LicenseClient.checkLicense not found; no changes applied.")
        }
    }
}
