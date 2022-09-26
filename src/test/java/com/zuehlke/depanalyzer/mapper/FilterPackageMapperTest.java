package com.zuehlke.depanalyzer.mapper;

import com.zuehlke.depanalyzer.model.*;
import com.zuehlke.depanalyzer.test.TestElement;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static com.zuehlke.depanalyzer.test.ElementTestHelper.assertEquals;

public class FilterPackageMapperTest {
    @Test
    public void empty() {
        TestElement original = TestElement.builder()
                .type(ElementType.GRAPH)
                .build();
        Element result = original.apply(FilterPackageMapper.prefix("testPackage"));
        assertEquals(original, result);
    }

    @Test
    public void singleMatching() {
        TestElement original = TestElement.builder()
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
        Element result = original.apply(FilterPackageMapper.prefix("testPackage"));
        assertEquals(original, result);
    }

    @Test
    public void nonMatching() {
        TestElement original = TestElement.builder()
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
                        .name("test2Package")
                        .fullName("test2Package")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("test2Package.testClass")
                                .details(new ClassDetails("test2Package.testClass"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        Element result = original.apply(FilterPackageMapper.prefix("testPackage"));
        TestElement expected = original.toBuilder()
                .clearChildren()
                .children(original.getChildren()
                        .filter(x -> x.getName().map("testPackage"::equals).orElse(false))
                        .map(TestElement::copy)
                        .collect(Collectors.toList()))
                .build();
        assertEquals(expected, result);
    }

    @Test
    public void dependency() {
        TestElement original = TestElement.builder()
                .child(TestElement.builder()
                        .name("testPackage")
                        .fullName("testPackage")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage.testClass")
                                .details(new ClassDetails("testPackage.testClass"))
                                .dependency(new Dependency("testPackage.testClass", "testPackage", new DependencyDetails("testPackage.testClass", "testPackage")))
                                .dependency(new Dependency("testPackage.testClass", "testPackage2", new DependencyDetails("testPackage.testClass", "testPackage2")))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .child(TestElement.builder()
                        .name("test2Package")
                        .fullName("test2Package")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("test2Package.testClass")
                                .details(new ClassDetails("test2Package.testClass"))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        Element result = original.apply(FilterPackageMapper.prefix("testPackage"));
        TestElement expected = TestElement.builder()
                .child(TestElement.builder()
                        .name("testPackage")
                        .fullName("testPackage")
                        .type(ElementType.PACKAGE)
                        .child(TestElement.builder()
                                .name("testClass")
                                .fullName("testPackage.testClass")
                                .details(new ClassDetails("testPackage.testClass"))
                                .dependency(new Dependency("testPackage.testClass", "testPackage", new DependencyDetails("testPackage.testClass", "testPackage")))
                                .type(ElementType.CLASS)
                                .build())
                        .build())
                .type(ElementType.GRAPH)
                .build();
        assertEquals(expected, result);
    }
}
