package morningentree.morphe.patches.nzb360.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "nzb360",
        packageName = "com.kevinforeman.nzb360",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x1565C0,
        targets = listOf(
            AppTarget(null),
            AppTarget("24.1"),
        ),
    )
}
