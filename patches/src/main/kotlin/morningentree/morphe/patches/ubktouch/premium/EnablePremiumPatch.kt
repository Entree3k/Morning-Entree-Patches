package morningentree.morphe.patches.ubktouch.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import morningentree.morphe.patches.ubktouch.shared.Constants
import morningentree.morphe.util.getReference

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks UbikiTouch Premium. Must be installed with either ADB/InstallerX Revived/Inure so it is not in Restricted Mode",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val unlocked = MainPrefUnlockedFingerprint.method

        val licenseRef = unlocked.instructions
            .mapNotNull { it.getReference<MethodReference>() }
            .firstOrNull { it.returnType == "Z" && it.parameterTypes.isEmpty() }

        if (licenseRef != null) {
            classDefForEach { classDef ->
                if (classDef.type != licenseRef.definingClass) return@classDefForEach
                mutableClassDefBy(classDef).methods
                    .firstOrNull {
                        it.name == licenseRef.name &&
                            it.parameterTypes.isEmpty() &&
                            it.returnType == "Z"
                    }
                    ?.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
            }
        }

        unlocked.addInstructions(
            0,
            """
                sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                return-object v0
            """,
        )
    }
}
