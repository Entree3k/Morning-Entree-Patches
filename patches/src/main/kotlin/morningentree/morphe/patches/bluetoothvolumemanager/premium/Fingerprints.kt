package morningentree.morphe.patches.bluetoothvolumemanager.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

val InfoConstructorFingerprint = Fingerprint(
    definingClass = "Leu/darken/bluemusic/upgrade/core/UpgradeRepoGplay\$Info;",
    name = "<init>",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf(
        "Leu/darken/bluemusic/upgrade/core/billing/BillingData;",
        "Ljava/lang/Throwable;",
        "I",
    ),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IPUT_BOOLEAN,
            definingClass = "Leu/darken/bluemusic/upgrade/core/UpgradeRepoGplay\$Info;",
            name = "isUpgraded",
        ),
    ),
)
