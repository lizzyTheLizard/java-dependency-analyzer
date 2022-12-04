package site.gutschi.documentation;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import site.gutschi.dependency.jdeps.JDepsRunner;
import site.gutschi.dependency.nomnoml.NomnomlWriter;

import java.io.File;
import java.util.Arrays;

@SuppressWarnings("unused")
@Mojo(name = "create-documentation", defaultPhase = LifecyclePhase.COMPILE)
public class JavaDocumentationMojo extends AbstractMojo {
    @Parameter(property = "input", defaultValue = "target/classes")
    String input;

    @Parameter(property = "outputFolder", defaultValue = "target/doc")
    String outputFolder;

    public JavaDocumentationMojo(){

    }

    JavaDocumentationMojo(String input, String outputFolder){
        this.input = input;
        this.outputFolder = outputFolder;
    }

    @Override
    public void execute() throws MojoExecutionException {
        try {
            File file = new File(outputFolder);
            getLog().debug("Try to create documentation at '" + file.getAbsolutePath() + "'");
            File[] inputs = Arrays.stream(input.split(",")).map(File::new).toArray(File[]::new);
            for (File s : inputs) {
                getLog().debug("Use input '" + s.getAbsolutePath() + "'");
            }

            String jdeps = JDepsRunner.create().file(inputs).run();
            NomnomlWriter.create().outputDir(file).jdepsOutput(jdeps).write();
            getLog().info("Created documentation at '" + file.getAbsolutePath() + "'");
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
