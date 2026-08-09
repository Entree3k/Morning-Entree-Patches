package morningentree.morphe.patches.fakegpslocation.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Fake GPS Location",
        packageName = "com.hopefactory2021.fakegpslocation",
        appIconColor = 0x5F7C8A,
        targets = listOf(
            AppTarget(null),
            AppTarget("125.0"),
        ),
    )
}
