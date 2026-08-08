package morningentree.morphe.patches.armworkout.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Arm Workout",
        packageName = "armworkout.armworkoutformen.armexercises",
        appIconColor = 0xFF5252,
        targets = listOf(
            AppTarget("2.4.3"),
        ),
    )
}
