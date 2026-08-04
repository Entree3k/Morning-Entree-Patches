package morningentree.morphe.patches.volumestyles.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Volume Styles",
        packageName = "com.tombayley.volumepanel",
        appIconColor = 0x2D7AED,
        targets = listOf(
            AppTarget("5.2.1"),
        ),
    )
}
