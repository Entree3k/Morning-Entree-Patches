package morningentree.morphe.patches.obdmary.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Obd Mary",
        packageName = "com.elm.elm327.obd2.eobd.obd.car.scanner.diagnostics.tool.dashboard." +
            "doctor.check.engine.torque.speed.trouble.codes.mary",
        appIconColor = 0x17364C,
        targets = listOf(AppTarget("1.318")),
    )
}
