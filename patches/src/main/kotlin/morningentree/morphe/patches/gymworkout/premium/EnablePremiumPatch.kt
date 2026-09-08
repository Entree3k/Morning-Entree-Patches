package morningentree.morphe.patches.gymworkout.premium

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import morningentree.morphe.patches.gymworkout.shared.Constants
import morningentree.morphe.util.getReference
import java.util.logging.Logger

private const val APP_PREFIX = "Lgymworkout/"

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Gym Workout premium",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger(this::class.java.name)

        val appReaderClasses = HashMap<String, MutableSet<String>>()
        val allReaderClasses = HashMap<String, MutableSet<String>>()
        val refByKey = HashMap<String, FieldReference>()

        classDefForEach { classDef ->
            val isAppClass = classDef.type.startsWith(APP_PREFIX)
            for (method in classDef.methods) {
                val insns = method.instructionsOrNull ?: continue
                for (insn in insns) {
                    if (insn.opcode != Opcode.SGET_BOOLEAN) continue
                    val ref = insn.getReference<FieldReference>() ?: continue
                    val key = "${ref.definingClass}->${ref.name}"
                    refByKey[key] = ref
                    allReaderClasses.getOrPut(key) { hashSetOf() }.add(classDef.type)

                    if (isAppClass) {
                        val dc = ref.definingClass
                        val external = !dc.startsWith(APP_PREFIX) &&
                            !dc.startsWith("Landroid") &&
                            !dc.startsWith("Ljava/") &&
                            !dc.startsWith("Lkotlin/") &&
                            !dc.startsWith("Landroidx/")
                        if (external) {
                            appReaderClasses.getOrPut(key) { hashSetOf() }.add(classDef.type)
                        }
                    }
                }
            }
        }

        val topEntry = appReaderClasses.maxByOrNull { it.value.size }
            ?: throw PatchException("Gym Workout: no candidate premium flag found.")
        if (topEntry.value.size < 3) {
            throw PatchException(
                "Gym Workout: best premium-flag candidate is read by only ${topEntry.value.size} " +
                    "app class(es); aborting rather than patch the wrong field.",
            )
        }

        val premiumField = refByKey.getValue(topEntry.key)
        val targetClasses = allReaderClasses.getValue(topEntry.key)
        logger.info(
            "Gym Workout: premium flag = ${premiumField.definingClass}->${premiumField.name} " +
                "(read by ${topEntry.value.size} app classes, ${targetClasses.size} total).",
        )

        var patched = 0
        classDefForEach { classDef ->
            if (classDef.type !in targetClasses) return@classDefForEach

            for (method in mutableClassDefBy(classDef).methods) {
                val insns = method.instructionsOrNull?.toList() ?: continue
                for (index in insns.indices.reversed()) {
                    val insn = insns[index]
                    if (insn.opcode != Opcode.SGET_BOOLEAN) continue
                    val ref = insn.getReference<FieldReference>() ?: continue
                    if (ref.definingClass != premiumField.definingClass ||
                        ref.name != premiumField.name
                    ) {
                        continue
                    }
                    val register = (insn as OneRegisterInstruction).registerA
                    method.replaceInstruction(index, "const/16 v$register, 0x1")
                    patched++
                }
            }
        }

        if (patched == 0) {
            throw PatchException("Gym Workout: no premium-flag reads were patched.")
        }
        logger.info("Gym Workout: forced $patched premium-flag read(s) to true.")
    }
}
