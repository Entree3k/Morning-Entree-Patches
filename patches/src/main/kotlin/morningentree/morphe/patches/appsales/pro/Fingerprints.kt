package morningentree.morphe.patches.appsales.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.Opcode

object GetBooleanPrefFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;", "Z"),
    strings = listOf("prefKey"),
)

object GetStringPrefFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf("Ljava/lang/String;"),
    strings = listOf("prefKey"),
)

object ClearPremiumUserFingerprint : Fingerprint(
    returnType = "V",
    parameters = emptyList(),
    strings = listOf("pu", "premium_sku", "premium_ack_required"),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.SGET_OBJECT,
            definingClass = "Ljava/lang/Boolean;",
            name = "FALSE",
            type = "Ljava/lang/Boolean;",
        ),
    ),
)

object ShowPremiumDialogFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Ljava/util/List;"),
    strings = listOf("premiumSkuYearly", "as_premium_monthly"),
)
