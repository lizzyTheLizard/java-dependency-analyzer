package site.gutschi.dependency.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import site.gutschi.dependency.jdeps.JDepsRunner
import site.gutschi.dependency.write.writeFile
import java.io.File
import java.util.*

@Mojo(name = "create-documentation", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
class DocumentationMojo : AbstractMojo() {
    @Parameter(property = "input", defaultValue = "target/classes")
    var input: String = "target/classes"

    @Parameter(property = "outputFolder", defaultValue = "target/doc")
    var outputFolder: String = "target/doc"

    @Parameter(property = "multiRelease")
    var multiRelease: String? = null

    @Parameter(property = "fatJarRegex")
    var fatJarRegex: String? = null

    @Throws(MojoExecutionException::class)
    override fun execute() {
        try {
            val jDepsRunner = JDepsRunner()
            multiRelease?.let { jDepsRunner.multiRelease(it) }
            fatJarRegex?.let { jDepsRunner.fatJarMatcher { x -> x.name.matches(it.toRegex()) } }
            input.split(",").stream()
                .map { x -> File(x) }
                .peek { x -> log.debug("Use input '${x.absolutePath}'") }
                .forEach { x -> jDepsRunner.file(x) }
            val jDepsResult = jDepsRunner.run()

            val file = File(outputFolder)
            writeFile(file, jDepsResult)
            log.info("Created documentation at '" + file.absolutePath + "'")
        } catch (e: Exception) {
            throw MojoExecutionException(e.message, e)
        }
    }
}