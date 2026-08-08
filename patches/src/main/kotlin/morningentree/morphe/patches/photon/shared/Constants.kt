package morningentree.morphe.patches.photon.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Photon Camera",
        packageName = "com.hinnka.mycamera",
        appIconColor = 0x252525,
        targets = listOf(AppTarget("1.25.1.2")),
    )
}
