package morningentree.morphe.patches.homeworkout.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Home Workout - No Equipment",
        packageName = "homeworkout.homeworkouts.noequipment",
        appIconColor = 0x004AFF,
        targets = listOf(
            AppTarget("1.7.6"),
        ),
    )
}
