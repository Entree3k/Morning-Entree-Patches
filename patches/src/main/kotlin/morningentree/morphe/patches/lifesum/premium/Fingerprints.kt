package morningentree.morphe.patches.lifesum.premium

import app.morphe.patcher.Fingerprint

internal object ApiUserProfileGetPremiumFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/lifesum/profile/network/models/ApiUserProfile;" &&
                method.name == "getPremium"
    }
)

internal object HasPremiumFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/sillens/shapeupclub/db/models/ProfileModelExtensionsKt;" &&
                method.name == "hasPremium"
    }
)
