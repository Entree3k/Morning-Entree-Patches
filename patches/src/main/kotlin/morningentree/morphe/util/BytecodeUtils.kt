package morningentree.morphe.util

import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.Reference
import com.android.tools.smali.dexlib2.iface.value.EncodedValue
import com.android.tools.smali.dexlib2.util.MethodUtil

/**
 * Finds and returns the first field in this class matching the given [field] by name and type.
 */
fun MutableClass.findMutableFieldOf(field: Field) = this.fields.first {
    it.name == field.name && it.type == field.type
}

/**
 * Finds and returns the first method in this class whose signature matches the given [method].
 */
fun MutableClass.findMutableMethodOf(method: Method) = this.methods.first {
    MethodUtil.methodSignaturesMatch(it, method)
}

/**
 * Returns the [Reference] as [T], or null if the instruction is not a
 * [ReferenceInstruction] or the reference is not of type [T].
 */
inline fun <reified T : Reference> Instruction.getReference() =
    (this as? ReferenceInstruction)?.reference as? T

/**
 * Returns the field's initial encoded value as [T], or null if it is not of type [T].
 */
inline fun <reified T : EncodedValue> Field.getEncodedValue() =
    this.initialValue as? T
