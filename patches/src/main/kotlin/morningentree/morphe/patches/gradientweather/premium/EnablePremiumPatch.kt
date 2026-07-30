package morningentree.morphe.patches.gradientweather.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import morningentree.morphe.patches.gradientweather.shared.Constants
import morningentree.morphe.util.getReference
import java.util.logging.Logger

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Gradient Weather Premium. Use With Spoof Install Source",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger(this::class.java.name)

        InitSubscriptionTierFingerprint.method.apply {
            val insns = instructions.toList()
            val stringIndex = insns.indexOfFirst {
                it.getReference<StringReference>()?.string == "is_lifetime"
            }
            if (stringIndex < 0) {
                throw PatchException("Could not find the is_lifetime pref read in the constructor.")
            }
            val moveResultIndex = ((stringIndex + 1) until insns.size).firstOrNull {
                insns[it].opcode == Opcode.MOVE_RESULT
            } ?: throw PatchException("Could not find the is_lifetime getBoolean result.")
            val register = (insns[moveResultIndex] as OneRegisterInstruction).registerA
            addInstruction(moveResultIndex + 1, "const/4 v$register, 0x1")
        }

        runCatching {
            SetSubscriptionTierFingerprint.method.apply {
                val lifetimeReference = instructions
                    .firstNotNullOfOrNull {
                        it.getReference<FieldReference>()?.takeIf { ref -> ref.name == "LIFETIME" }
                    }
                    ?: throw PatchException("Could not find the LIFETIME tier constant in the setter.")

                val smaliReference =
                    "${lifetimeReference.definingClass}->${lifetimeReference.name}:${lifetimeReference.type}"

                addInstructions(0, "sget-object p1, $smaliReference")
            }
        }.onSuccess {
            logger.info("Gradient Weather: tier setter pinned to LIFETIME.")
        }.onFailure {
            logger.warning("Gradient Weather: tier setter edit skipped: ${it.message}")
        }
    }
}
