package morningentree.morphe.patches.commute.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Does Not Commute",
        packageName = "com.mediocre.commute",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x2E7D6F,
        targets = listOf(AppTarget("1.5.5")),
    )
}
