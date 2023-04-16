package site.gutschi.dependency.asm

import java.io.File

class AsmException private constructor(message: String, e: Throwable?) : RuntimeException(message, e) {
    companion object {
        fun cannotReadFile(file: File, e: Exception): AsmException {
            return AsmException("Cannot read file ${file.absolutePath}: ${e.message}", e)
        }
    }
}