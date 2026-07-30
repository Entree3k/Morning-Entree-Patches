package morningentree.morphe.patches.pinnit.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Pinnit",
        packageName = "dev.sasikanth.pinnit2",
        appIconColor = 0xE0563F,
        targets = listOf(
            AppTarget("1.13.0"),
        ),
    )
}
