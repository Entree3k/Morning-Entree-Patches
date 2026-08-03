package morningentree.morphe.patches.todomate.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Todo Mate",
        packageName = "com.undefined.mate",
        appIconColor = 0x00C9A7,
        targets = listOf(
            AppTarget("5.15.8"),
        ),
    )
}
