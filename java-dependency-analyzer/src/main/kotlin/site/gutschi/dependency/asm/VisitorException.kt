package site.gutschi.dependency.asm

class VisitorException private constructor(message: String, e: Throwable?) : RuntimeException(message, e)