package morningentree.morphe.patches.exams.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.rawResourcePatch
import morningentree.morphe.patches.shared.misc.hex.Replacement
import java.io.FileNotFoundException

private const val BUNDLE_PATH = "assets/index.android.bundle"

private fun exam(name: String, packageName: String, version: String) =
    Compatibility(
        name = name,
        packageName = packageName,
        appIconColor = 0x023C69,
        targets = listOf(AppTarget(version)),
    )

@Suppress("unused")
val unlockAllQuestionsPatch = rawResourcePatch(
    name = "Unlock all questions",
    description = "Unlocks all exam questions",
) {
    compatibleWith(
        exam("CCNA 200-301", "com.trieudadlovestun.ccna200301", "1.0.15"),
        exam("ServiceNow CSA", "com.trieudadlovestun.servicenowsystemadmin", "1.0.10"),
        exam("SC-900", "com.trieudadlovestun.sc900", "1.0.10"),
        exam("AWS SAA-C02", "com.trieudadlovestun.saac02ver3", "1.0.18"),
        exam("ISTQB Foundation", "com.trieudadlovestun.istqbfoundation", "1.0.15"),
        exam("AZ-104", "com.trieudadlovestun.az104", "1.0.16"),
        exam("PCEP-30-02", "com.trieudadlovestun.pcep3002", "1.0.9"),
        exam("CompTIA A+ Core 1", "com.trieudadlovestun.comptiacore1", "1.0.11"),
        exam("CCNP 350-401", "com.trieudadlovestun.ccnp350401", "1.0.18"),
        exam("SC-200", "com.trieudadlovestun.sc200exam", "1.0.2"),
        exam("CISM", "com.trieudadlovestun.cism", "1.0.9"),
        exam("AWS SCS-C01", "com.trieudadlovestun.scsc01", "1.0.14"),
        exam("AI-900", "com.trieudadlovestun.ai900exam", "1.0.1"),
        exam("Salesforce Admin", "com.trieudadlovestun.salesforceadmin", "1.0.16"),
        exam("SnowPro Core", "com.trieudadlovestun.snowprocore", "1.0.10"),
        exam("CCNP SCOR 350-701", "com.trieudadlovestun.scor350701", "1.0.8"),
        exam("CompTIA A+ Core 2", "com.trieudadlovestun.comptiacore2", "1.0.12"),
        exam("AZ-900", "com.trieudadlovestun.az900ver3", "1.0.23"),
        exam("CEH v11", "com.trieudadlovestun.cehv11", "1.0.20"),
        exam("AWS CLF-C01", "com.trieudadlovestun.clfc01", "1.0.20"),
        exam("CompTIA Security+", "com.trieudadlovestun.comptiasecurityplus", "1.0.16"),
        exam("CompTIA Server+", "com.trieudadlovestun.comptiaserverplus", "1.0.10"),
        exam("CompTIA CySA+", "com.trieudadlovestun.comptiacysa", "1.0.12"),
        exam("AZ-500", "com.trieudadlovestun.az500", "1.0.8"),
        exam("CompTIA SecurityX", "com.trieudadlovestun.comptiasecux", "1.0.1"),
        exam("CompTIA PenTest+", "com.trieudadlovestun.pentestplusexam", "1.0.1"),
        exam("AWS MLA-C01", "com.trieudadlovestun.mlac01exam", "1.0.3"),
        exam("DASM", "com.trieudadlovestun.dasm", "1.0.9"),
        exam("CAPM", "com.trieudadlovestun.capm", "1.0.8"),
        exam("AZ-400", "com.trieudadlovestun.az400", "1.0.8"),
        exam("DP-203", "com.trieudadlovestun.dp203", "1.0.9"),
        exam("PSPO I", "com.trieudadlovestun.pspooneexam", "1.0.1"),
        exam("AWS SOA-C02", "com.trieudadlovestun.soac02", "1.0.9"),
        exam("AWS DOP-C01", "com.trieudadlovestun.dopc01", "1.0.6"),
        exam("PSM II", "com.trieudadlovestun.psm2", "1.0.9"),
        exam("AZ-700", "com.trieudadlovestun.az700exam", "1.0.1"),
        exam("AWS SAP-C01", "com.trieudadlovestun.sapc01", "1.0.8"),
        exam("GCP Associate Cloud Engineer", "com.trieudadlovestun.gcpassociatecloudengineer", "1.0.8"),
        exam("DP-900", "com.trieudadlovestun.dp900", "1.0.10"),
        exam("CompTIA Linux+", "com.trieudadlovestun.comptialinuxplusexam", "1.0.2"),
        exam("AWS AIF-C01", "com.trieudadlovestun.aifc01exam", "1.0.1"),
        exam("PMI-ACP", "com.trieudadlovestun.pmiacp", "1.0.6"),
        exam("CompTIA ITF+", "com.trieudadlovestun.comptiaitf", "1.0.14"),
        exam("AWS DVA-C01", "com.trieudadlovestun.dvac01ver3", "1.0.12"),
        exam("AZ-204", "com.trieudadlovestun.az204ver2022", "1.0.9"),
        exam("DP-300", "com.trieudadlovestun.dp300", "1.0.6"),
        exam("CISSP", "com.trieudadlovestun.cissp", "1.0.10"),
        exam("Tableau Desktop", "com.trieudadlovestun.tableaudesktop", "1.0.8"),
        exam("CompTIA SecAI", "com.trieudadlovestun.comptiasecai", "1.0.1"),
        exam("Terraform Associate", "com.trieudadlovestun.terraform", "1.0.10"),
    )

    execute {
        val file = get(BUNDLE_PATH, true)
        if (!file.exists())
            throw FileNotFoundException("React Native bundle not found at: $BUNDLE_PATH")

        val bytes = file.readBytes()

        Replacement(
            "homeScreenSlice',initialState:{isPremium:!1".toByteArray(Charsets.US_ASCII),
            "homeScreenSlice',initialState:{isPremium:!0".toByteArray(Charsets.US_ASCII),
            BUNDLE_PATH,
        ).replacePattern(bytes)

        file.writeBytes(bytes)
    }
}
