package morningentree.morphe.patches.google.gboard.incognito

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import morningentree.morphe.patches.google.gboard.detection.signature.bypassSignaturePatch
import morningentree.morphe.patches.google.gboard.shared.COMPATIBILITY_GBOARD
import morningentree.morphe.util.returnEarly
import java.util.logging.Logger

@Suppress("unused")
val alwaysIncognitoPatch = bytecodePatch(
    name = "Always incognito mode",
    description = "Always opens Gboard in incognito mode to disable typing-history collection and " +
        "personalization, while keeping clipboard and voice typing working in incognito.",
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    val logger = Logger.getLogger(this::class.java.name)

    execute {
        val incognitoMethod = IsIncognitoModeFingerprint.methodOrNull
            ?: IsIncognitoModeV2Fingerprint.methodOrNull
            ?: IsIncognitoModeInlinedFingerprint.methodOrNull
            ?: throw PatchException("Failed to force-enable incognito mode.")

        when (incognitoMethod) {
            IsIncognitoModeInlinedFingerprint.methodOrNull -> {
                val matches = IsIncognitoModeInlinedFingerprint.instructionMatches
                val requestIncognitoModeIndex = matches.last().index
                val isIncognitoModeIndex = requestIncognitoModeIndex - 1

                val register = incognitoMethod
                    .getInstruction<FiveRegisterInstruction>(requestIncognitoModeIndex)
                    .registerD

                incognitoMethod.replaceInstruction(
                    index = isIncognitoModeIndex,
                    smaliInstruction = "const/4 v$register, 0x1",
                )
            }
            else -> incognitoMethod.returnEarly(true)
        }

        runCatching {
            OnPrimaryClipChangedFingerprint.method.apply {
                val matches = OnPrimaryClipChangedFingerprint.instructionMatches
                val startIndex = matches.first().index
                val endIndex = matches.last().index
                removeInstructions(
                    index = startIndex,
                    count = (endIndex - startIndex) + 1,
                )
            }
        }.onSuccess {
            logger.info("[Found] Enabled clipboard in incognito.")
        }.onFailure {
            logger.warning("[Skipped] Could not enable clipboard in incognito: ${it.message}")
        }

        runCatching {
            EnableVoiceTypingFingerprint.method.apply {
                val index = EnableVoiceTypingFingerprint.instructionMatches.last().index
                val register = getInstruction<OneRegisterInstruction>(index).registerA
                addInstruction(index, "const/4 v$register, 0x0")
            }
        }.onSuccess {
            logger.info("[Found] Enabled voice typing in incognito.")
        }.onFailure {
            logger.warning("[Skipped] Could not enable voice typing in incognito: ${it.message}")
        }
    }
}
