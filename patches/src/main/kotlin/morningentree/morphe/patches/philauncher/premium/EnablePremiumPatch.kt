package morningentree.morphe.patches.philauncher.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import morningentree.morphe.patches.philauncher.shared.disablePairipLicenseCheckPatch
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Phi Launcher Pro",
) {
    compatibleWith(
        Compatibility(
            name = "Phi Launcher",
            packageName = "com.launcher.hype",
            appIconColor = 0x6C63FF,
            targets = listOf(AppTarget("3.5.1")),
        ),
    )

    dependsOn(disablePairipLicenseCheckPatch)

    execute {
        IsProUserFingerprint.method.returnEarly(true)
    }
}
