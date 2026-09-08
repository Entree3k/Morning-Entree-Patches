package morningentree.morphe.patches.recorder.restrictions

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import morningentree.morphe.patches.recorder.shared.Constants
import morningentree.morphe.util.getReference

private const val LEGACY_PIXEL_FEATURE = "com.google.android.feature.PIXEL_2017_EXPERIENCE"

private fun isPixelFeatureGateString(s: String): Boolean =
    s == LEGACY_PIXEL_FEATURE ||
        (s.startsWith("com.google.android.feature") && s.contains("PIXEL"))

private fun MutableMethod.indexOfPixelFeatureString(): Int =
    instructions.toList().indexOfFirst { instruction ->
        instruction.opcode == Opcode.CONST_STRING &&
            instruction.getReference<StringReference>()?.string
                ?.let(::isPixelFeatureGateString) == true
    }

private fun classHierarchyExtendsAndroidApplication(
    classDef: ClassDef,
    classByType: Map<String, ClassDef>,
): Boolean {
    var parent: String? = classDef.superclass
    while (parent != null) {
        if (parent == "Landroid/app/Application;") return true
        parent = classByType[parent]?.superclass
    }
    return false
}

@Suppress("unused")
val removeDeviceRestrictionsPatch = bytecodePatch(
    name = "Remove device restrictions",
    description = "Removes restrictions from using the app on any device. Requires mounting patched " +
        "app over original.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val method =
            ApplicationOnCreateFingerprint.methodOrNull
                ?: findApplicationOnCreateWithPixelGate()
                ?: throw PatchException(
                    "Remove device restrictions: could not find Application.onCreate " +
                        "(RecorderApplication or direct Application subclass with Pixel feature gate).",
                )

        val featureStringIndex = method.indexOfPixelFeatureString()
        if (featureStringIndex < 0) {
            throw PatchException(
                "Remove device restrictions: expected a Pixel feature const-string in " +
                    "Application.onCreate",
            )
        }

        val moveResultIndex =
            method.instructions.toList().withIndex()
                .firstOrNull { (index, instruction) ->
                    index > featureStringIndex && instruction.opcode == Opcode.MOVE_RESULT
                }?.index
                ?: throw PatchException(
                    "Remove device restrictions: could not find the hasSystemFeature result after " +
                        "the Pixel feature const-string.",
                )

        val resultRegister =
            method.getInstruction<OneRegisterInstruction>(moveResultIndex).registerA

        method.addInstruction(moveResultIndex + 1, "const/16 v$resultRegister, 0x1")
    }
}

private fun BytecodePatchContext.findApplicationOnCreateWithPixelGate(): MutableMethod? {
    val classByType = buildMap {
        classDefForEach { classDef -> put(classDef.type, classDef) }
    }
    var found: MutableMethod? = null
    classDefForEach { classDef ->
        if (found != null) return@classDefForEach
        if (!classHierarchyExtendsAndroidApplication(classDef, classByType)) return@classDefForEach

        found = mutableClassDefBy(classDef).methods.find { method ->
            method.name == "onCreate" &&
                method.returnType == "V" &&
                method.parameterTypes.isEmpty() &&
                method.instructionsOrNull.orEmpty().any { instruction ->
                    instruction.opcode == Opcode.CONST_STRING &&
                        instruction.getReference<StringReference>()?.string
                            ?.let(::isPixelFeatureGateString) == true
                }
        }
    }
    return found
}
