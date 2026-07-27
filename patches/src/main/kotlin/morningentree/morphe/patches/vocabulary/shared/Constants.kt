package morningentree.morphe.patches.vocabulary.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Vocabulary",
        packageName = "com.hrd.vocabulary",
        appIconColor = 0xC7604F,
        targets = listOf(
            AppTarget("5.5.1"),
        ),
    )
}
