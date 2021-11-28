package io.github.nnolab.util.context;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Context supposed to be immutable.
 *
 * @author nnolab
 */
public interface ImmutableContext extends Context {

    /*These operations are overriden for optimization*/

    /**
     * {@inheritDoc}
     */
    @Override
    default Object getOrDefault(String key, Object defaultValue) {
        Object value = get(key);
        if (value == null && !containsKey(key)) {
            return defaultValue;
        } else {
            return value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Object getOrCompute(String key, Function<String, Object> function) {
        Objects.requireNonNull(function);
        Object value = get(key);
        if (value == null && !containsKey(key)) {
            return function.apply(key);
        } else {
            return value;
        }
    }

    /*These operations are allowed and overriden to return {@code ImmutableContext}*/

    /**
     * {@inheritDoc}
     */
    @Override
    ImmutableContext copy();

    /**
     * {@inheritDoc}
     */
    @Override
    ImmutableContext copy(BiPredicate<String, Object> criteria);

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Context acceptor) {

        return copyTo(acceptor, ReplaceRule.PUT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Context acceptor, ReplaceRule replaceRule) {
        Context.super.copyTo(acceptor, replaceRule);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Context acceptor, BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Context acceptor, ReplaceRule replaceRule,
                                    BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, replaceRule, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Map<String, Object> acceptor) {
        Context.super.copyTo(acceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Map<String, Object> acceptor, boolean replace) {
        Context.super.copyTo(acceptor, replace);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Map<String, Object> acceptor, BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext copyTo(Map<String, Object> acceptor, boolean replace,
                                    BiPredicate<String, Object> criteria) {
        Context.super.copyTo(acceptor, replace, criteria);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext forEach(BiConsumer<String, Object> action) {
        Context.super.forEach(action);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default ImmutableContext forEach(BiPredicate<String, Object> criteria, BiConsumer<String, Object> action) {
        Context.super.forEach(criteria, action);
        return this;
    }

    /*These operations break immutability and not allowed*/

    /**
     * Not supported.
     */
    @Override
    default Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Object putIfAbsent(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Object putIfKeyAbsent(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Object getOrComputeAndPut(String key, Function<String, Object> function) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default <T> T getOrComputeAndPut(String key, Class<T> valueType, Function<String, T> function) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Object remove(String key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default boolean removeExactly(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default boolean remove(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default <T> T removeOfType(String key, Class<T> valueType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Object removeOrGetDefault(String key, Object defaultValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default <T> T removeOrGetDefault(String key, Class<T> valueType, T defaultValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Object removeOrCompute(String key, Function<String, Object> function) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default <T> T removeOrCompute(String key, Class<T> valueType, Function<String, T> supplier) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context filter(BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Context acceptor) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Context acceptor, ReplaceRule replaceRule) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Context acceptor, BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Context acceptor, ReplaceRule replaceRule,
                            BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Context source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Context source, ReplaceRule replaceRule) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Context source, BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Context source, ReplaceRule replaceRule,
                             BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Context source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Context source, ReplaceRule replaceRule) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Context source, BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Context source, ReplaceRule replaceRule,
                              BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Map<String, Object> acceptor) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Map<String, Object> acceptor, boolean replace) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Map<String, Object> acceptor, BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainTo(Map<String, Object> acceptor, boolean replace,
                            BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Map<String, Object> source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Map<String, Object> source, ReplaceRule replaceRule) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Map<String, Object> source, BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context copyFrom(Map<String, Object> source, ReplaceRule replaceRule,
                             BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Map<String, Object> source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Map<String, Object> source, ReplaceRule replaceRule) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Map<String, Object> source, BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    default Context drainFrom(Map<String, Object> source, ReplaceRule replaceRule, BiPredicate<String, Object> criteria) {
        throw new UnsupportedOperationException();
    }
}
