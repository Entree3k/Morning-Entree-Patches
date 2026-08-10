package morningentree.morphe.patches.jefit.elite

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object IsEliteFingerprint : Fingerprint(
    definingClass = "Lje/fit/account/v2/JefitAccountV2;",
    name = "isElite",
    returnType = "Z",
    parameters = emptyList(),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

object IsProFingerprint : Fingerprint(
    definingClass = "Lje/fit/account/v2/JefitAccountV2;",
    name = "isPro",
    returnType = "Z",
    parameters = emptyList(),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

object GetAccountTypeFingerprint : Fingerprint(
    definingClass = "Lje/fit/account/v2/JefitAccountV2;",
    name = "getAccountType",
    returnType = "I",
    parameters = emptyList(),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
)

object FunctionAccountTypeFingerprint : Fingerprint(
    definingClass = "Lje/fit/Function;",
    name = "accountType",
    returnType = "I",
    parameters = emptyList(),
    accessFlags = listOf(AccessFlags.PUBLIC),
)

object IsEliteUserFingerprint : Fingerprint(
    definingClass = "Lje/fit/charts/ExerciseChartRepository;",
    name = "isEliteUser",
    returnType = "Z",
    parameters = emptyList(),
    accessFlags = listOf(AccessFlags.PUBLIC),
)
