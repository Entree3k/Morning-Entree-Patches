package morningentree.morphe.patches.materialcapsule.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import morningentree.morphe.patches.materialcapsule.shared.Constants
import morningentree.morphe.util.getReference

private fun MethodReference.isProFlagDecrypt() =
    returnType == "Ljava/lang/String;" &&
        parameterTypes.map { it.toString() } ==
        listOf("Ljava/lang/String;", "Ljavax/crypto/SecretKey;")

private fun Method.readsProFlag(): Boolean {
    val instructions = instructionsOrNull ?: return false

    return instructions.any { it.getReference<StringReference>()?.string == "yes" } &&
        instructions.any { it.getReference<MethodReference>()?.isProFlagDecrypt() == true }
}

private fun MethodReference.isFlowEmit() =
    returnType == "Ljava/lang/Object;" &&
        parameterTypes.size == 2 &&
        parameterTypes[0].toString() == "Ljava/lang/Object;"

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Material Capsule Pro",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        classDefForEach { classDef ->
            if (classDef.type != "Lcom/pairip/licensecheck/LicenseClient;") return@classDefForEach

            mutableClassDefBy(classDef).methods
                .filter { it.name == "checkLicense" && it.returnType == "V" }
                .forEach { it.addInstruction(0, "return-void") }
        }

        var patchedCount = 0

        classDefForEach { classDef ->
            if (classDef.methods.none { it.readsProFlag() }) return@classDefForEach

            mutableClassDefBy(classDef).methods.forEach { method ->
                if (!method.readsProFlag()) return@forEach
                val instructions = method.instructionsOrNull?.toList() ?: return@forEach

                val emits = instructions.withIndex().mapNotNull { (index, instruction) ->
                    if (instruction.opcode != Opcode.INVOKE_INTERFACE) return@mapNotNull null
                    if (instruction.getReference<MethodReference>()?.isFlowEmit() != true) {
                        return@mapNotNull null
                    }

                    (instruction as? FiveRegisterInstruction)?.let { index to it.registerD }
                }

                if (emits.isEmpty()) {
                    method.addInstructions(
                        0,
                        """
                            sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                            return-object v0
                        """,
                    )
                } else {
                    emits.reversed().forEach { (index, register) ->
                        method.addInstruction(
                            index,
                            "sget-object v$register, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;",
                        )
                    }
                }

                patchedCount++
            }
        }

        if (patchedCount == 0) throw PatchException(
            "No Pro flag read was found. Re-derive.",
        )
    }
}
