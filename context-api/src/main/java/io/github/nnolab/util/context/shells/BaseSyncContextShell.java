package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.*;

/**
 * Thread-safe shell for encapsulating any {@link Context} instance.
 * Only abstract methods and, {@link #equals(Object)} and {@link #toString()}
 * are delegated to encapsulated implementation.
 * Others are implemented by default.
 *
 * @author nnolab
 */
public class BaseSyncContextShell implements Context, Serializable {

    private static final long serialVersionUID = -6397148135277109390L;

    private final Context encapsulated;

    /**
     * Construct shell with encapsulated instance.
     *
     * @param encapsulated encapsulated instance
     * @throws NullPointerException if {@code encapsulated} is null
     */
    public BaseSyncContextShell(Context encapsulated) {
        this.encapsulated = Objects.requireNonNull(encapsulated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return getClass().getName() + ": {" + encapsulated.toString() + "}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object put(String key, Object value) {
        return encapsulated.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object get(String key) {
        return encapsulated.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object putIfAbsent(String key, Object value) {
        return encapsulated.putIfAbsent(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object putIfKeyAbsent(String key, Object value) {
        return encapsulated.putIfKeyAbsent(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object getOrDefault(String key, Object defaultValue) {
        return encapsulated.getOrDefault(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object getOrCompute(String key, Function<String, Object> function) {
        return encapsulated.getOrCompute(key, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object getOrComputeAndPut(String key, Function<String, Object> function) {
        return encapsulated.getOrComputeAndPut(key, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> T getOrComputeAndPut(String key, Class<T> valueType, Function<String, T> function) {
        return encapsulated.getOrComputeAndPut(key, valueType, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object remove(String key) {
        return encapsulated.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean removeExactly(String key, Object value) {
        return encapsulated.removeExactly(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object removeOrCompute(String key, Function<String, Object> function) {
        return encapsulated.removeOrCompute(key, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean containsKey(String key) {
        return encapsulated.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean containsValue(Object value) {
        return encapsulated.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int size() {
        return encapsulated.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isEmpty() {
        return encapsulated.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean equals(Object obj) {
        return encapsulated.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized BaseSyncContextShell clear() {
        encapsulated.clear();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Keys keys() {
        return encapsulated.keys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Values values() {
        return encapsulated.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Entries entries() {
        return encapsulated.entries();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized BaseSyncContextShell copy() {
        return new BaseSyncContextShell(encapsulated.copy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized BaseSyncContextShell copy(BiPredicate<String, Object> criteria) {
        return new BaseSyncContextShell(encapsulated.copy(criteria));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized BaseSyncContextShell filter(BiPredicate<String, Object> criteria) {
        encapsulated.filter(criteria);
        return this;
    }
}
