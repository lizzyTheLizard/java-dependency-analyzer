package site.gutschi.dependency.asm

import java.io.File

class ClassFileFinderException private constructor(message: String, e: Throwable?) : RuntimeException(message, e) {
    companion object {
        fun cannotReadFile(file: File, e: Exception): ClassFileFinderException {
            return ClassFileFinderException("Cannot read file ${file.absolutePath}: ${e.message}", e)
        }
    }
}