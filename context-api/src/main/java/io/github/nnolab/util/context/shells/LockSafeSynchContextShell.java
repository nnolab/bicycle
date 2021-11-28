package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Thread-safe, lock-safe shell for encapsulating any {@link Context} instance.
 * Only abstract methods, {@link #equals(Object)} and {@link #toString()}
 * are delegated to encapsulated implementation.
 * Others are implemented by default.
 *
 * @author nnolab
 */
public class LockSafeSynchContextShell implements LockSafeContext, Serializable {

    private static final long serialVersionUID = 6411671366725334226L;

    private final Context encapsulated;

    /**
     * Construct shell with encapsulated instance.
     *
     * @param encapsulated encapsulated instance
     * @throws NullPointerException if {@code encapsulated} is null
     */
    public LockSafeSynchContextShell(Context encapsulated) {
        this.encapsulated = Objects.requireNonNull(encapsulated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "Synchronized_context_shell: {" + encapsulated.toString() + "}";
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
    public synchronized boolean containsKey(String key) {
        return encapsulated.containsKey(key);
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
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Context)) {
            return false;
        }
        Context cobj = (Context) obj;
        if (encapsulated.size() != cobj.size()) {
            return false;
        }
        for (Entry entry : encapsulated.entries()) {
            if (!Objects.equals(entry.getValue(), cobj.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Context clear() {
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
    public synchronized LockSafeSynchContextShell copy() {
        return new LockSafeSynchContextShell(encapsulated.copy());
    }
}
