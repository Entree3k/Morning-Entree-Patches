package morningentree.morphe.patches.gradientweather.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Gradient Weather",
        packageName = "com.subtlesignals.gradientweather",
        appIconColor = 0x6C7BFF,
        targets = listOf(
            AppTarget("1.1.1"),
        ),
    )
}
