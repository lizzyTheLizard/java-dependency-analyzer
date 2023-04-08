package site.gutschi.dependency

import java.io.File

class TestFileHelpers {
    companion object {
        fun getFile(path: String): File {
            val url = object {}.javaClass.getResource("/$path")!!
            return File(url.toURI())
        }

        fun getFileContent(path: String): String {
            val file = getFile(path)
            //Make sure to use proper line separator and ignore newlines from file
            return file.readLines().joinToString(System.lineSeparator())
        }
    }
}