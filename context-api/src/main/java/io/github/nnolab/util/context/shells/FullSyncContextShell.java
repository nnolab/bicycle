package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

/**
 * Thread-safe shell for encapsulating any {@link Context} instance.
 * All methods are delegated to encapsulated implementation.
 *
 * @author nnolab
 */
public class FullSyncContextShell implements Context, Serializable {

    private static final long serialVersionUID = -8715696052611944788L;

    private final Context encapsulated;

    /**
     * Construct shell with encapsulated instance.
     *
     * @param encapsulated encapsulated instance
     * @throws NullPointerException if {@code encapsulated} is null
     */
    public FullSyncContextShell(Context encapsulated) {
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
    public synchronized Object put(Object value) {
        return encapsulated.put(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> Object put(Class<? super T> valueType, T value) {
        return encapsulated.put(valueType, value);
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
    public synchronized <T> T get(String key, Class<T> valueType) {
        return encapsulated.get(key, valueType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> T get(Class<T> valueType) {
        return encapsulated.get(valueType);
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
    public synchronized Object putIfAbsent(Object value) {
        return encapsulated.putIfAbsent(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> Object putIfAbsent(Class<? super T> valueType, T value) {
        return encapsulated.putIfAbsent(valueType, value);
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
    public synchronized Object putIfKeyAbsent(Object value) {
        return encapsulated.putIfKeyAbsent(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> Object putIfKeyAbsent(Class<? super T> valueType, T value) {
        return encapsulated.putIfKeyAbsent(valueType, value);
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
    public synchronized <T> T getOrDefault(String key, Class<T> valueType, T defaultValue) {
        return encapsulated.getOrDefault(key, valueType, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> T getOrDefault(Class<T> valueType, T defaultValue) {
        return encapsulated.getOrDefault(valueType, defaultValue);
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
    public synchronized <T> T getOrCompute(String key, Class<T> valueType, Function<String, T> function) {
        return encapsulated.getOrCompute(key, valueType, function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> T getOrCompute(Class<T> valueType, Function<String, T> function) {
        return encapsulated.getOrCompute(valueType, function);
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
    public synchronized <T> T getOrComputeAndPut(Class<T> valueType, Function<String, T> function) {
        return encapsulated.getOrComputeAndPut(valueType, function);
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
    public synchronized boolean remove(String key, Object value) {
        return encapsulated.remove(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> T removeOfType(String key, Class<T> valueType) {
        return encapsulated.removeOfType(key, valueType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object removeOrGetDefault(String key, Object defaultValue) {
        return encapsulated.removeOrGetDefault(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <T> T removeOrGetDefault(String key, Class<T> valueType, T defaultValue) {
        return encapsulated.removeOrGetDefault(key, valueType, defaultValue);
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
    public synchronized <T> T removeOrCompute(String key, Class<T> valueType, Function<String, T> function) {
        return encapsulated.removeOrCompute(key, valueType, function);
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
    public synchronized FullSyncContextShell clear() {
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
    public synchronized FullSyncContextShell copy() {
        return new FullSyncContextShell(encapsulated.copy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copy(BiPredicate<String, Object> criteria) {
        return new FullSyncContextShell(encapsulated.copy(criteria));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell filter(BiPredicate<String, Object> criteria) {
        encapsulated.filter(criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Context acceptor) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Context acceptor, ReplaceRule replaceRule) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Context acceptor, BiPredicate<String, Object> criteria) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Context acceptor, ReplaceRule replaceRule,
                                                    BiPredicate<String, Object> criteria) {
        checkNotThis(acceptor);
        encapsulated.copyTo(acceptor, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Context acceptor) {
        checkNotThis(acceptor);
        encapsulated.drainTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Context acceptor, ReplaceRule replaceRule) {
        checkNotThis(acceptor);
        encapsulated.drainTo(acceptor, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Context acceptor, BiPredicate<String, Object> criteria) {
        checkNotThis(acceptor);
        encapsulated.drainTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Context acceptor, ReplaceRule replaceRule,
                                                     BiPredicate<String, Object> criteria) {
        checkNotThis(acceptor);
        encapsulated.drainTo(acceptor, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Context source) {
        checkNotThis(source);
        encapsulated.copyFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Context source, ReplaceRule replaceRule) {
        checkNotThis(source);
        encapsulated.copyFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Context source, BiPredicate<String, Object> criteria) {
        checkNotThis(source);
        encapsulated.copyFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Context source, ReplaceRule replaceRule,
                                                      BiPredicate<String, Object> criteria) {
        checkNotThis(source);
        encapsulated.copyFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Context source) {
        checkNotThis(source);
        encapsulated.drainFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Context source, ReplaceRule replaceRule) {
        checkNotThis(source);
        encapsulated.drainFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Context source, BiPredicate<String, Object> criteria) {
        checkNotThis(source);
        encapsulated.drainFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Context source, ReplaceRule replaceRule,
                                                       BiPredicate<String, Object> criteria) {
        checkNotThis(source);
        encapsulated.drainFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Map<String, Object> acceptor) {
        encapsulated.copyTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Map<String, Object> acceptor, boolean replace) {
        encapsulated.copyTo(acceptor, replace);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Map<String, Object> acceptor, BiPredicate<String, Object> criteria) {
        encapsulated.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyTo(Map<String, Object> acceptor, boolean replace,
                                                    BiPredicate<String, Object> criteria) {
        encapsulated.copyTo(acceptor, replace, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Map<String, Object> acceptor) {
        encapsulated.drainTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Map<String, Object> acceptor, boolean replace) {
        encapsulated.drainTo(acceptor, replace);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Map<String, Object> acceptor, BiPredicate<String, Object> criteria) {
        encapsulated.drainTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainTo(Map<String, Object> acceptor, boolean replace,
                                                     BiPredicate<String, Object> criteria) {
        encapsulated.drainTo(acceptor, replace, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Map<String, Object> source) {
        encapsulated.copyFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Map<String, Object> source, ReplaceRule replaceRule) {
        encapsulated.copyFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Map<String, Object> source, BiPredicate<String, Object> criteria) {
        encapsulated.copyFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell copyFrom(Map<String, Object> source, ReplaceRule replaceRule,
                                                      BiPredicate<String, Object> criteria) {
        encapsulated.copyFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Map<String, Object> source) {
        encapsulated.drainFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Map<String, Object> source, ReplaceRule replaceRule) {
        encapsulated.drainFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Map<String, Object> source, BiPredicate<String, Object> criteria) {
        encapsulated.drainFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell drainFrom(Map<String, Object> source, ReplaceRule replaceRule,
                                                       BiPredicate<String, Object> criteria) {
        encapsulated.drainFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell forEach(BiConsumer<String, Object> action) {
        encapsulated.forEach(action);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized FullSyncContextShell forEach(BiPredicate<String, Object> criteria, BiConsumer<String, Object> action) {
        encapsulated.forEach(criteria, action);
        return this;
    }
}
