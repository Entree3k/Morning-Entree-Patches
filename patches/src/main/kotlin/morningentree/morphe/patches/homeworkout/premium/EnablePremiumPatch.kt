package morningentree.morphe.patches.homeworkout.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.homeworkout.shared.Constants
import morningentree.morphe.util.returnEarly

private const val STRING = "Ljava/lang/String;"

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Home Workout Premium",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val iapSpType = IapSpFingerprint.method.definingClass

        classDefForEach { classDef ->
            if (classDef.type != iapSpType) return@classDefForEach

            mutableClassDefBy(classDef).methods
                .filter { method ->
                    method.returnType == "Z" &&
                        method.parameterTypes.map { it.toString() } == listOf(STRING)
                }
                .forEach { it.returnEarly(false) }
        }
    }
}
