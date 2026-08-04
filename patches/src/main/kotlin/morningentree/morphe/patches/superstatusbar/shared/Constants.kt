package morningentree.morphe.patches.superstatusbar.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Super Status Bar",
        packageName = "com.tombayley.statusbar",
        appIconColor = 0x20C26B,
        targets = listOf(
            AppTarget("2.13.0"),
        ),
    )
}
