package morningentree.morphe.patches.projectivy.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks Projectivy Launcher Premium",
) {
    compatibleWith(
        Compatibility(
            name = "Projectivy Launcher",
            packageName = "com.spocky.projengmenu",
            appIconColor = 0xF08029,
            targets = listOf(AppTarget("4.71")),
        ),
    )

    execute {
        var handled = false
        classDefForEach { classDef ->
            if (classDef.type != "Lcom/spocky/projengmenu/PTApplication;") return@classDefForEach

            val methods = mutableClassDefBy(classDef).methods

            val constructor = methods.singleOrNull { it.name == "<init>" }
                ?: throw PatchException("Projectivy: PTApplication.<init> not found.")
            val seeds = constructor.implementation?.instructions?.withIndex()
                ?.filter { (_, instruction) ->
                    (instruction as? ReferenceInstruction)?.reference?.toString() ==
                        "Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;"
                }?.toList() ?: emptyList()
            if (seeds.size != 1) {
                throw PatchException(
                    "Projectivy: expected exactly one Boolean.TRUE seed in PTApplication.<init> but found " +
                        "${seeds.size}. Re-derive which StateFlow holds the premium gate.",
                )
            }
            val (seedIndex, seedInstruction) = seeds.single()
            val seedRegister = (seedInstruction as OneRegisterInstruction).registerA
            constructor.replaceInstruction(
                seedIndex,
                "sget-object v$seedRegister, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;",
            )

            val staticWriter = methods.singleOrNull { method ->
                AccessFlags.STATIC.isSet(method.accessFlags) &&
                    method.returnType == "V" &&
                    method.parameterTypes.map { it.toString() } ==
                    listOf("Lcom/spocky/projengmenu/PTApplication;", "Z")
            } ?: throw PatchException(
                "Projectivy: the static license re-check writer a(PTApplication, boolean) was not found. " +
                    "The gate shape changed and the chokepoint must be re-derived.",
            )
            staticWriter.addInstructions(0, "return-void")

            handled = true
        }

        if (!handled) {
            throw PatchException(
                "Projectivy: PTApplication not found. This patch targets 4.71; the application class " +
                    "moved and the chokepoint must be re-derived.",
            )
        }
    }
}
