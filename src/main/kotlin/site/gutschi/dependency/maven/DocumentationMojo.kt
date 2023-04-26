package site.gutschi.dependency.maven

import org.apache.maven.artifact.Artifact
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.plugins.annotations.ResolutionScope
import org.apache.maven.project.MavenProject
import site.gutschi.dependency.Main
import site.gutschi.dependency.Properties
import site.gutschi.dependency.Properties.Level
import site.gutschi.dependency.frameworks.JavaBase
import site.gutschi.dependency.frameworks.Spring
import java.io.File

@Mojo(name = "create-documentation", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
class DocumentationMojo : AbstractMojo() {
    @Parameter(property = "version", required = true, readonly=true, defaultValue = "\${project}")
    var project: MavenProject = MavenProject()

    @Parameter(property = "inputs", required = true, defaultValue = "\${project.build.outputDirectory}")
    var inputs: List<String> = listOf("target/classes")

    @Parameter(property = "dependencyMatchers")
    var dependencyMatchers: List<String> = listOf()

    @Parameter(property = "outputFolder", defaultValue = "\${project.build.directory}/doc")
    var outputFolder: String = "target/doc"

    @Parameter(property = "basePackage")
    var basePackage: String? = null

    @Parameter(property = "collapsePackages")
    var collapsePackages: List<String> = listOf()

    @Parameter(property = "ignoredPackages")
    var ignoredPackages: List<String> = listOf()

    @Parameter(property = "splitPackages")
    var splitPackages: List<String> = listOf()

    @Throws(MojoExecutionException::class)
    override fun execute() {
        try {
            val properties = Properties(
                name = project.name,
                version = project.version,
                log = { m: String, l: Level -> log(m, l) },
                outputFolder = File(outputFolder),
                inputs = getInputs(),
                includeFatJarClasses = false,
                basePackage = basePackage,
                collapsePackages = collapsePackages,
                ignoredPackages = ignoredPackages,
                splitPackages = splitPackages,
                attributeCollectors = listOf(JavaBase(), Spring())
            )
            Main(properties).execute()
        } catch (e: Exception) {
            throw MojoExecutionException(e.message, e)
        }
    }

    private fun getInputs(): Collection<File> {
        val regexDependencyMatcher = dependencyMatchers.map { Regex(it) }
        val filesFromDependencies = project.artifacts
            .filter { a -> regexDependencyMatcher.any { it.matches(a.fullName) }}
            .map { it.file }
        val filesFromInputs = inputs.map { File(it) }
        println("Input files ${filesFromInputs.map { it.absolutePath }}, DependencyFiles: ${filesFromDependencies.map { it.absolutePath }}")
        println("Dependencies: ${project.artifacts.map { it.fullName } }}")
        return filesFromInputs.union(filesFromDependencies)
    }

    private fun log(m: String, l: Level) {
        when (l) {
            Level.DEBUG -> log.debug(m)
            Level.INFO -> log.info(m)
            Level.WARN -> log.warn(m)
        }
    }
}

private val Artifact.fullName: CharSequence
    get() { return listOf(groupId, artifactId, version).joinToString(":")}
