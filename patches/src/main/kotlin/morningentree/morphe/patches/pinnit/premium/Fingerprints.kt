package morningentree.morphe.patches.pinnit.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object PurchaseStatusFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf("Lcom/revenuecat/purchases/CustomerInfo;"),
    strings = listOf("pro"),
)

internal object PurchaseStatusGetterFingerprint : Fingerprint(
    strings = listOf("Fetching purchase status failed"),
)

internal object RestorePurchaseFingerprint : Fingerprint(
    strings = listOf("Restore purchase failed"),
)
