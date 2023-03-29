package site.gutschi.dependency.jdeps

typealias JDepsResult = List<JDepsResultLine>

data class JDepsResultLine(val from: String, val to: String, val module: String?) {
    companion object {
        fun fromLine(line: String) : JDepsResultLine {
            val arrowSplit = line.split("->")
            if (arrowSplit.size != 2) {
                throw JDepsException.invalidOutputLine(line)
            }
            val from = arrowSplit[0].trim()
            val remaining = arrowSplit[1].trim()
            val firstSpace = remaining.indexOf(" ")
            val to = remaining.substring(0, firstSpace).trim()
            val module = remaining.substring(firstSpace).trim()
            if(module.isEmpty() || module == "not found") {
                return JDepsResultLine(from, to, null)
            }
            return JDepsResultLine(from, to, module)
        }
    }
}
