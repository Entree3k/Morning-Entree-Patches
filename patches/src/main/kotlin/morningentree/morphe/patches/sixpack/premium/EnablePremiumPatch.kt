package morningentree.morphe.patches.sixpack.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.sixpack.shared.Constants
import morningentree.morphe.util.returnEarly

private const val CONTEXT = "Landroid/content/Context;"

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Six Pack in 30 Days premium — removes ads and unlocks all workouts and plans.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val helperType = RemoveAdsGateFingerprint.method.definingClass

        classDefForEach { classDef ->
            if (classDef.type != helperType) return@classDefForEach

            mutableClassDefBy(classDef).methods
                .filter { method ->
                    method.returnType == "Z" &&
                        method.parameterTypes.map { it.toString() }.let {
                            it.isEmpty() || it == listOf(CONTEXT)
                        }
                }
                .forEach { it.returnEarly(true) }
        }
    }
}
