package io.github.nnolab.util.context.impl;

import io.github.nnolab.util.context.AbstractContextTestCase;

import java.util.Map;

/**
 * Test case for {@link HashContext}.
 *
 * @author nnolab
 */
public class HashContextTestCase extends AbstractContextTestCase<HashContext> {

    @Override
    protected HashContext getTestableContext() {
        return new HashContext();
    }

    @Override
    protected HashContext getTestableContext(int capacity) {
        return new HashContext(capacity);
    }

    @Override
    protected HashContext getTestableContext(Map<String, Object> source) {
        HashContext context = new HashContext(source);
        context.resize(2, 0.5f, 2, 0.5f);
        return context;
    }
}
