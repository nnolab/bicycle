package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.*;

/**
 * Shell for encapsulating any {@link Context} instance.
 * Only abstract methods, {@link #equals(Object)} and {@link #toString()}
 * are delegated to encapsulated implementation.
 * Others are implemented by default.
 * Not thread-safe.
 *
 * @author nnolab
 */
public class BaseContextShell implements Context, Serializable {

    private static final long serialVersionUID = -7944763756383553396L;

    private final Context encapsulated;

    /**
     * Construct shell with encapsulated instance.
     *
     * @param encapsulated encapsulated instance
     * @throws NullPointerException if {@code encapsulated} is null
     */
    public BaseContextShell(Context encapsulated) {
        this.encapsulated = Objects.requireNonNull(encapsulated);
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
    public Object put(String key, Object value) {
        return encapsulated.put(key, value);
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
    public Object putIfAbsent(String key, Object value) {
        return encapsulated.putIfAbsent(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object putIfKeyAbsent(String key, Object value) {
        return encapsulated.putIfKeyAbsent(key, value);
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
    public Object getOrCompute(String key, Function<String, Object> function) {
        return encapsulated.getOrCompute(key, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOrComputeAndPut(String key, Function<String, Object> function) {
        return encapsulated.getOrComputeAndPut(key, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getOrComputeAndPut(String key, Class<T> valueType, Function<String, T> function) {
        return encapsulated.getOrComputeAndPut(key, valueType, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object remove(String key) {
        return encapsulated.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeExactly(String key, Object value) {
        return encapsulated.removeExactly(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object removeOrCompute(String key, Function<String, Object> function) {
        return encapsulated.removeOrCompute(key, function);
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
    public BaseContextShell clear() {
        encapsulated.clear();
        return this;
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
    public BaseContextShell copy() {
        return new BaseContextShell(encapsulated.copy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseContextShell copy(BiPredicate<String, Object> criteria) {
        return new BaseContextShell(encapsulated.copy(criteria));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseContextShell filter(BiPredicate<String, Object> criteria) {
        encapsulated.filter(criteria);
        return this;
    }
}
