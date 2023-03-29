package site.gutschi.dependency.write

import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Path


class WriteFileException private constructor(message: String, e: Throwable?) : RuntimeException(message, e) {
    companion object {
        fun couldNotParseUrl(path: String): WriteFileException {
            return WriteFileException("Could not parse URL '$path'", null)
        }

        fun couldNotReadTemplate(url: URL, e: Throwable?): WriteFileException {
            return WriteFileException("Could not read template '$url.path'", e)
        }

        fun couldNotCreateOutputDir(f: File): WriteFileException {
            return WriteFileException("Could not create output dir '$f.absolutePath'", null)
        }

        fun couldNotWrite(path: Path, e: IOException?): WriteFileException {
            return WriteFileException("Could not write to output file '$path.toAbsolutePath()'", e)
        }
    }
}