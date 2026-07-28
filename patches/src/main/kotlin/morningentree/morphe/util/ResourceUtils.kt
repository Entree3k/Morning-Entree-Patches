package morningentree.morphe.util

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList

/**
 * Returns the first element in the [Document] with the specified tag name.
 */
operator fun Document.get(tagName: String): Element =
    getElementsByTagName(tagName).item(0) as Element

/**
 * Filters the child nodes to a sequence of only those that are [Element]s.
 */
fun NodeList.asElementSequence(): Sequence<Element> =
    (0 until this.length)
        .asSequence()
        .mapNotNull { this.item(it) as? Element }

/**
 * Returns the value of the specified attribute of the [Element].
 */
operator fun Element.get(attrName: String): String = getAttribute(attrName)

/**
 * Sets the value of the specified attribute of the [Element].
 */
operator fun Element.set(
    attrName: String,
    attrValue: String,
): Unit = setAttribute(attrName, attrValue)
