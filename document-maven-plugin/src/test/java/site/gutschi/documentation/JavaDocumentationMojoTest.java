package site.gutschi.documentation;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class JavaDocumentationMojoTest {
    @Test
    public void defaultInputs() throws Exception {
        String input = "src/test/resources/unit/document-maven-plugin-simple-tests/target/doc";
        String output = "src/test/resources/unit/document-maven-plugin-simple-tests/target/classes";
        FileUtils.deleteDirectory(new File(output));

        JavaDocumentationMojo myMojo = new JavaDocumentationMojo(input, output);
        myMojo.execute();

        Assertions.assertTrue(new File(output).exists() && new File(output).isDirectory());
    }
}