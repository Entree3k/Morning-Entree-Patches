package morningentree.morphe.patches.sleep.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Sleep as Android",
        packageName = "com.urbandroid.sleep",
        appIconColor = 0x80C583,
        targets = listOf(AppTarget("20260616")),
    )
}
