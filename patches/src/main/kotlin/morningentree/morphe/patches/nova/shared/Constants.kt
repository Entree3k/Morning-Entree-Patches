package morningentree.morphe.patches.nova.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Nova Launcher",
        packageName = "com.teslacoilsw.launcher",
        appIconColor = 0xDA4624,
        targets = listOf(
            // Nova's versionName is "<versionCode> (<semver>)".
            AppTarget("88600 (8.8.6)"),
        ),
    )
}
