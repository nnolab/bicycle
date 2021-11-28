package io.github.nnolab.util.context;

import java.util.*;
import java.util.function.*;

/**
 * Context, that provides execution of values {@link Object#equals(Object)}
 * methods, functions, predicates and consumers outer of synchronized body.
 * It helps to prevent deadlock if it will appear inside of them.
 *
 * @author nnolab
 */
public interface LockSafeContext extends Context {

    /**
     * {@inheritDoc}
     */
    @Override
    default Object getOrCompute(String key, Function<String, Object> function) {
        Objects.requireNonNull(function);
        Object stub = new Object();
        Object value = getOrDefault(key, stub);
        if (value == stub) {
            value = function.apply(key);
        }
        return value;
    }

    /**
     * In case of concurrent access to this method for one instance
     * function can be called more than once.
     * It may be undesirable for function with side-effects.
     *
     * @param key      key the key whose associated value is to be returned
     * @param function function to compute default value
     * @return the value to which the specified key is mapped, or value, supplied
     * by function if this map contains no mapping for the key
     * @throws NullPointerException if key is {@code null} and implementation
     *                              does not support {@code null} keys
     * @throws NullPointerException if {@code valueType} or {@code function} is {@code null}
     * @throws NullPointerException if function produces {@code null} value and implementation
     *                              does not support {@code null} values
     */
    @Override
    default Object getOrComputeAndPut(String key, Function<String, Object> function) {
        Objects.requireNonNull(function);
        Object stub = new Object();
        Object value = getOrDefault(key, stub);
        if (value == stub) {
            value = function.apply(key);
            put(key, value);
        }
        return value;
    }

    /**
     * In case of concurrent access to this method of one instance
     * {@code function} can be called more than once.
     * It may be undesired for {@code function} with side-effects.
     *
     * @param <T>       expected type of associated value
     * @param key       key key the key whose associated value is to be returned
     * @param valueType class object of expected value type
     * @param function  function to compute default value
     * @return the value to which the specified key is mapped, or value, supplied
     * by function if this map contains no mapping for the key or contains
     * assignment-incompatible associated value or contains {@code null}
     * @throws NullPointerException if key is {@code null} and implementation
     *                              does not support {@code null} keys
     * @throws NullPointerException if {@code valueType} or {@code function} is {@code null}
     * @throws NullPointerException if function produces {@code null} value and implementation
     *                              does not support {@code null} values
     */
    @Override
    @SuppressWarnings("unchecked")
    default <T> T getOrComputeAndPut(String key, Class<T> valueType, Function<String, T> function) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(valueType);
        Object value = get(key);
        if (valueType.isInstance(value)) {
            return (T) value;
        } else {
            value = function.apply(key);
            put(key, value);
            return (T) value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Object removeOrCompute(String key, Function<String, Object> function) {
        Objects.requireNonNull(function);
        Object stub = new Object();
        Object value = removeOrGetDefault(key, stub);
        if (value == stub) {
            value = function.apply(key);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean containsValue(Object value) {
        for (Object val : values()) {
            if (Objects.equals(val, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    LockSafeContext copy();

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copy(BiPredicate<String, Object> criteria) {
        Objects.requireNonNull(criteria);
        LockSafeContext copy = copy();
        for (Entry entry : entries()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!criteria.test(key, value)) {
                copy.remove(key);
            }
        }
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext filter(BiPredicate<String, Object> criteria) {
        Objects.requireNonNull(criteria);
        for (Entry entry : entries()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!criteria.test(key, value)) {
                remove(key);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Context acceptor) {
        Context.super.copyTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Context acceptor, ReplaceRule replaceRule) {
        Context.super.copyTo(acceptor, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Context acceptor, BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Context acceptor, ReplaceRule replaceRule,
                                   BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Context acceptor) {
        Context.super.drainTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Context acceptor, ReplaceRule replaceRule) {
        Context.super.drainTo(acceptor, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Context acceptor, BiPredicate<String, Object> criteria) {
        Context.super.drainTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Context acceptor, ReplaceRule replaceRule,
                                    BiPredicate<String, Object> criteria) {
        Context.super.drainTo(acceptor, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Context source) {
        Context.super.copyFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Context source, ReplaceRule replaceRule) {
        Context.super.copyFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Context source, BiPredicate<String, Object> criteria) {
        Context.super.copyFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Context source, ReplaceRule replaceRule,
                                     BiPredicate<String, Object> criteria) {
        Context.super.copyFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Context source) {
        Context.super.drainFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Context source, ReplaceRule replaceRule) {
        Context.super.drainFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Context source, BiPredicate<String, Object> criteria) {
        Context.super.drainFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Context source, ReplaceRule replaceRule,
                                      BiPredicate<String, Object> criteria) {
        Context.super.drainFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Map<String, Object> acceptor) {
        Context.super.copyTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Map<String, Object> acceptor, boolean replace) {
        Context.super.copyTo(acceptor, replace);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Map<String, Object> acceptor, BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyTo(Map<String, Object> acceptor, boolean replace,
                                   BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, replace, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Map<String, Object> acceptor) {
        Context.super.drainTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Map<String, Object> acceptor, boolean replace) {
        Context.super.drainTo(acceptor, replace);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Map<String, Object> acceptor, BiPredicate<String, Object> criteria) {
        Context.super.drainTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainTo(Map<String, Object> acceptor, boolean replace,
                                    BiPredicate<String, Object> criteria) {
        Context.super.drainTo(acceptor, replace, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Map<String, Object> source) {
        Context.super.copyFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Map<String, Object> source, ReplaceRule replaceRule) {
        Context.super.copyFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Map<String, Object> source, BiPredicate<String, Object> criteria) {
        Context.super.copyFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext copyFrom(Map<String, Object> source, ReplaceRule replaceRule,
                                     BiPredicate<String, Object> criteria) {
        Context.super.copyFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Map<String, Object> source) {
        Context.super.drainFrom(source);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Map<String, Object> source, ReplaceRule replaceRule) {
        Context.super.drainFrom(source, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Map<String, Object> source, BiPredicate<String, Object> criteria) {
        Context.super.drainFrom(source, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext drainFrom(Map<String, Object> source, ReplaceRule replaceRule,
                                      BiPredicate<String, Object> criteria) {
        Context.super.drainFrom(source, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext forEach(BiConsumer<String, Object> action) {
        Context.super.forEach(action);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default LockSafeContext forEach(BiPredicate<String, Object> criteria, BiConsumer<String, Object> action) {
        Context.super.forEach(criteria, action);
        return this;
    }
}
