package morningentree.morphe.patches.superstatusbar.premium

import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import morningentree.morphe.patches.superstatusbar.shared.Constants
import morningentree.morphe.util.getReference
import morningentree.morphe.util.returnEarly
import java.util.logging.Logger

private const val OWNED_STATE_NAME = "PURCHASED_AND_ACKNOWLEDGED"

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Super Status Bar Premium. Use with Spoof Install Source",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger(this::class.java.name)

        val enumInit = EntitlementStateEnumFingerprint.method
        val enumType = enumInit.definingClass

        val enumInsns = enumInit.instructions.toList()
        val ownedNameIndex = enumInsns.indexOfFirst {
            it.opcode == Opcode.CONST_STRING &&
                it.getReference<StringReference>()?.string == OWNED_STATE_NAME
        }
        if (ownedNameIndex < 0) {
            throw PatchException("Could not find the '$OWNED_STATE_NAME' entitlement constant in $enumType.")
        }
        val ownedField = enumInsns.drop(ownedNameIndex).firstNotNullOfOrNull { insn ->
            if (insn.opcode != Opcode.SPUT_OBJECT) return@firstNotNullOfOrNull null
            insn.getReference<FieldReference>()?.takeIf { it.type == enumType }
        } ?: throw PatchException("Could not resolve the owned entitlement field on $enumType.")

        fun Method.isEntitlementConverter() =
            AccessFlags.STATIC.isSet(accessFlags) &&
                returnType == "Z" &&
                parameterTypes.size == 1 &&
                parameterTypes.first().toString() == enumType

        var patched = 0
        classDefForEach { classDef ->
            if (classDef.methods.none { it.isEntitlementConverter() }) return@classDefForEach

            mutableClassDefBy(classDef).methods.forEach { method ->
                if (!method.isEntitlementConverter()) return@forEach

                val insns = method.instructionsOrNull ?: return@forEach
                val readsOwnedField = insns.any {
                    val ref = it.getReference<FieldReference>() ?: return@any false
                    ref.name == ownedField.name &&
                        ref.definingClass == ownedField.definingClass &&
                        ref.type == ownedField.type
                }
                if (!readsOwnedField) return@forEach

                method.returnEarly(true)
                patched++
            }
        }

        if (patched == 0) {
            throw PatchException("Could not find the entitlement '$OWNED_STATE_NAME' converter to patch.")
        }

        logger.info("Super Status Bar Enable Premium: forced $patched entitlement converter(s) to owned.")
    }
}
