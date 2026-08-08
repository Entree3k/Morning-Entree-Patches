package morningentree.morphe.patches.obdmary.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object IsDiagnosticsEditionOwnedFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("IS_DIAGNOSTICS_EDITION_OWNED", "RUS_IS_DIAGNOSTICS_EDITION_OWNED"),
)

internal object IsFreeAppFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("IS_FREE_APP", "RUS_IS_FREE_APP"),
)

internal object IsFullAppSubscriptionPurchasedFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = emptyList(),
    strings = listOf("IS_FULL_APP_SUBS_PURCHASED"),
)
