package morningentree.morphe.patches.paisa.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Paisa",
        packageName = "dev.hemanths.paisa",
        appIconColor = 0x009688,
        targets = listOf(
            AppTarget("2026.06.230"),
        ),
    )
}
