package io.github.nnolab.util.context;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Base class for testing {@link ImmutableContext}.
 * <p>By default supposed {@code null} values are supported.
 *
 * @param <IC> the implementation
 * @author nnolab
 */
public abstract class AbstractImmutableContextTestCase<IC extends ImmutableContext>
        extends AbstractContextTestCase<IC> {

    /**
     * Checking method calls, wrapped in runnables, are unsupported.
     *
     * @param methodCalls method calls wrapped in runnables
     */
    protected void checkUnsupported(Runnable... methodCalls) {
        for (Runnable methodCall : methodCalls) {
            try {
                methodCall.run();
                fail();
            } catch (UnsupportedOperationException e) {
            }
        }
    }

    @Override
    public void testCopy() {

        IC context;
        ImmutableContext copy;
        Map<String, Object> source = getSupportMap();
        put12345(source);

        context = getTestableContext(source);
        copy = context.copy();
        assertNotSame(context, copy);
        assertFull12345WithSize(copy);
        assertFull12345WithSize(context);

        context = getTestableContext(source);
        copy = context.copy(allButKey1);
        assertNotSame(context, copy);
        assertFull12345WithSize(context);
        assertFull2345WithSize(copy);
        assertNull(copy.get(key1));
        assertFalse(copy.containsKey(key1));
    }

    @Override
    public void testPutMethods() {
        IC context = getTestableContext();
        checkUnsupported(
                () -> {context.put(key1, value1);},
                () -> {context.putIfAbsent(key1, value1);},
                () -> {context.putIfKeyAbsent(key1, value1);}
        );
    }

    @Override
    public void testRemoveMethods() {
        IC context = getTestableContext();
        checkUnsupported(
                () -> {context.remove(key1);},
                () -> {context.removeExactly(key1, value1);},
                () -> {context.remove(key1, value1);},
                () -> {context.removeOfType(key1, Integer.class);},
                () -> {context.removeOrGetDefault(key1, value1);},
                () -> {context.removeOrGetDefault(key1, Integer.class, value1);},
                () -> {context.removeOrCompute(key1, Integer::parseInt);},
                () -> {context.removeOrCompute(key1, Integer.class, Integer::parseInt);},
                () -> {context.clear();}
        );
    }

    @Override
    public void testGetOrComputeAndPut() {
        IC context = getTestableContext();
        checkUnsupported(
                () -> {context.getOrComputeAndPut(key1, Integer::parseInt);},
                () -> {context.getOrComputeAndPut(key1, Integer.class, Integer::parseInt);}
        );
    }

    @Override
    public void testFilter() {
        IC context = getTestableContext();
        checkUnsupported(
                () -> {context.filter(allButKey1);}
        );
    }

    @Override
    public void testDrainToContext() {
        IC context = getTestableContext();
        Context acceptor = getSupportContext();
        checkUnsupported(
                () -> {context.drainTo(acceptor);},
                () -> {context.drainTo(acceptor, Context.ReplaceRule.PUT);}
        );
    }

    @Override
    public void testDrainToContextWithCriteria() {
        IC context = getTestableContext();
        Context acceptor = getSupportContext();
        checkUnsupported(
                () -> {context.drainTo(acceptor, allButKey1);},
                () -> {context.drainTo(acceptor, Context.ReplaceRule.PUT, allButKey1);}
        );
    }

    @Override
    public void testCopyFromContext() {
        IC context = getTestableContext();
        Context source = getSupportContext();
        checkUnsupported(
                () -> {context.copyFrom(source);},
                () -> {context.copyFrom(source, Context.ReplaceRule.PUT);}
        );
    }

    @Override
    public void testCopyFromContextWithCriteria() {
        IC context = getTestableContext();
        Context source = getSupportContext();
        checkUnsupported(
                () -> {context.copyFrom(source, allButKey1);},
                () -> {context.copyFrom(source, Context.ReplaceRule.PUT, allButKey1);}
        );
    }

    @Override
    public void testDrainFromContext() {
        IC context = getTestableContext();
        Context source = getSupportContext();
        checkUnsupported(
                () -> {context.drainFrom(source);},
                () -> {context.drainFrom(source, Context.ReplaceRule.PUT);}
        );
    }

    @Override
    public void testDrainFromContextWithCriteria() {
        IC context = getTestableContext();
        Context source = getSupportContext();
        checkUnsupported(
                () -> {context.drainFrom(source, allButKey1);},
                () -> {context.drainFrom(source, Context.ReplaceRule.PUT, allButKey1);}
        );
    }

    @Override
    public void testDrainToMap() {
        IC context = getTestableContext();
        Map<String, Object> map = getSupportMap();
        checkUnsupported(
                () -> {context.drainTo(map);},
                () -> {context.drainTo(map, true);}
        );
    }

    @Override
    public void testDrainToMapWithCriteria() {
        IC context = getTestableContext();
        Map<String, Object> map = getSupportMap();
        checkUnsupported(
                () -> {context.drainTo(map, allButKey1);},
                () -> {context.drainTo(map, true, allButKey1);}
        );
    }

    @Override
    public void testCopyFromMap() {
        IC context = getTestableContext();
        Map<String, Object> map = getSupportMap();
        checkUnsupported(
                () -> {context.copyFrom(map);},
                () -> {context.copyFrom(map, Context.ReplaceRule.PUT);}
        );
    }

    @Override
    public void testCopyFromMapWithCriteria() {
        IC context = getTestableContext();
        Map<String, Object> map = getSupportMap();
        checkUnsupported(
                () -> {context.copyFrom(map, allButKey1);},
                () -> {context.copyFrom(map, Context.ReplaceRule.PUT, allButKey1);}
        );
    }

    @Override
    public void testDrainFromMap() {
        IC context = getTestableContext();
        Map<String, Object> map = getSupportMap();
        checkUnsupported(
                () -> {context.drainFrom(map);},
                () -> {context.drainFrom(map, Context.ReplaceRule.PUT);}
        );
    }

    @Override
    public void testDrainFromMapWithCriteria() {
        IC context = getTestableContext();
        Map<String, Object> map = getSupportMap();
        checkUnsupported(
                () -> {context.drainFrom(map, allButKey1);},
                () -> {context.drainFrom(map, Context.ReplaceRule.PUT, allButKey1);}
        );
    }

    @Override
    public void testForEach() {
        immutableTestForEach();
    }

    @Test
    public void testSerialization() {
        doImmutableSerializationChecks();
    }
}
