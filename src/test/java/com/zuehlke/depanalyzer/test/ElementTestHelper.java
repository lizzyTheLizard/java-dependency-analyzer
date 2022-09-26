package com.zuehlke.depanalyzer.test;

import com.zuehlke.depanalyzer.model.Dependency;
import com.zuehlke.depanalyzer.model.Element;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

@UtilityClass
public class ElementTestHelper {
    public static void assertEquals(Element e1, Element e2) {
        Assertions.assertEquals(
                e1.getName(),
                e2.getName(),
                format("Name differs between %s and %s", e1, e2));
        Assertions.assertEquals(
                e1.getFullName(),
                e2.getFullName(),
                format("Fullname differs between %s and %s", e1, e2));
        Assertions.assertEquals(
                e1.getType(),
                e2.getType(),
                format("Type differs between %s and %s", e1, e2));
        Assertions.assertEquals(
                e1.getDetails(),
                e2.getDetails(),
                format("Details differs between %s and %s", e1, e2));
        Assertions.assertEquals(
                e1.getParent().map(Element::getName),
                e2.getParent().map(Element::getName),
                format("Name differs between %s and %s", e1, e2));

        Dependency[] dependencies1 = e1.getDependencies()
                .sorted(ElementTestHelper::compare)
                .toArray(Dependency[]::new);
        Dependency[] dependencies2 = e2.getDependencies()
                .sorted(ElementTestHelper::compare)
                .toArray(Dependency[]::new);
        Assertions.assertArrayEquals(dependencies1, dependencies2);

        Element[] children1 = e1.getChildren()
                .sorted(ElementTestHelper::compare)
                .toArray(Element[]::new);
        Element[] children2 = e2.getChildren()
                .sorted(ElementTestHelper::compare)
                .toArray(Element[]::new);
        Assertions.assertEquals(children1.length, children2.length,
                format("Number of children differs between %s and %s", e1, e2));
        for (int i = 0; i < children1.length; i++) {
            assertEquals(children1[i], children2[i]);
        }
    }

    private String format(String message, Element e1, Element e2) {
        return String.format(
                message,
                e1.getFullName().orElse(null),
                e2.getFullName().orElse(null)
        );
    }

    private int compare(Element e1, Element e2) {
        return e1.getName().orElse("").compareTo(e2.getName().orElse(""));
    }

    private int compare(Dependency d1, Dependency d2) {
        int firstCompare = d1.getFrom().compareTo(d2.getFrom());
        if (firstCompare != 0) {
            return firstCompare;
        }
        return d1.getTo().compareTo(d2.getTo());
    }

}
