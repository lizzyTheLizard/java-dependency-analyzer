package site.gutschi.dependency.maven;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Deprecated
public abstract class DependencyVisitorTest<T> extends LinkedList<T> implements Serializable {
    private String string = Integer.TYPE.toString();
    private final static Character c = CharSequence.class.getCanonicalName().charAt(0);

    public List getList(Double param) {
        Collection c = new HashSet();
        System.out.println(c.equals(Float.SIZE));
        return null;
    }
}
