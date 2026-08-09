package morningentree.morphe.util

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode

val COMMON_REVENUECAT_ENTITLEMENT_IDS = listOf(
    "premium",
    "pro",
    "plus",
    "full",
    "vip",
    "gold",
    "elite",
    "premium_access",
    "pro_access",
    "all_access",
    "full_access",
    "unlimited",
    "lifetime",
    "standard",
    "subscription",
    "paid",
    "premium_annual",
    "premium_monthly",
)

fun MutableMethod.injectActiveRevenueCatEntitlements(
    entitlementIds: List<String> = COMMON_REVENUECAT_ENTITLEMENT_IDS,
) {
    val returnIndex = instructions.toList().indexOfFirst { it.opcode == Opcode.RETURN_OBJECT }
    if (returnIndex < 0) {
        throw PatchException("Could not find the return in EntitlementInfosMapperKt.map().")
    }

    val buildSynthetic = """
        new-instance v0, Ljava/util/LinkedHashMap;
        invoke-direct {v0}, Ljava/util/LinkedHashMap;-><init>()V

        const-string v1, "identifier"
        const-string v2, "premium"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "isActive"
        sget-object v2, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "willRenew"
        sget-object v2, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "periodType"
        const-string v2, "NORMAL"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "latestPurchaseDateMillis"
        const-wide/16 v4, 0x0
        invoke-static {v4, v5}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;
        move-result-object v2
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "latestPurchaseDate"
        const-string v2, "2020-01-01T00:00:00Z"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "originalPurchaseDateMillis"
        const-wide/16 v4, 0x0
        invoke-static {v4, v5}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;
        move-result-object v2
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "originalPurchaseDate"
        const-string v2, "2020-01-01T00:00:00Z"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "expirationDateMillis"
        const/4 v2, 0x0
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "expirationDate"
        const/4 v2, 0x0
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "store"
        const-string v2, "PLAY_STORE"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "productIdentifier"
        const-string v2, "premium"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "productPlanIdentifier"
        const/4 v2, 0x0
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "isSandbox"
        sget-object v2, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "unsubscribeDetectedAt"
        const/4 v2, 0x0
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "unsubscribeDetectedAtMillis"
        const/4 v2, 0x0
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "billingIssueDetectedAt"
        const/4 v2, 0x0
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "billingIssueDetectedAtMillis"
        const/4 v2, 0x0
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "ownershipType"
        const-string v2, "PURCHASED"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "verification"
        const-string v2, "NOT_REQUESTED"
        invoke-virtual {v0, v1, v2}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

        const-string v1, "active"
        invoke-interface {p0, v1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;
        move-result-object v1
        check-cast v1, Ljava/util/Map;

        const-string v3, "all"
        invoke-interface {p0, v3}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;
        move-result-object v3
        check-cast v3, Ljava/util/Map;
    """.trimIndent()

    val putUnderEachId = entitlementIds.joinToString("\n") { id ->
        """
        const-string v2, "$id"
        invoke-interface {v1, v2, v0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        invoke-interface {v3, v2, v0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        """.trimIndent()
    }

    addInstructions(returnIndex, "$buildSynthetic\n$putUnderEachId")
}
