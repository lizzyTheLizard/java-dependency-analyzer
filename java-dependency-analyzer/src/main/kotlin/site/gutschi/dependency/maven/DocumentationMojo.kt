package site.gutschi.dependency.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import site.gutschi.dependency.Level
import site.gutschi.dependency.Main
import site.gutschi.dependency.Properties
import java.io.File

@Mojo(name = "create-documentation", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
class DocumentationMojo : AbstractMojo() {
    @Parameter(property = "input", defaultValue = "target/classes")
    var input: String = "target/classes"

    @Parameter(property = "fatJarMatcher", defaultValue = "")
    var fatJarMatcher: String = ""

    @Parameter(property = "includeFatJarBoot", defaultValue = "false")
    var includeFatJarBoot: Boolean = false

    @Parameter(property = "outputFolder", defaultValue = "target/doc")
    var outputFolder: String = "target/doc"

    @Throws(MojoExecutionException::class)
    override fun execute() {
        try {
            val properties = Properties(
                log = { m: String, l: Level -> log(m, l) },
                outputFolder = File(outputFolder),
                inputs = input.split(",").map { File(it) },
                fatJarMatchers = fatJarMatcher.split(",").map { Regex(it) },
                includeFatJarClasses = includeFatJarBoot
            )
            Main(properties).execute()
        } catch (e: Exception) {
            throw MojoExecutionException(e.message, e)
        }
    }

    private fun log(m: String, l: Level) {
        when (l) {
            Level.DEBUG -> log.debug(m)
            Level.INFO -> log.info(m)
            Level.WARN -> log.warn(m)
        }
    }
}