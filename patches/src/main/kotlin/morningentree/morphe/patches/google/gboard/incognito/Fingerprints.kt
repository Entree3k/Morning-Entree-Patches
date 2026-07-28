package morningentree.morphe.patches.google.gboard.incognito

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private const val IME_FLAG_NO_PERSONALIZED_LEARNING: Long = 0x1000000 // 16777216

private val imeNoPersonalizedLearningFilters =
    listOf(
        opcode(Opcode.IF_EQZ),
        fieldAccess(
            definingClass = "Landroid/view/inputmethod/EditorInfo;",
            name = "imeOptions",
            type = "I",
            location = MatchAfterImmediately(),
        ),
        literal(
            literal = IME_FLAG_NO_PERSONALIZED_LEARNING,
            location = MatchAfterImmediately(),
        ),
        opcode(Opcode.AND_INT_2ADDR, MatchAfterImmediately()),
        opcode(Opcode.IF_EQZ, MatchAfterImmediately()),
        opcode(Opcode.CONST_4, MatchAfterImmediately()),
        opcode(Opcode.RETURN, MatchAfterImmediately()),
    )

internal object IsIncognitoModeFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = listOf("Landroid/view/inputmethod/EditorInfo;"),
    filters = imeNoPersonalizedLearningFilters,
)

internal object IsIncognitoModeV2Fingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf(),
    filters = imeNoPersonalizedLearningFilters,
)

internal object IsIncognitoModeInlinedFingerprint : Fingerprint(
    name = "onStartInput",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Landroid/view/inputmethod/EditorInfo;", "Z"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.IF_EQZ,
        Opcode.IGET,
        Opcode.CONST_HIGH16,
        Opcode.AND_INT_2ADDR,
        Opcode.IF_EQZ,
        Opcode.MOVE,
        Opcode.GOTO,
        Opcode.CONST_4,
        Opcode.INVOKE_VIRTUAL,
    ),
)

internal object OnPrimaryClipChangedFingerprint : Fingerprint(
    name = "onPrimaryClipChanged",
    returnType = "V",
    parameters = listOf(),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.INVOKE_DIRECT,
        Opcode.MOVE_RESULT,
        Opcode.IF_NEZ,
    ),
    strings = listOf("clipboard_primary_uri", ""),
)

internal object EnableVoiceTypingFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(
        "Landroid/content/Context;",
        "Landroid/view/inputmethod/EditorInfo;",
        "Z",
    ),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.RETURN,
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT,
        Opcode.IF_NEZ,
        Opcode.IF_EQZ,
    ),
)
