package morningentree.morphe.patches.lightmeter.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Lightmeter",
        packageName = "com.vodemn.lightmeter",
        appIconColor = 0x212121,
        targets = listOf(
            AppTarget(null),
            AppTarget("2.5.0"),
        ),
    )
}
