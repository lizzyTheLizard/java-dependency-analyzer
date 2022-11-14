package site.gutschi.documentation;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class JavaDocumentationMojoTest extends AbstractMojoTestCase {
    public void testSomething() throws Exception {
        File target = new File("target/doc");
        FileUtils.deleteDirectory(target);

        File pom = getTestFile("src/test/resources/unit/document-maven-plugin-simple-tests/pom.xml");
        JavaDocumentationMojo myMojo = (JavaDocumentationMojo) lookupMojo("create-documentation", pom);
        myMojo.execute();

        Assert.assertTrue("Target folder has been created", target.exists() && target.isDirectory());
    }
}