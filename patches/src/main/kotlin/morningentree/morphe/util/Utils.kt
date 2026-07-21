package morningentree.morphe.util

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod


/**
 * Convert a string representing a pattern of hexadecimal bytes to a byte array.
 *
 * @return The byte array representing the pattern.
 * @throws PatchException If the pattern is invalid.
 */
fun byteArrayOf(pattern: String): ByteArray {
    val stripped = pattern.replace(Regex("\\s"), "")
    check(stripped.length % 2 == 0) { "Must have an even length" }

    try {
    return stripped
        .chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
    } catch (e: NumberFormatException) {
        throw PatchException(
            "Could not parse pattern: $pattern. A pattern is a sequence of case insensitive strings " +
                    "representing hexadecimal bytes separated by spaces",
            e,
        )
    }
}

fun ByteArray.toInt(littleEndian: Boolean = false): Int {
    if (size != 4)
        throw IllegalArgumentException("ByteArray must be exactly 4 bytes long")

    val bytes = if (littleEndian) reversedArray() else this

    return (bytes[0].toInt() and 0xFF) shl 24 or
            (bytes[1].toInt() and 0xFF) shl 16 or
            (bytes[2].toInt() and 0xFF) shl 8 or
            (bytes[3].toInt() and 0xFF)
}

fun asCString(str: String, padTo: Int? = null): ByteArray {
    return str.toByteArray(Charsets.US_ASCII) + byteArrayOf(0)
}

/**
 * Overwrite the method body to immediately return a boolean value.
 * Only valid for methods with a boolean (`Z`) return type.
 */
fun MutableMethod.returnEarly(value: Boolean = false) {
    check(returnType == "Z") { "returnEarly(Boolean) requires a Z return type, was $returnType" }

    addInstructions(
        0,
        """
            const/4 v0, ${if (value) "0x1" else "0x0"}
            return v0
        """.trimIndent()
    )
}