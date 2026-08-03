package morningentree.morphe.patches.todomate.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import morningentree.morphe.patches.todomate.shared.Constants
import morningentree.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks Todo Mate Premium. Use With Spoof Install Source",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        EntitlementInfosMapperFingerprint.method.apply {
            val returnIndex = instructions.toList().indexOfFirst { it.opcode == Opcode.RETURN_OBJECT }
            if (returnIndex < 0) {
                throw PatchException("Could not find the return in EntitlementInfosMapperKt.map().")
            }

            addInstructions(
                returnIndex,
                """
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
                    const-string v2, "todo_mate_premium_yearly_plan"
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

                    const-string v1, "all"
                    invoke-interface {p0, v1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;
                    move-result-object v1
                    check-cast v1, Ljava/util/Map;
                    const-string v2, "premium"
                    invoke-interface {v1, v2, v0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

                    const-string v1, "active"
                    invoke-interface {p0, v1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;
                    move-result-object v1
                    check-cast v1, Ljava/util/Map;
                    const-string v2, "premium"
                    invoke-interface {v1, v2, v0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
                """.trimIndent(),
            )
        }

        EntitlementInfoIsActiveFingerprint.method.returnEarly(true)
    }
}
