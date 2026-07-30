package morningentree.morphe.patches.nova.prime

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import morningentree.morphe.patches.nova.shared.Constants
import morningentree.morphe.util.getReference

@Suppress("unused")
val enablePrimePatch = bytecodePatch(
    name = "Enable Prime",
    description = "Unlocks Nova Launcher Prime",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        SetPrimeFromPreferencesFingerprint.method.apply {
            val insns = instructions.toList()

            val getIntIndex = insns.indexOfFirst {
                it.opcode == Opcode.INVOKE_INTERFACE &&
                    it.getReference<MethodReference>()?.name == "getInt"
            }
            if (getIntIndex < 0) {
                throw PatchException("Could not find the license-level getInt read in Nova's license method.")
            }

            val moveResultIndex = (getIntIndex + 1 until insns.size).first {
                insns[it].opcode == Opcode.MOVE_RESULT
            }
            val register = (insns[moveResultIndex] as OneRegisterInstruction).registerA

            addInstruction(moveResultIndex + 1, "const/16 v$register, 0x200")
        }

        LicenseCheckEntryFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Lny/a3;->a:Lny/h2;
                const/4 v1, 0x1
                iput-boolean v1, v0, Lny/h2;->h:Z
                iput-boolean v1, v0, Lny/h2;->c:Z
                sput-boolean v1, Lvu/y0;->b:Z
                return-void
            """,
        )
    }
}
