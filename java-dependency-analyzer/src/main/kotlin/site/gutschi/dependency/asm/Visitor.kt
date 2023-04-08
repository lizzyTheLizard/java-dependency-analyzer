package site.gutschi.dependency.asm

import org.objectweb.asm.ClassReader

interface Visitor {
    @Throws(VisitorException::class)
    fun visit(classFile: ByteArray) {

    }

    @Throws(VisitorException::class)
    fun visit(classFile: ClassReader) {

    }
}