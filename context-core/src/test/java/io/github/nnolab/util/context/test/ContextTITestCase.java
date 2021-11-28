package io.github.nnolab.util.context.test;

import io.github.nnolab.util.context.AbstractContextTestCase;

import java.util.Map;

/**
 * Tests for {@link ContextTI}.
 *
 * @author nnolab
 */
public class ContextTITestCase extends AbstractContextTestCase<ContextTI> {

    @Override
    protected ContextTI getTestableContext() {
        return new ContextTI(1);
    }

    @Override
    protected ContextTI getTestableContext(int capacity) {
        return new ContextTI(capacity);
    }

    @Override
    protected ContextTI getTestableContext(Map<String, Object> source) {
        return new ContextTI(source);
    }
}
