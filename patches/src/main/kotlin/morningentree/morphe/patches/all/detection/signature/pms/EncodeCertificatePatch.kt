package morningentree.morphe.patches.all.detection.signature.pms

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patcher.patch.stringOption
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Base64
import java.util.logging.Logger
import java.util.zip.ZipInputStream

internal var signature: String? = null
    private set

private val log = Logger.getLogger("SpoofSignatureCertExtractor")

private fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)

private fun parseFirstX509(stream: InputStream): ByteArray? =
    CertificateFactory.getInstance("X.509")
        .generateCertificates(stream)
        .filterIsInstance<X509Certificate>()
        .firstOrNull()
        ?.encoded

private fun isCertEntry(name: String): Boolean =
    name.startsWith("META-INF/") && name.substringAfterLast('.') in setOf("RSA", "DSA", "EC")

private fun certFromApkBytes(apkBytes: ByteArray): ByteArray? {
    ZipInputStream(ByteArrayInputStream(apkBytes)).use { zis ->
        while (true) {
            val entry = zis.nextEntry ?: break
            if (!entry.isDirectory && isCertEntry(entry.name)) {
                parseFirstX509(zis)?.let { return it }
            }
        }
    }
    return null
}

private fun extractFromSigningBlock(apkBytes: ByteArray): ByteArray? {
    val buf = ByteBuffer.wrap(apkBytes).order(ByteOrder.LITTLE_ENDIAN)
    var eocdOffset = -1
    for (i in apkBytes.size - 22 downTo maxOf(0, apkBytes.size - 65557)) {
        if (buf.getInt(i) == 0x06054b50) { eocdOffset = i; break }
    }
    if (eocdOffset < 0) return null
    val cdOffset = buf.getInt(eocdOffset + 16)
    val blockEnd = cdOffset
    if (blockEnd < 32) return null
    val magic = byteArrayOf(
        0x41, 0x50, 0x4b, 0x20, 0x53, 0x69, 0x67, 0x20,
        0x42, 0x6c, 0x6f, 0x63, 0x6b, 0x20, 0x34, 0x32,
    )
    if (!apkBytes.copyOfRange(blockEnd - 16, blockEnd).contentEquals(magic)) return null
    val blockSize = buf.getLong(blockEnd - 24)
    val blockStart = blockEnd - blockSize.toInt() - 8
    if (blockStart < 0) return null
    var pos = blockStart + 8
    val pairsEnd = blockEnd - 24
    while (pos < pairsEnd - 12) {
        val pairLen = buf.getLong(pos).toInt()
        val pairId = buf.getInt(pos + 8)
        val valueStart = pos + 12
        val valueEnd = pos + 8 + pairLen
        if (pairId == 0x7109871a || pairId == 0xf05368c0.toInt()) {
            if (valueEnd > valueStart + 28) {
                try {
                    val v = ByteBuffer.wrap(apkBytes, valueStart, valueEnd - valueStart)
                        .order(ByteOrder.LITTLE_ENDIAN)
                    v.int; v.int; v.int
                    val digestsLen = v.int; v.position(v.position() + digestsLen)
                    v.int
                    val certLen = v.int
                    if (certLen > 0 && certLen < apkBytes.size) {
                        val certBytes = ByteArray(certLen); v.get(certBytes)
                        val cert = CertificateFactory.getInstance("X.509")
                            .generateCertificate(ByteArrayInputStream(certBytes)) as? X509Certificate
                        if (cert != null) return cert.encoded
                    }
                } catch (_: Exception) {
                }
            }
        }
        pos = valueStart + pairLen - 4
        if (pairLen <= 4) break
    }
    return null
}

private fun extractFromFile(file: File): Boolean {
    val fileBytes = file.readBytes()
    var apkBytes: ByteArray? = null
    ZipInputStream(ByteArrayInputStream(fileBytes)).use { outer ->
        while (true) {
            val entry = outer.nextEntry ?: break
            if (!entry.isDirectory && (entry.name == "base.apk" || entry.name.endsWith("/base.apk"))) {
                apkBytes = outer.readBytes(); break
            }
        }
    }
    val targetBytes = apkBytes ?: fileBytes
    extractFromSigningBlock(targetBytes)?.let { signature = it.toBase64(); return true }
    certFromApkBytes(targetBytes)?.let { signature = it.toBase64(); return true }
    return false
}

private const val SOURCE_AUTO = "auto"
private const val SOURCE_SIMPLE = "simple"
private const val SOURCE_APK_FILE = "apkFile"
private const val SOURCE_INSTALLED = "installed"

val encodeCertificatePatch = rawResourcePatch(
    name = "Provide original app certificate",
    description = "Extracts and Base64-encodes the original app's signing certificate. " +
        "Applied automatically by 'Spoof signature verification'; you normally do not need " +
        "to touch it. Use 'Certificate source' to control where the certificate comes from.",
    default = false,
) {
    val certificateSource by stringOption(
        key = "certificateSource",
        default = SOURCE_AUTO,
        values = mapOf(
            "Automatic (recommended)" to SOURCE_AUTO,
            "Simple / decoded APK only" to SOURCE_SIMPLE,
            "Original APK file" to SOURCE_APK_FILE,
            "Installed app" to SOURCE_INSTALLED,
        ),
        title = "Certificate source",
        description = "Where to read the original signing certificate from.\n" +
            "• Automatic: reads the APK you're patching (v1 META-INF), then an 'Original APK file' " +
            "if set, then the installed app. Works for most apps with no extra input.\n" +
            "• Simple / decoded APK only: v1 META-INF of the APK being patched (same as the basic " +
            "extractor). Fails on v2/v3-only apps that have no v1 signature.\n" +
            "• Original APK file: reads the certificate (incl. v2/v3) from the APK you point to below.\n" +
            "• Installed app: reads it from the stock app installed on this device (on-device only).",
        required = true,
    ) { it in setOf(SOURCE_AUTO, SOURCE_SIMPLE, SOURCE_APK_FILE, SOURCE_INSTALLED) }

    val originalApkPath by stringOption(
        key = "originalApkPath",
        default = null,
        title = "Original APK file",
        description = "The original, unmodified APK (or .apks/.xapk bundle) to read the certificate " +
            "from. Select a file or paste the full file path. Required when 'Certificate source' is " +
            "'Original APK file'; also used as a fallback in 'Automatic'.",
        required = false,
    ) { path -> path.isNullOrBlank() || File(path.trim()).let { it.exists() && it.isFile } }

    execute {
        fun tryApkFile(): Boolean {
            val path = originalApkPath?.takeIf { it.isNotBlank() } ?: return false
            val file = File(path.trim())
            if (file.exists() && extractFromFile(file)) {
                log.info("Cert extracted from provided APK: ${file.name}")
                return true
            }
            log.warning("Could not extract a certificate from provided path: $path")
            return false
        }

        fun tryInstalledApp(): Boolean = try {
            val pkgName = runCatching { appPackageName }.getOrNull()?.takeIf { it.isNotBlank() }
                ?: Regex("""package="([^"]+)"""")
                    .find(get("AndroidManifest.xml").readText())?.groupValues?.get(1)
                ?: throw Exception("package name not found in AndroidManifest.xml")
            val ctx = Class.forName("android.app.ActivityThread")
                .getDeclaredMethod("currentApplication").apply { isAccessible = true }
                .invoke(null) ?: throw Exception("no current application context")
            val pm = ctx.javaClass.getMethod("getPackageManager").invoke(ctx)
                ?: throw Exception("no PackageManager")
            val pkgInfo = pm.javaClass
                .getMethod("getPackageInfo", String::class.java, Int::class.java)
                .invoke(pm, pkgName, 0) ?: throw Exception("no PackageInfo for $pkgName")
            val appInfo = try {
                pkgInfo.javaClass.getMethod("getApplicationInfo").invoke(pkgInfo)
            } catch (_: Exception) {
                pkgInfo.javaClass.getField("applicationInfo").get(pkgInfo)
            } ?: throw Exception("no ApplicationInfo")
            val sourceDir = (
                try {
                    appInfo.javaClass.getMethod("getSourceDir").invoke(appInfo)
                } catch (_: Exception) {
                    appInfo.javaClass.getField("sourceDir").get(appInfo)
                }
                ) as? String ?: throw Exception("no sourceDir")
            if (extractFromFile(File(sourceDir))) {
                log.info("Cert extracted from installed app: $sourceDir")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            log.info("Installed-app cert strategy unavailable: ${e.message}")
            false
        }

        fun trySimple(): Boolean {
            val metaInf = get("META-INF")
            val certFile = metaInf.listFiles()
                ?.firstOrNull { it.isFile && isCertEntry("META-INF/${it.name}") } ?: return false
            certFile.inputStream().use { parseFirstX509(it) }?.let {
                signature = it.toBase64()
                log.info("Cert extracted from decoded META-INF/${certFile.name}")
                return true
            }
            return false
        }

        val extracted = when (certificateSource) {
            SOURCE_SIMPLE -> trySimple()
            SOURCE_APK_FILE -> tryApkFile()
            SOURCE_INSTALLED -> tryInstalledApp()
            else -> trySimple() || tryApkFile() || tryInstalledApp()
        }

        if (!extracted) {
            log.warning(
                "No signing certificate could be extracted (source='$certificateSource'). " +
                    "Set 'Certificate source' to 'Original APK file' and pick the original APK, " +
                    "keep the stock app installed, or paste the Base64 signature in the " +
                    "'Spoof signature verification' patch options.",
            )
        }
    }
}
