package morningentree.morphe.patches.all.detection.signature.pms

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption

@Suppress("unused")
val spoofSignatureVerificationPatch = bytecodePatch(
    name = "Spoof signature verification",
    description = "Spoofs the signature verification",
    default = false
) {
    extendWith("extensions/all/detection/signature/pms.mpe")

    dependsOn(
        packageNamePatch,
        encodeCertificatePatch,
        replaceSubApplicationPatch
    )

    val packageNameOption =
        stringOption(
            key = "packageName",
            default = "Default",
            values = mapOf("Default" to "Default"),
            title = "Package name",
            description = "The package name of the app, if modified. " +
                "This must match the package name defined when the " +
                "'Change package name' patch is selected or as defined in the AndroidManifest.xml.",
            required = true,
        ) { packageName ->
            val packageNamePattern = """^[a-z]\w*(\.[a-z]\w*)+$""".toRegex()
            packageName == "Default" || packageName!!.matches(packageNamePattern)
        }

    val signatureOption =
        stringOption(
            key = "signature",
            default = "Default",
            title = "Base64-encoded signature",
            description = "The base64-encoded signature from the original, unmodified APK. " +
                "Leave as \"Default\" to auto-extract the certificate from the APK. " +
                "Otherwise, clear it and paste the full Base64 signature to override auto-extraction.",
            required = true,
        ) { signature ->
            signature == "Default" || !signature.isNullOrEmpty()
        }

    execute {
        StaticConstructorFingerprint.method.apply {
            val packageNameIndex = StaticConstructorFingerprint.instructionMatches.first().index
            val customPackageName = packageNameOption.value!!
            val packageName =
                if (customPackageName == packageNameOption.default) {
                    appPackageName
                } else {
                    customPackageName
                }

            replaceInstruction(
                index = packageNameIndex,
                smaliInstruction = """
                    const-string v0, "$packageName"
                """
            )

            val signatureIndex = StaticConstructorFingerprint.instructionMatches.last().index
            val customSignature = signatureOption.value!!
            val signature =
                if (customSignature == signatureOption.default) {
                    signature ?: throw PatchException(
                        errorMessage = "Could not auto-extract the app's signing certificate. " +
                            "Keep the original, unmodified app installed on this device, or set " +
                            "'Path to original APK' in the 'Provide original app certificate' " +
                            "patch, or paste the Base64-encoded signature in this patch's " +
                            "'Base64-encoded signature' option."
                    )
                } else {
                    customSignature.trim()
                }

            replaceInstruction(
                index = signatureIndex,
                smaliInstruction = """
                    const-string v1, "$signature"
                """
            )
        }

        val signatureHookAppClass = StaticConstructorFingerprint.originalClassDef

        classDefForEach { classDef ->
            if (
                classDef != signatureHookAppClass &&
                classDef.superclass == "Landroid/app/Application;"
            ) {
                mutableClassDefBy(classDef)
                    .setSuperClass(signatureHookAppClass.type)
            }
        }
    }
}
