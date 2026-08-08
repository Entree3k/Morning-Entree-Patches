package morningentree.morphe.patches.loseweightmen.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Lose Weight App for Men",
        packageName = "menloseweight.loseweightappformen.weightlossformen",
        appIconColor = 0x0DA98D,
        targets = listOf(
            AppTarget("2.4.37"),
        ),
    )
}
