package morningentree.morphe.patches.materialcapsule.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Material Capsule",
        packageName = "com.pryshedko.mtisland",
        appIconColor = 0x212121,
        targets = listOf(AppTarget("15.7")),
    )
}
