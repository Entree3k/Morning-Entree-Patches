package morningentree.morphe.patches.bluetoothvolumemanager.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Bluetooth Volume Manager",
        packageName = "eu.darken.bluemusic",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x2196F3,
        targets = listOf(
            AppTarget(null),
            AppTarget("3.4.3"),
        ),
    )
}
