package io.github.nnolab.util.context.multithread;

import io.github.nnolab.util.context.impl.HashContext;

import java.util.Map;

/**
 * Provider of {@link HashContext}.
 *
 * @author nnolab
 */
public interface HashContextProvider
        extends ContextImplementationProvider<HashContext> {

    @Override
    default HashContext getContext() {
        return new HashContext();
    }

    @Override
    default HashContext getContext(int capacity) {
        return new HashContext(capacity);
    }

    @Override
    default HashContext getContext(Map<String, Object> source) {
        return new HashContext(source);
    }
}
