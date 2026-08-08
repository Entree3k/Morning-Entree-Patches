package morningentree.morphe.patches.macrodroid.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

private const val PACKAGE_MANAGER = "Landroid/content/pm/PackageManager;"
private const val SIGNATURE = "Landroid/content/pm/Signature;"

internal object ProStatusFingerprint : Fingerprint(
    definingClass = "Lcom/arlosoft/macrodroid/settings/Settings;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    strings = listOf("vcp_count"),
)

internal object SignatureCheckFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    filters = listOf(
        methodCall(definingClass = PACKAGE_MANAGER, name = "getPackageInfo"),
        methodCall(definingClass = SIGNATURE, name = "toCharsString"),
    ),
)

internal object TemplateStoreSignatureCheckFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    filters = listOf(
        methodCall(definingClass = PACKAGE_MANAGER, name = "getPackageInfo"),
        methodCall(definingClass = SIGNATURE, name = "toCharsString"),
    ),
)
