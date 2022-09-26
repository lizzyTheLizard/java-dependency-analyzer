package com.zuehlke.depanalyzer.graph;

import com.zuehlke.depanalyzer.model.ClassDetails;
import com.zuehlke.depanalyzer.model.Dependency;
import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.ElementType;
import com.zuehlke.depanalyzer.test.TestElement;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.zuehlke.depanalyzer.test.ElementTestHelper.assertEquals;

public class GraphTest {
    @Test
    public void empty() {
        Element graph = Graph.create(Set.of(), Set.of());
        Element expected = TestElement.builder()
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, graph);
    }

    @Test
    public void singleClass() {
        Element graph = Graph.create(Set.of(), Set.of(new ClassDetails("testClass")));
        Element expected = TestElement.builder()
                .child(TestElement.builder()
                        .name("testClass")
                        .fullName("testClass")
                        .details(new ClassDetails("testClass"))
                        .type(ElementType.CLASS)
                        .build())
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, graph);
    }

    @Test
    public void singlePackage() {
        Element graph = Graph.create(Set.of(), Set.of(new ClassDetails("testPackage.testClass")));
        Element expected = TestElement.builder()
                .child(TestElement.builder()
                        .name("testPackage")
                        .fullName("testPackage")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage.testClass")
                                .details(new ClassDetails("testPackage.testClass"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, graph);
    }

    @Test
    public void subPackage() {
        Element graph = Graph.create(Set.of(), Set.of(new ClassDetails("testPackage.subPackage.testClass")));
        Element expected = TestElement.builder()
                .child(TestElement.builder()
                        .name("testPackage")
                        .fullName("testPackage")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("subPackage")
                                .fullName("testPackage.subPackage")
                                .type(ElementType.PACKAGE)
                                .child(TestElement.builder()
                                        .name("testClass")
                                        .fullName("testPackage.subPackage.testClass")
                                        .details(new ClassDetails("testPackage.subPackage.testClass"))
                                        .type(ElementType.CLASS)
                                        .build())
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, graph);
    }

    @Test
    public void multiPackage() {
        Element graph = Graph.create(Set.of(), Set.of(new ClassDetails("testPackage.testClass"), new ClassDetails("testPackage2.testClass")));
        Element expected = TestElement.builder()
                .child(TestElement.builder()
                        .name("testPackage")
                        .fullName("testPackage")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage.testClass")
                                .details(new ClassDetails("testPackage.testClass"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .child(TestElement.builder()
                        .name("testPackage2")
                        .fullName("testPackage2")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage2.testClass")
                                .details(new ClassDetails("testPackage2.testClass"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, graph);
    }

    @Test
    public void dependencyWithin() {
        Element graph = Graph.create(Set.of(Dependency.create("testPackage.testClass", "testPackage.testClass2")), Set.of(new ClassDetails("testPackage.testClass"), new ClassDetails("testPackage.testClass2")));
        Element expected = TestElement.builder()
                .child(TestElement.builder()
                        .name("testPackage")
                        .fullName("testPackage")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage.testClass")
                                .details(new ClassDetails("testPackage.testClass"))
                                .dependency(Dependency.create("testPackage.testClass", "testPackage.testClass2"))
                                .type(ElementType.CLASS)
                                .build())
                        .child(TestElement.builder()
                                .name("testClass2")
                                .fullName("testPackage.testClass2")
                                .details(new ClassDetails("testPackage.testClass2"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, graph);
    }

    @Test
    public void dependencyOverPackages() {
        Element graph = Graph.create(Set.of(Dependency.create("testPackage.testClass", "testPackage2.testClass")), Set.of(new ClassDetails("testPackage.testClass"), new ClassDetails("testPackage2.testClass")));
        Element expected = TestElement.builder()
                .child(TestElement.builder()
                        .name("testPackage")
                        .fullName("testPackage")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage.testClass")
                                .details(new ClassDetails("testPackage.testClass"))
                                .dependency(Dependency.create("testPackage.testClass", "testPackage2.testClass"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .child(TestElement.builder()
                        .name("testPackage2")
                        .fullName("testPackage2")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage2.testClass")
                                .details(new ClassDetails("testPackage2.testClass"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, graph);
    }
}