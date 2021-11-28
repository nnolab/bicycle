package io.github.nnolab.util.context.impl;

import io.github.nnolab.util.context.AbstractContextTestCase;
import io.github.nnolab.util.context.impl.mapb.*;

import java.util.Map;

/**
 * Tests for {@link MapBasedSIContext}.
 *
 * @author nnolab
 */
public class MapBasedSIContextTestCase extends AbstractContextTestCase<MapBasedSIContext> {

    @Override
    protected MapBasedSIContext getTestableContext() {
        return new MapBasedSIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP);
    }

    @Override
    protected MapBasedSIContext getTestableContext(int capacity) {
        return new MapBasedSIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP,
                capacity);
    }

    @Override
    protected MapBasedSIContext getTestableContext(Map<String, Object> source) {
        return new MapBasedSIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP,
                source);
    }
}
