package morningentree.morphe.patches.pinout.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "PinOut",
        packageName = "com.mediocre.pinout",
        apkFileType = ApkFileType.APK,
        appIconColor = 0xE91E63,
        targets = listOf(AppTarget("1.0.7")),
    )
}
