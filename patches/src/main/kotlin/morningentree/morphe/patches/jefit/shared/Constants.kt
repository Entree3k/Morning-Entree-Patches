package morningentree.morphe.patches.jefit.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "JEFIT",
        packageName = "je.fit",
        appIconColor = 0x0B6FB8,
        targets = listOf(
            AppTarget("17.2.10"),
        ),
    )
}
