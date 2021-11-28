package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

/**
 * Immutable shell for encapsulating any {@link Context} instance.
 * All non-mutable methods are delegated to encapsulated implementation.
 *
 * @author nnolab
 */
public class FullImmutableContextShell implements ImmutableContext, Serializable {

    private static final long serialVersionUID = 5524346548429797478L;

    private final Context encapsulated;

    /**
     * Construct shell with encapsulated instance.
     *
     * @param encapsulated encapsulated instance
     * @throws NullPointerException if {@code encapsulated} is null
     */
    public FullImmutableContextShell(Context encapsulated) {
        this.encapsulated = Objects.requireNonNull(encapsulated);
    }

    /**
     * Check given object != this.
     *
     * @param obj object to check
     * @throws IllegalArgumentException if given object == this
     */
    private void checkNotThis(Object obj) {
        if (obj == this) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getName() + ": {" + encapsulated.toString() + "}";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String key) {
        return encapsulated.get(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(String key, Class<T> valueType) {
        return encapsulated.get(key, valueType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOrDefault(String key, Object defaultValue) {
        return encapsulated.getOrDefault(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getOrDefault(String key, Class<T> valueType, T defaultValue) {
        return encapsulated.getOrDefault(key, valueType, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOrCompute(String key, Function<String, Object> function) {
        return encapsulated.getOrCompute(key, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getOrCompute(String key, Class<T> valueType, Function<String, T> function) {
        return encapsulated.getOrCompute(key, valueType, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(String key) {
        return encapsulated.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        return encapsulated.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return encapsulated.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return encapsulated.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return encapsulated.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Keys keys() {
        return encapsulated.keys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Values values() {
        return encapsulated.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entries entries() {
        return encapsulated.entries();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copy() {
        return new FullImmutableContextShell(encapsulated.copy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copy(BiPredicate<String, Object> criteria) {
        return new FullImmutableContextShell(encapsulated.copy(criteria));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Context acceptor) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Context acceptor, ReplaceRule replaceRule) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Context acceptor, BiPredicate<String, Object> criteria) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Context acceptor, ReplaceRule replaceRule,
                                            BiPredicate<String, Object> criteria) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Map<String, Object> acceptor) {
        encapsulated.copyTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Map<String, Object> acceptor, boolean replace) {
        encapsulated.copyTo(acceptor, replace);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Map<String, Object> acceptor, BiPredicate<String, Object> criteria) {
        encapsulated.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell copyTo(Map<String, Object> acceptor, boolean replace,
                                            BiPredicate<String, Object> criteria) {
        encapsulated.copyTo(acceptor, replace, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell forEach(BiConsumer<String, Object> action) {
        encapsulated.forEach(action);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FullImmutableContextShell forEach(BiPredicate<String, Object> criteria,
                                             BiConsumer<String, Object> action) {
        encapsulated.forEach(criteria, action);
        return this;
    }
}
