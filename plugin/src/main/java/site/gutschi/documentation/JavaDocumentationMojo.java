package site.gutschi.documentation;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import site.gutschi.documentation.jdeps.JDepsRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mojo(name = "create-documentation", defaultPhase = LifecyclePhase.COMPILE)
public class JavaDocumentationMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "input", readonly = false, defaultValue = "target/classes")
    String input;

    @Parameter(property = "outputFolder", readonly = false, defaultValue = "target/doc")
    String output;

    @Override
    public void execute() throws MojoExecutionException {
        String jdeps = JDepsRunner.create().file(input.split(",")).run();
        String template = getTemplate();
        String mergedIndex = merge(template, jdeps);
        writeIndex(mergedIndex);
    }

    private String getTemplate() throws MojoExecutionException {
        try {
            URL url = getClass().getResource("/index.html");
            if(url == null) {
                throw new MojoExecutionException("Could not read input template from classpath, url is null");
            }
            return IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e){
            throw new MojoExecutionException("Could not read input template from classpath", e);
        }
    }

    private String merge(String template, String jdeps) throws MojoExecutionException {
        int indexWithinVariable = template.indexOf("classes -> java.base");
        System.out.println(indexWithinVariable);
        int indexLeadingQute = template.substring(0, indexWithinVariable).lastIndexOf('`');
        System.out.println(indexLeadingQute);
        int indexTrailingQute = template.indexOf('`', indexWithinVariable);
        System.out.println(indexTrailingQute);
        return template.substring(0, indexLeadingQute+2) + jdeps + template.substring(indexTrailingQute);
    }

    private void writeIndex(String template) throws MojoExecutionException {
        File f = new File(output);
        if (f.mkdir()) {
            System.out.println("Directory " + f.getAbsolutePath() + " is created");
        } else {
            throw new MojoExecutionException("Could not create output dir" + output);
        }
        Path path = Paths.get(output, "index.html");
        try {
            Files.writeString(path, template);
        } catch (IOException e){
            throw new MojoExecutionException("Could not write output " + path.toAbsolutePath(), e);
        }
    }

}
