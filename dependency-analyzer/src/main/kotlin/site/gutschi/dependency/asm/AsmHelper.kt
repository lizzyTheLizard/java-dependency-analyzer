package site.gutschi.dependency.asm

class AsmHelper {
    companion object {
        fun getCanonicalName(asmName: String): String {
            return asmName.replace(Regex("/"), ".")
        }

        fun getSimpleName(asmName: String): String {
            return asmName.substringAfterLast("/")
        }
    }
}