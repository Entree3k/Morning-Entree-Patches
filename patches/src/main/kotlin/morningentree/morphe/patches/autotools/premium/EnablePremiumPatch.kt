package morningentree.morphe.patches.autotools.premium

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.autotools.shared.Constants
import morningentree.morphe.util.returnEarly
import java.util.logging.Logger

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks AutoTools",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger(this::class.java.name)
        var patched = 0

        fun isLiteGate(name: String, returnType: String, params: List<*>) =
            name == "isLite" && returnType == "Z" && params.isEmpty()

        classDefForEach { classDef ->
            if (classDef.methods.none { isLiteGate(it.name, it.returnType, it.parameterTypes) }) {
                return@classDefForEach
            }

            mutableClassDefBy(classDef).methods.forEach { method ->
                if (!isLiteGate(method.name, method.returnType, method.parameterTypes)) return@forEach
                if (method.instructionsOrNull == null) return@forEach

                method.returnEarly(false)
                patched++
            }
        }

        logger.info("AutoTools Enable Premium: forced $patched isLite() gate(s) to full version.")
    }
}
