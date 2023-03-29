package site.gutschi.dependency.jdeps

import java.io.File

class JDepsException private constructor(message: String, e: Throwable?) : RuntimeException(message, e) {
    companion object {
        fun targetFileDoesNotExist(targetFile: File): JDepsException {
            return JDepsException("File ${targetFile.absolutePath} does not exist", null)
        }

        fun targetFileNotReadable(targetFile: File): JDepsException {
            return JDepsException("File ${targetFile.absolutePath} is not readable", null)
        }

        fun wrap(e: Exception): JDepsException {
            return if (e is JDepsException) e else JDepsException("Error while running jDeps: ${e.message}", e)
        }

        fun cannotUnzip(cause: Throwable): JDepsException {
            return JDepsException("Could not unzip fat jar: ${cause.message}", cause)
        }

        fun matcherAfterFile(): Throwable {
            return JDepsException("Cannot change fatJarMatcher after files have been added", null)
        }

        fun invalidOutputLine(line: String): Throwable {
            return JDepsException("Invalid output line $line cannot be parsed", null)
        }

        fun nonZeroReturn(result: Int, output: String): Throwable {
            return JDepsException("JDeps returned $result: $output", null)

        }

        fun jdepsNotFound(): Throwable {
            return JDepsException("JDeps not a registered tool, please use a regular JVM", null)
        }

    }
}