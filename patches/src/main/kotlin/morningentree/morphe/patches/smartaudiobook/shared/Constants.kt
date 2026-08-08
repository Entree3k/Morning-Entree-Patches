package morningentree.morphe.patches.smartaudiobook.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Smart AudioBook Player",
        packageName = "ak.alizandro.smartaudiobookplayer",
        appIconColor = 0xEF5350,
        targets = listOf(AppTarget("11.8.2")),
    )
}
