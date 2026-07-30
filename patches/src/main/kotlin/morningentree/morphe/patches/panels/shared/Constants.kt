package morningentree.morphe.patches.panels.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Panels",
        packageName = "com.fossor.panels",
        appIconColor = 0x2196F3,
        targets = listOf(AppTarget("1.596"))
    )
}
