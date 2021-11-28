package io.github.nnolab.util.context.impl;

import io.github.nnolab.util.context.AbstractContextTestCase;
import io.github.nnolab.util.context.impl.mapb.*;

import java.util.Map;

/**
 * Tests for {@link MapBasedLIContext}.
 *
 * @author nnolab
 */
public class MapBasedLIContextTestCase extends AbstractContextTestCase<MapBasedLIContext> {
    
    @Override
    protected MapBasedLIContext getTestableContext() {
        return new MapBasedLIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP);
    }

    @Override
    protected MapBasedLIContext getTestableContext(int capacity) {
        return new MapBasedLIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP,
                capacity);
    }

    @Override
    protected MapBasedLIContext getTestableContext(Map<String, Object> source) {
        return new MapBasedLIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP,
                source);
    }
}
