package io.github.nnolab.util.context.multithread;

import io.github.nnolab.util.context.impl.MapBasedSIContext;
import io.github.nnolab.util.context.impl.mapb.*;

import java.util.Map;

/**
 * Provider of {@link MapBasedSIContext}.
 *
 * @author nnolab
 */
public interface MapBasedSIContextProvider
        extends ContextImplementationProvider<MapBasedSIContext> {

    @Override
    default MapBasedSIContext getContext() {
        return new MapBasedSIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP);
    }

    @Override
    default MapBasedSIContext getContext(int capacity) {
        return new MapBasedSIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP,
                capacity);
    }

    @Override
    default MapBasedSIContext getContext(Map<String, Object> source) {
        return new MapBasedSIContext(false, true,
                DefaultMapSuppliers.HASH_MAP,
                CapacityMapSuppliers.HASH_MAP,
                source);
    }
}
