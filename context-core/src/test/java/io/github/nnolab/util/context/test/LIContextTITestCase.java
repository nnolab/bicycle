package io.github.nnolab.util.context.test;

import io.github.nnolab.util.context.AbstractContextTestCase;

import java.util.Map;

/**
 * Tests for {@link LIContextTI}.
 *
 * @author nnolab
 */
public class LIContextTITestCase extends AbstractContextTestCase<LIContextTI> {

    @Override
    protected LIContextTI getTestableContext() {
        return new LIContextTI();
    }

    @Override
    protected LIContextTI getTestableContext(int capacity) {
        return new LIContextTI();
    }

    @Override
    protected LIContextTI getTestableContext(Map<String, Object> source) {
        return new LIContextTI(source);
    }
}
