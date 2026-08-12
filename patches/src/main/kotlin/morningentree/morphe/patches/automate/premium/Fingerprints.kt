package morningentree.morphe.patches.automate.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

internal object AutomatePremiumGateFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf(
        "Lcom/llamalab/automate/B0;",
        "Lcom/llamalab/automate/BeginningStatement;",
        "Ljava/lang/Object;",
        "Z",
    ),
    filters = listOf(
        string("runningStatementCount"),
        string("checkPremiumAllow"),
    ),
)

internal object AutomatePremiumQueryFingerprint : Fingerprint(
    definingClass = "Lcom/llamalab/automate/AutomateService;",
    name = "onQueryPremiumCompleted",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Lcom/android/billingclient/api/Purchase;", "Ljava/lang/Throwable;"),
    filters = listOf(
        string("onQueryPremiumCompleted failed"),
    ),
)
