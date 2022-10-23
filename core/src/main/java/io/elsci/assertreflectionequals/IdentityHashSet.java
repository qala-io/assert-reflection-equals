package io.elsci.assertreflectionequals;

import java.util.*;

/**
 * {@link IdentityHashMap}-backed implementation of the {@link Set} interface.
 * This means that whether an object is an element of the set depends on whether it is == (rather than equals()) to an element of the set.
 * Each element in the set is a key in the backing IdentityHashMap.
 */
public class IdentityHashSet<E> extends AbstractSet<E> {

    private final transient IdentityHashMap<E,Boolean> map;

    /**
     * Construct a new, empty IdentityHashSet whose backing IdentityHashMap has the default expected maximum size (21)
     */
    public IdentityHashSet(E ... initial) {
        map = new IdentityHashMap<>();
        this.addAll(Arrays.asList(initial));
    }

    /**
     * Adds the specified element to this set if it is not already present.
     */
    @Override
    public boolean add(E o) {
        return map.put(o, Boolean.FALSE) == null;
    }

    /**
     * Removes all of the elements from this set
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Returns <code>true</code> if this set contains the specified element.
     */
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * Returns <code>true</code> if this set contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this set. The elements are returned in no particular order.
     */
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * Removes the specified element from this set if it is present.
     */
    @Override
    public boolean remove(Object o) {
        return (map.remove(o) != null);
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     */
    @Override
    public int size() {
        return map.size();
    }

}
