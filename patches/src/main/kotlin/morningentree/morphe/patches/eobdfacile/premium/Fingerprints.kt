package morningentree.morphe.patches.eobdfacile.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object LicenseLevelFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "I",
    parameters = listOf("Landroid/content/Context;"),
    strings = listOf("LSNV", "TP", "CP", "TB", "CB", "TU", "CU"),
)
