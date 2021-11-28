package io.github.nnolab.util.context.test;

import io.github.nnolab.util.context.AbstractImmutableContextTestCase;

import java.util.Map;

/**
 * Tests for {@link ImmutableContextTI}.
 *
 * @author nnolab
 */
public class ImmutableContextTITestCase extends AbstractImmutableContextTestCase<ImmutableContextTI> {

    @Override
    protected ImmutableContextTI getTestableContext() {
        return new ImmutableContextTI(1);
    }

    @Override
    protected ImmutableContextTI getTestableContext(int capacity) {
        return new ImmutableContextTI(capacity);
    }

    @Override
    protected ImmutableContextTI getTestableContext(Map<String, Object> source) {
        return new ImmutableContextTI(source);
    }
}
