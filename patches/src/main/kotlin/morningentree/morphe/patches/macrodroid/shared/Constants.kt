package morningentree.morphe.patches.macrodroid.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "MacroDroid",
        packageName = "com.arlosoft.macrodroid",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x20334D,
        targets = listOf(AppTarget("5.65.9")),
    )
}
