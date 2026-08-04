package morningentree.morphe.patches.listy.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Listy",
        packageName = "pro.listy",
        appIconColor = 0x007AFF,
        targets = listOf(
            AppTarget("1.3.4"),
        ),
    )
}
