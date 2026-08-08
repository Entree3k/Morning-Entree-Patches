package morningentree.morphe.patches.smashhit.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Smash Hit",
        packageName = "com.mediocre.smashhit",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x1E88E5,
        targets = listOf(AppTarget("1.5.14")),
    )
}
