package morningentree.morphe.patches.loseweightmen.premium

import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.loseweightmen.shared.Constants
import morningentree.morphe.util.returnEarly

private const val CONTEXT = "Landroid/content/Context;"

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Lose Weight App for Men Premium. Use with Spoof App Signature",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val helperType = IsPremiumGateFingerprint.method.definingClass

        classDefForEach { classDef ->
            if (classDef.type != helperType) return@classDefForEach

            mutableClassDefBy(classDef).methods
                .filter { method ->
                    method.returnType == "Z" &&
                        method.parameterTypes.map { it.toString() } == listOf(CONTEXT)
                }
                .forEach { it.returnEarly(true) }
        }
    }
}
