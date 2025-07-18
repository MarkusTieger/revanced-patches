package app.revanced.patches.cricbuzz.ads

import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val disableAdsPatch = bytecodePatch (
    name = "Hide ads",
) {
    compatibleWith("com.cricbuzz.android"("6.23.02"))

    execute {
        userStateSwitchFingerprint.method.apply {
            val opcodeIndex = indexOfFirstInstructionOrThrow(Opcode.MOVE_RESULT_OBJECT)
            val register = getInstruction<OneRegisterInstruction>(opcodeIndex).registerA

            addInstruction(
                opcodeIndex + 1,
                "const-string v$register, \"ACTIVE\""
            )
        }
    }
}
