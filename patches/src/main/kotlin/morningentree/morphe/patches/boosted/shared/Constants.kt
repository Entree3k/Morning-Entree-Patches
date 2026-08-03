package morningentree.morphe.patches.boosted.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Boosted Productivity",
        packageName = "com.boostedproductivity.app",
        appIconColor = 0x00C689,
        targets = listOf(
            AppTarget("1.6.21"),
        ),
    )
}
