package io.github.nnolab.util.context.impl;

import io.github.nnolab.util.context.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.*;

/**
 * Extension of {@link AbstractContext} with linked iterable entries.
 *
 * @author nnolab
 */
public abstract class AbstractLIContext extends AbstractContext {

    /**
     * Default.
     */
    public AbstractLIContext() {
    }

    /**
     * @see AbstractContext#AbstractContext(boolean, boolean)
     */
    public AbstractLIContext(boolean supportNullKeys, boolean supportNullValues) {
        super(supportNullKeys, supportNullValues);
    }

    /**
     * Anchor for new entries and iteration.
     */
    protected IterNode head = new IterNode(null, null);

    /**
     * Number of key-value mappings.
     */
    protected transient volatile int size = 0;

    /**
     * Put given node into node set.
     *
     * @param node node to be placed
     */
    protected abstract void putNode(IterNode node);

    /**
     * Find node, contains given key or return {@code null}.
     *
     * @param key key for lookup
     * @return node, contains given key or {@code null}
     */
    protected abstract IterNode findNode(String key);

    /**
     * Remove node from node set.
     *
     * @param node node to be removed
     */
    protected abstract void removeNode(IterNode node);

    /**
     * Add node onto head of chain.
     *
     * @param node node to be added
     * @throws NullPointerException if node is {@code null}
     */
    protected void addNodeToHead(IterNode node) {
        node.prev = head;
        node.next = head.next;
        head.next = node;
        if (node.next != null) {
            node.next.prev = node;
        }
    }

    /**
     * Create new node with given key and value.
     *
     * @param key   key for new node
     * @param value value for new node
     * @return new node
     */
    protected IterNode createNewNode(String key, Object value) {
        return new IterNode(key, value);
    }

    /**
     * Create and put node with specified key and value.
     *
     * @param key   key
     * @param value value
     */
    protected void addNewNode(String key, Object value) {
        IterNode node = createNewNode(key, value);
        putNode(node);
        addNodeToHead(node);
        size++;
    }

    /**
     * Common serialization.
     */
    protected void write(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);
        IterNode node = head.next;
        while (node != null) {
            s.writeObject(node);
            node = node.next;
        }
    }

    /**
     * Common deserialization.
     */
    protected void read(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        size = 0;
        int sz = s.readInt();
        for (int i = 0; i < sz; i++) {
            IterNode node = (IterNode) s.readObject();
            putNode(node);
            addNodeToHead(node);
            size++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Context: {");
        IterNode node = head.next;
        while (node != null) {
            sb.append(node.key).append("=");
            if (node.value == this) {
                sb.append("(this Context)");
            } else {
                sb.append(node.value);
            }
            sb.append("; ");
            node = node.next;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object put(String key, Object value) {
        checkNullKV(key, value);
        IterNode node = findNode(key);
        if (node == null) {
            addNewNode(key, value);
            return null;
        } else {
            Object oldVal = node.value;
            node.value = value;
            return oldVal;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String key) {
        checkNullKey(key);
        IterNode node = findNode(key);
        if (node == null) {
            return null;
        } else {
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object putIfAbsent(String key, Object value) {
        checkNullKV(key, value);
        IterNode node = findNode(key);
        if (node == null) {
            addNewNode(key, value);
            return null;
        } else if (node.value == null) {
            node.value = value;
            return null;
        } else {
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object putIfKeyAbsent(String key, Object value) {
        checkNullKV(key, value);
        IterNode node = findNode(key);
        if (node == null) {
            addNewNode(key, value);
            return null;
        } else {
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOrDefault(String key, Object defaultValue) {
        checkNullKey(key);
        IterNode node = findNode(key);
        if (node == null) {
            return defaultValue;
        } else {
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOrCompute(String key, Function<String, Object> function) {
        checkNullKey(key);
        Objects.requireNonNull(function);
        IterNode node = findNode(key);
        if (node == null) {
            return function.apply(key);
        } else {
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOrComputeAndPut(String key, Function<String, Object> function) {
        checkNullKey(key);
        Objects.requireNonNull(function);
        IterNode node = findNode(key);
        if (node == null) {
            Object value = function.apply(key);
            checkNullValue(value);
            addNewNode(key, value);
            return value;
        } else {
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOrComputeAndPut(String key, Class<T> valueType, Function<String, T> function) {
        checkNullKey(key);
        Objects.requireNonNull(valueType);
        Objects.requireNonNull(function);
        IterNode node = findNode(key);
        if (node == null) {
            T value = function.apply(key);
            checkNullValue(value);
            addNewNode(key, value);
            return value;
        } else if (!(valueType.isInstance(node.value))) {
            T value = function.apply(key);
            checkNullValue(value);
            node.value = value;
            return value;
        } else {
            return (T) node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object remove(String key) {
        checkNullKey(key);
        IterNode node = findNode(key);
        if (node == null) {
            return null;
        } else {
            removeNode(node);
            size--;
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeExactly(String key, Object value) {
        checkNullKey(key);
        IterNode node = findNode(key);
        if (node == null) {
            return false;
        } else if (node.value == value) {
            removeNode(node);
            size--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object removeOrCompute(String key, Function<String, Object> function) {
        checkNullKey(key);
        Objects.requireNonNull(function);
        IterNode node = findNode(key);
        if (node == null) {
            return function.apply(key);
        } else {
            removeNode(node);
            size--;
            return node.value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(String key) {
        checkNullKey(key);
        return findNode(key) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        checkNullValue(value);
        IterNode node = head.next;
        while (node != null) {
            if (Objects.equals(node.value, value)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
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
        if (cobj.size() != size()) {
            return false;
        }
        IterNode node = head.next;
        while (node != null) {
            if (!Objects.equals(node.value, cobj.get(node.key))) {
                return false;
            }
            node = node.next;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext clear() {
        head.next = null;
        size = 0;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext.Keys keys() {
        return new LinkedKeys(head);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext.Values values() {
        return new LinkedValues(head);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext.Entries entries() {
        return new LinkedEntries(head);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext filter(BiPredicate<String, Object> criteria) {
        Objects.requireNonNull(criteria);
        IterNode node = head.next;
        while (node != null) {
            if (!criteria.test(node.key, node.value)) {
                removeNode(node);
                size--;
            }
            node = node.next;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext copyTo(Context acceptor, Context.ReplaceRule replaceRule) {
        Objects.requireNonNull(acceptor);
        Objects.requireNonNull(replaceRule);
        if (acceptor == this) {
            throw new IllegalArgumentException();
        }
        IterNode node = head.next;
        switch (replaceRule) {
            case PUT:
                while (node != null) {
                    acceptor.put(node.key, node.value);
                    node = node.next;
                }
                break;
            case PUT_IF_ABSENT:
                while (node != null) {
                    acceptor.putIfAbsent(node.key, node.value);
                    node = node.next;
                }
                break;
            case PUT_IF_KEY_ABSENT:
                while (node != null) {
                    acceptor.putIfKeyAbsent(node.key, node.value);
                    node = node.next;
                }
                break;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext copyTo(Context acceptor, Context.ReplaceRule replaceRule,
                                    BiPredicate<String, Object> criteria) {
        Objects.requireNonNull(acceptor);
        Objects.requireNonNull(replaceRule);
        Objects.requireNonNull(criteria);
        if (acceptor == this) {
            throw new IllegalArgumentException();
        }
        String key;
        Object value;
        IterNode node = head.next;
        switch (replaceRule) {
            case PUT:
                while (node != null) {
                    key = node.key;
                    value = node.value;
                    if (criteria.test(key, value)) {
                        acceptor.put(key, value);
                    }
                    node = node.next;
                }
                break;
            case PUT_IF_ABSENT:
                while (node != null) {
                    key = node.key;
                    value = node.value;
                    if (criteria.test(key, value)) {
                        acceptor.putIfAbsent(key, value);
                    }
                    node = node.next;
                }
                break;
            case PUT_IF_KEY_ABSENT:
                while (node != null) {
                    key = node.key;
                    value = node.value;
                    if (criteria.test(key, value)) {
                        acceptor.putIfKeyAbsent(key, value);
                    }
                    node = node.next;
                }
                break;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext drainTo(Context acceptor, Context.ReplaceRule replaceRule) {
        Objects.requireNonNull(acceptor);
        Objects.requireNonNull(replaceRule);
        if (acceptor == this) {
            throw new IllegalArgumentException();
        }
        IterNode node = head.next;
        switch (replaceRule) {
            case PUT:
                while (node != null) {
                    acceptor.put(node.key, node.value);
                    removeNode(node);
                    size--;
                    node = node.next;
                }
                break;
            case PUT_IF_ABSENT:
                while (node != null) {
                    acceptor.putIfAbsent(node.key, node.value);
                    removeNode(node);
                    size--;
                    node = node.next;
                }
                break;
            case PUT_IF_KEY_ABSENT:
                while (node != null) {
                    acceptor.putIfKeyAbsent(node.key, node.value);
                    removeNode(node);
                    size--;
                    node = node.next;
                }
                break;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext drainTo(Context acceptor, Context.ReplaceRule replaceRule,
                                     BiPredicate<String, Object> criteria) {
        Objects.requireNonNull(acceptor);
        Objects.requireNonNull(replaceRule);
        Objects.requireNonNull(criteria);
        if (acceptor == this) {
            throw new IllegalArgumentException();
        }
        String key;
        Object value;
        IterNode node = head.next;
        switch (replaceRule) {
            case PUT:
                while (node != null) {
                    key = node.key;
                    value = node.value;
                    if (criteria.test(key, value)) {
                        acceptor.put(key, value);
                        removeNode(node);
                        size--;
                    }
                    node = node.next;
                }
                break;
            case PUT_IF_ABSENT:
                while (node != null) {
                    key = node.key;
                    value = node.value;
                    if (criteria.test(key, value)) {
                        acceptor.putIfAbsent(key, value);
                        removeNode(node);
                        size--;
                    }
                    node = node.next;
                }
                break;
            case PUT_IF_KEY_ABSENT:
                while (node != null) {
                    key = node.key;
                    value = node.value;
                    if (criteria.test(key, value)) {
                        acceptor.putIfKeyAbsent(key, value);
                        removeNode(node);
                        size--;
                    }
                    node = node.next;
                }
                break;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext copyTo(Map<String, Object> acceptor, boolean replace) {
        Objects.requireNonNull(acceptor);
        IterNode node = head.next;
        if (replace) {
            while (node != null) {
                acceptor.put(node.key, node.value);
                node = node.next;
            }
        } else {
            while (node != null) {
                acceptor.putIfAbsent(node.key, node.value);
                node = node.next;
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext copyTo(Map<String, Object> acceptor, boolean replace,
                                    BiPredicate<String, Object> criteria) {
        Objects.requireNonNull(acceptor);
        Objects.requireNonNull(criteria);
        String key;
        Object value;
        IterNode node = head.next;
        if (replace) {
            while (node != null) {
                key = node.key;
                value = node.value;
                if (criteria.test(key, value)) {
                    acceptor.put(key, value);
                }
                node = node.next;
            }
        } else {
            while (node != null) {
                key = node.key;
                value = node.value;
                if (criteria.test(key, value)) {
                    acceptor.putIfAbsent(key, value);
                }
                node = node.next;
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext drainTo(Map<String, Object> acceptor, boolean replace) {
        Objects.requireNonNull(acceptor);
        IterNode node = head.next;
        if (replace) {
            while (node != null) {
                acceptor.put(node.key, node.value);
                removeNode(node);
                size--;
                node = node.next;
            }
        } else {
            while (node != null) {
                acceptor.putIfAbsent(node.key, node.value);
                removeNode(node);
                size--;
                node = node.next;
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext drainTo(Map<String, Object> acceptor, boolean replace,
                                     BiPredicate<String, Object> criteria) {
        Objects.requireNonNull(acceptor);
        Objects.requireNonNull(criteria);
        String key;
        Object value;
        IterNode node = head.next;
        if (replace) {
            while (node != null) {
                key = node.key;
                value = node.value;
                if (criteria.test(key, value)) {
                    acceptor.put(key, value);
                    removeNode(node);
                    size--;
                }
                node = node.next;
            }
        } else {
            while (node != null) {
                key = node.key;
                value = node.value;
                if (criteria.test(key, value)) {
                    acceptor.putIfAbsent(key, value);
                    removeNode(node);
                    size--;
                }
                node = node.next;
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext forEach(BiConsumer<String, Object> action) {
        Objects.requireNonNull(action);
        IterNode node = head.next;
        while (node != null) {
            action.accept(node.key, node.value);
            node = node.next;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLIContext forEach(BiPredicate<String, Object> criteria, BiConsumer<String, Object> action) {
        Objects.requireNonNull(criteria);
        Objects.requireNonNull(action);
        String key;
        Object value;
        IterNode node = head.next;
        while (node != null) {
            key = node.key;
            value = node.value;
            if (criteria.test(key, value)) {
                action.accept(key, value);
            }
            node = node.next;
        }
        return this;
    }
}
