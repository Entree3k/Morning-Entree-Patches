package morningentree.morphe.patches.google.gboard.tracking

import java.io.File
import java.net.IDN
import java.net.URI

/**
 * Parses a hosts/blocklist and answers whether a given host is blocked. Ported from the adobo
 * patches. Supports "hosts file" lines (`0.0.0.0 example.com`), bare domains, and URLs.
 */
class HostsBlocker private constructor(
    private val blocklist: HashSet<String>,
) {
    fun isBlocked(
        host: String,
        wildcard: Boolean = true,
    ): Boolean {
        if (host.isBlank()) return false

        var normalizedHost = normalizeDomain(host)
            .let(::extractHost)
            ?.takeIf(::isHostValid)
            ?: return false

        if (blocklist.contains(normalizedHost)) {
            return true
        }
        if (!wildcard) return false

        while (normalizedHost.contains(DOT_CHAR)) {
            normalizedHost = normalizedHost.substringAfter(DOT_CHAR)
            if (blocklist.contains(normalizedHost)) {
                return true
            }
        }
        return false
    }

    fun close() {
        blocklist.clear()
    }

    private fun normalizeDomain(domain: String): String {
        val trimmedDomain = domain.trim().trimEnd('.')
        return runCatching { IDN.toASCII(trimmedDomain) }
            .getOrDefault(domain)
            .lowercase()
    }

    companion object {
        private const val COMMENT_CHAR = '#'
        private const val SPACE_CHAR = ' '
        private const val DOT_CHAR = '.'

        private const val MAX_DOMAIN_LENGTH = 253
        private const val MAX_PARTS = 127
        private const val MIN_PARTS = 2
        private const val MAX_PARTS_LENGTH = 63

        private val RESERVED_HOSTNAMES = setOf(
            "localhost",
            "localhost6",
            "localhost.localdomain",
            "localhost6.localdomain6",
            "local",
            "broadcasthost",
            "127.0.0.1",
            "0.0.0.0",
            "::1",
            "ip6-localhost",
            "ip6-loopback",
            "ip6-localnet",
            "ip6-mcastprefix",
            "ip6-allnodes",
            "ip6-allrouters",
            "ip6-allhosts",
        )

        fun fromString(input: String): HostsBlocker {
            val blocklist = hashSetOf<String>()
            val lines = input.lineSequence()
            parseLines(lines, blocklist)
            return HostsBlocker(blocklist)
        }

        fun fromFile(file: File): HostsBlocker {
            val blocklist = hashSetOf<String>()
            file.useLines { lines ->
                parseLines(lines, blocklist)
            }
            return HostsBlocker(blocklist)
        }

        fun extractHost(input: String): String? {
            val urlWithScheme = if (input.contains("://")) input else "http://$input"
            return runCatching { URI.create(urlWithScheme).host }.getOrNull()
        }

        private fun parseLines(
            lines: Sequence<String>,
            out: MutableSet<String>,
        ) {
            for (line in lines) {
                val trimmed = line.substringBefore(COMMENT_CHAR).trim()
                if (trimmed.isBlank()) continue

                val host =
                    extractHost(
                        trimmed
                            .substringAfter(SPACE_CHAR)
                            .trim(),
                    ) ?: continue

                if (host in RESERVED_HOSTNAMES) continue

                if (isHostValid(host)) {
                    out.add(host.lowercase())
                }
            }
        }

        private fun isHostValid(input: String): Boolean {
            if (input.isBlank() || input.length > MAX_DOMAIN_LENGTH) return false
            if (input.startsWith(DOT_CHAR) || input.endsWith(DOT_CHAR)) return false

            val parts = input.split(DOT_CHAR)
            if (parts.size !in MIN_PARTS..MAX_PARTS) return false

            for (part in parts) {
                if (part.length > MAX_PARTS_LENGTH) return false
            }
            return true
        }
    }
}
