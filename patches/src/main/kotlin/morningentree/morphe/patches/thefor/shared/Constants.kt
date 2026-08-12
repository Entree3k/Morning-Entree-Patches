package morningentree.morphe.patches.thefor.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "TheFor",
        packageName = "xyz.thefor.habits.habits",
        appIconColor = 0x4CAF50,
        targets = listOf(
            AppTarget(null),
            AppTarget("1.4.6"),
        ),
    )
}
