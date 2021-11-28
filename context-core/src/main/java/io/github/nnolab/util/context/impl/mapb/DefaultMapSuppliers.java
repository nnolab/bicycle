package io.github.nnolab.util.context.impl.mapb;

import java.util.*;
import java.util.function.Supplier;

/**
 * Serializable suppliers of map with default initial capacity.
 *
 * @author nnolab
 */
public enum DefaultMapSuppliers implements Supplier<Map<String, Object>> {

    HASH_MAP {
        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> get() {
            return new HashMap<>();
        }
    },

    IDENTITY_HASH_MAP {
        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> get() {
            return new IdentityHashMap<>();
        }
    },

    WEAK_HASH_MAP {
        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> get() {
            return new WeakHashMap<>();
        }
    },

    TREE_MAP {
        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> get() {
            return new TreeMap<>();
        }
    };
}
