package morningentree.morphe.util

import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.util.proxy.mutableTypes.MutableField
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.Instruction

/**
 * Generic field/method string-transformation pass over the whole app (or a single [classDef]).
 *
 * Ported from the adobo patches. [methodFilter] returns a non-null key for each instruction that
 * should be transformed; [methodTransform] then rewrites it. Field initial values are handled by
 * [fieldFilter]/[fieldTransform].
 */
fun <T> BytecodePatchContext.transformationPatch(
    classDef: ClassDef? = null,
    fieldFilter: (ClassDef, Field) -> Boolean = { _, _ -> false },
    fieldTransform: (MutableField, ClassDef) -> Unit = { _, _ -> },
    methodFilter: (ClassDef, Method, Instruction, Int) -> T?,
    methodTransform: (MutableMethod, T) -> Unit,
) {
    fun findPatchIndices(classDef: ClassDef, method: Method): Sequence<T>? =
        method.implementation
            ?.instructions
            ?.asSequence()
            ?.withIndex()
            ?.mapNotNull { (index, instruction) ->
                methodFilter(classDef, method, instruction, index)
            }

    fun ClassDef.filterMethods(): List<Method> = buildList {
        methods.forEach { method ->
            val patchIndices = findPatchIndices(
                classDef = this@filterMethods,
                method = method,
            )
            if (patchIndices?.any() == true) {
                add(method)
            }
        }
    }

    fun ClassDef.filterFields(): List<Field> = buildList {
        fields.forEach { field ->
            if (fieldFilter(this@filterFields, field)) {
                add(field)
            }
        }
    }

    buildMap {
        if (classDef != null) {
            val methods = classDef.filterMethods()
            val fields = classDef.filterFields()

            if (methods.isNotEmpty() || fields.isNotEmpty()) {
                put(classDef, methods to fields)
            }
            return@buildMap
        }

        classDefForEach { classDef ->
            val methods = classDef.filterMethods()
            val fields = classDef.filterFields()

            if (methods.isNotEmpty() || fields.isNotEmpty()) {
                put(classDef, methods to fields)
            }
        }
    }.forEach { (classDef, pair) ->
        val mutableClass = mutableClassDefBy(classDef)
        val (methods, fields) = pair

        methods.forEach methods@{ method ->
            val mutableMethod = mutableClass.findMutableMethodOf(method)
            val patchIndices = findPatchIndices(mutableClass, mutableMethod)
                ?.toCollection(ArrayDeque())
                ?: return@methods

            while (!patchIndices.isEmpty()) {
                methodTransform(mutableMethod, patchIndices.removeLast())
            }
        }

        fields.forEach { field ->
            val mutableField = mutableClass.findMutableFieldOf(field)
            fieldTransform(mutableField, classDef)
        }
    }
}
