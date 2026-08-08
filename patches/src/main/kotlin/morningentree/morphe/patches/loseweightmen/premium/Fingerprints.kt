package morningentree.morphe.patches.loseweightmen.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object IsPremiumGateFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    strings = listOf("LmUmbARzLXc2aQFoTS4Kbz9lOGUjZ110EXA-ZgByImUtLj9lAmcgdD9vFXNfbxRtKW5hci9tWnYVYSpz"),
)
