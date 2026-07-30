package morningentree.morphe.patches.ubktouch.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "UbikiTouch",
        packageName = "eu.toneiv.ubktouch",
        appIconColor = 0x2196F3,
        targets = listOf(
            AppTarget("1.17.6"),
        ),
    )
}
