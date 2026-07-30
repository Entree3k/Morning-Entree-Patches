package morningentree.morphe.patches.autotools.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "AutoTools",
        packageName = "com.joaomgcd.autotools",
        appIconColor = 0xFF9800,
        targets = listOf(
            AppTarget("2.3.19"),
        ),
    )
}
