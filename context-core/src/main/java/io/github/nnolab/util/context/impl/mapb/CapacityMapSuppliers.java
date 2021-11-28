package io.github.nnolab.util.context.impl.mapb;

import java.util.*;
import java.util.function.IntFunction;

/**
 * Serializable suppliers of map with specified initial capacity.
 *
 * @author nnolab
 */
public enum CapacityMapSuppliers implements IntFunction<Map<String, Object>> {

    HASH_MAP {
        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> apply(int value) {
            return new HashMap<>(value);
        }
    },

    IDENTITY_HASH_MAP {
        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> apply(int value) {
            return new IdentityHashMap<>(value);
        }
    },

    WEAK_HASH_MAP {
        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> apply(int value) {
            return new WeakHashMap<>(value);
        }
    }
}
