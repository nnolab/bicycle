package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.*;

/**
 * Immutable shell for encapsulating any {@link Context} instance.
 * Only abstract methods, {@link #equals(Object)} and {@link #toString()}
 * are delegated to encapsulated implementation.
 * Others are implemented by default.
 *
 * @author nnolab
 */
public class BaseImmutableContextShell implements ImmutableContext, Serializable {

    private static final long serialVersionUID = 1155735597595826668L;

    private final Context encapsulated;

    /**
     * Construct shell with encapsulated instance.
     *
     * @param encapsulated encapsulated instance
     * @throws NullPointerException if {@code encapsulated} is null
     */
    public BaseImmutableContextShell(Context encapsulated) {
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
    public Object get(String key) {
        return encapsulated.get(key);
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
    public BaseImmutableContextShell copy() {
        return new BaseImmutableContextShell(encapsulated.copy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseImmutableContextShell copy(BiPredicate<String, Object> criteria) {
        return new BaseImmutableContextShell(encapsulated.copy(criteria));
    }
}
