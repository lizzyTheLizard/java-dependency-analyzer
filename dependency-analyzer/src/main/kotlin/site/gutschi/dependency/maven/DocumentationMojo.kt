package site.gutschi.dependency.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import site.gutschi.dependency.Main
import site.gutschi.dependency.Properties
import site.gutschi.dependency.Properties.Level
import java.io.File


@Mojo(name = "create-documentation", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
class DocumentationMojo : AbstractMojo() {
    @Parameter(property = "name", readonly = true, required = true, defaultValue = "\${project.name}")
    var name: String = ""

    @Parameter(property = "version", readonly = true, required = true, defaultValue = "\${project.version}")
    var version: String = ""

    @Parameter(property = "inputs", readonly = true, required = true,  defaultValue = "target/classes")
    var inputs: List<String> = listOf("target/classes")

    @Parameter(property = "fatJarMatchers", readonly = true)
    var fatJarMatchers: List<String> = listOf()

    @Parameter(property = "includeFatJarClasses", readonly = true, defaultValue = "false")
    var includeFatJarClasses: Boolean = false

    @Parameter(property = "outputFolder", readonly = true, defaultValue = "target/doc")
    var outputFolder: String = "target/doc"

    @Parameter(property = "basePackage", readonly = true)
    var basePackage: String? = null

    @Parameter(property = "collapsePackages", readonly = true)
    var collapsePackages: List<String> = listOf()

    @Parameter(property = "ignoredPackages", readonly = true)
    var ignoredPackages: List<String> = listOf()

    @Parameter(property = "splitPackages", readonly = true)
    var splitPackages: List<String> = listOf()

    @Throws(MojoExecutionException::class)
    override fun execute() {
        try {
            val properties = Properties(
                name = name,
                version = version,
                log = { m: String, l: Level -> log(m, l) },
                outputFolder = File(outputFolder),
                inputs = inputs.map { File(it) },
                fatJarMatchers = fatJarMatchers.map { Regex(it) },
                includeFatJarClasses = includeFatJarClasses,
                basePackage = basePackage,
                collapsePackages = collapsePackages,
                ignoredPackages = ignoredPackages,
                splitPackages = splitPackages,
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