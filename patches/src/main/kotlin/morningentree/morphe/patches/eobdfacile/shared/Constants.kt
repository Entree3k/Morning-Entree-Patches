package morningentree.morphe.patches.eobdfacile.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "EOBD Facile",
        packageName = "org.eobdfacile.android",
        // Shipped as a single universal APK, not a split bundle.
        apkFileType = ApkFileType.APK,
        appIconColor = 0x1565C0,
        targets = listOf(AppTarget("3.87.1179")),
    )
}
