package morningentree.morphe.patches.hydrocoach.premium

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.hydrocoach.shared.Constants
import morningentree.morphe.util.returnEarly
import java.util.logging.Logger

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Hydro Coach Pro",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger(this::class.java.name)

        val forcedTrue = setOf(
            "getHasProFeatures",
            "getPurchasedPro",
            "getPurchasedNoAds",
            "getPurchasedUpgradeNoAdsToPro",
            "isSubscribedToPro",
            "getHasAllThemes",
        )
        val forcedFalse = setOf(
            "getShowAds",
        )

        val anchor = "getHasProFeatures"
        var patched = 0

        classDefForEach { classDef ->
            val isAccountModel = classDef.methods.any { method ->
                method.name == anchor && method.returnType == "Z"
            }
            if (!isAccountModel) return@classDefForEach

            for (method in mutableClassDefBy(classDef).methods) {
                if (method.returnType != "Z") continue
                when (method.name) {
                    in forcedTrue -> {
                        method.returnEarly(true)
                        patched++
                    }
                    in forcedFalse -> {
                        method.returnEarly(false)
                        patched++
                    }
                }
            }
        }

        if (patched == 0) {
            throw PatchException(
                "Hydro Coach: account model (class declaring getHasProFeatures) not found.",
            )
        }
        logger.info("Hydro Coach: forced $patched premium accessor(s) on the account model.")
    }
}
