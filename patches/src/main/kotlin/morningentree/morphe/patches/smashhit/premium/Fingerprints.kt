package morningentree.morphe.patches.smashhit.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess

private const val ANDROID_STORE = "Lcom/mediocre/smashhit/AndroidStore;"
private const val COMMAND_HANDLER = "Lcom/mediocre/smashhit/CommandHandler;"
private const val COMMAND_MODEL = "Lcom/mediocre/smashhit/CommandThreadsafeModel;"
private const val ATOMIC_BOOLEAN = "Ljava/util/concurrent/atomic/AtomicBoolean;"

internal object OwnsPremiumProductFingerprint : Fingerprint(
    name = "ownsPremiumProduct",
    returnType = "Z",
    parameters = emptyList(),
    custom = { _, classDef -> classDef.type == ANDROID_STORE },
)

internal object IsProductOwnedFingerprint : Fingerprint(
    name = "isProductOwned",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
    custom = { _, classDef -> classDef.type == COMMAND_MODEL },
)

internal object HasRefreshedOwnedProductsFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf("[Ljava/lang/String;"),
    filters = listOf(
        fieldAccess(
            definingClass = COMMAND_MODEL,
            name = "hasRefreshedOwnedProducts",
            type = ATOMIC_BOOLEAN,
        ),
    ),
    custom = { _, classDef -> classDef.type == COMMAND_HANDLER },
)

internal object IsPremiumProductRestoredFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf("[Ljava/lang/String;"),
    filters = listOf(
        fieldAccess(
            definingClass = COMMAND_MODEL,
            name = "isPremiumProductRestored",
            type = ATOMIC_BOOLEAN,
        ),
    ),
    custom = { _, classDef -> classDef.type == COMMAND_HANDLER },
)
