package morningentree.morphe.patches.gymworkout.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Gym Workout",
        packageName = "gymworkout.gym.gymlog.gymtrainer",
        appIconColor = 0xFF6D00,
        targets = listOf(
            AppTarget("1.6.3"),
        ),
    )
}
