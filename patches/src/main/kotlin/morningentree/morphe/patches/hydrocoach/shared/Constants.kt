package morningentree.morphe.patches.hydrocoach.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Hydro Coach",
        packageName = "com.codium.hydrocoach",
        appIconColor = 0x2196F3,
        targets = listOf(
            AppTarget(null),
            AppTarget("5.1.9"),
        ),
    )
}
