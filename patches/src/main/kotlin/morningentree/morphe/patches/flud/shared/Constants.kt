package morningentree.morphe.patches.flud.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Flud",
        packageName = "com.delphicoder.flud",
        apkFileType = ApkFileType.APKS,
        appIconColor = 0xF16522,
        targets = listOf(
            AppTarget(null),
            AppTarget("2.0.13-beta02"),
        ),
    )
}
