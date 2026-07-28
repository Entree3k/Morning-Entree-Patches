package morningentree.morphe.patches.gradientweather.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object InitSubscriptionTierFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
    strings = listOf("is_lifetime", "is_premium"),
)

internal object SetSubscriptionTierFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    strings = listOf("override_state"),
    custom = { method, _ -> method.parameterTypes.size == 1 },
)
