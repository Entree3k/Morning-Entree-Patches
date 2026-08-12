package morningentree.morphe.patches.nzb360.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object IsAASubscriptionActiveFingerprint : Fingerprint(
    definingClass = "Lcom/kevinforeman/nzb360/helpers/NZB360LicenseHelper;",
    name = "isAASubscriptionActive",
    parameters = listOf(),
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

internal object IsUnlockedFingerprint : Fingerprint(
    definingClass = "Lcom/kevinforeman/nzb360/helpers/NZB360LicenseHelper;",
    name = "isUnlocked",
    parameters = listOf(
        "Lcom/kevinforeman/nzb360/helpers/NZB360LicenseHelper\$Service;",
        "Z",
    ),
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

internal object IsLockedTwoArgFingerprint : Fingerprint(
    definingClass = "Lcom/kevinforeman/nzb360/helpers/NZB360LicenseHelper;",
    name = "isLocked",
    parameters = listOf(
        "Lcom/kevinforeman/nzb360/helpers/NZB360LicenseHelper\$Service;",
        "Z",
    ),
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

internal object IsLockedOneArgFingerprint : Fingerprint(
    definingClass = "Lcom/kevinforeman/nzb360/helpers/NZB360LicenseHelper;",
    name = "isLocked",
    parameters = listOf("Lcom/kevinforeman/nzb360/helpers/NZB360LicenseHelper\$Service;"),
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

internal object SubscriptionSectionDefaultPlanFingerprint : Fingerprint(
    definingClass = "Lcom/kevinforeman/nzb360/upgradecenter/UpgradeCenterKt;",
    name = "SubscriptionSection",
    strings = listOf("Monthly"),
)

internal object IsSubscribedFingerprint : Fingerprint(
    definingClass = "Lcom/kevinforeman/nzb360/upgradecenter/Contract\$UIState;",
    name = "isSubscribed",
    parameters = listOf(),
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)
