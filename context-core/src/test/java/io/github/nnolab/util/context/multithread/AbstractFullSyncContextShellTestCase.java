package io.github.nnolab.util.context.multithread;

import io.github.nnolab.util.context.*;
import io.github.nnolab.util.context.shells.*;

import java.util.Map;

/**
 * Tests for context implementations, encapsulated in {@link FullSyncContextShell}.
 *
 * @param <C> implementation
 * @author nnolab
 */
public abstract class AbstractFullSyncContextShellTestCase<C extends Context>
        extends AbstractSynchronizedThreadContextTestCase<FullSyncContextShell>
        implements ContextImplementationProvider<C> {

    @Override
    protected FullSyncContextShell getTestableContext() {
        return new FullSyncContextShell(getContext());
    }

    @Override
    protected FullSyncContextShell getTestableContext(int capacity) {
        return new FullSyncContextShell(getContext(capacity));
    }

    @Override
    protected FullSyncContextShell getTestableContext(Map<String, Object> source) {
        return new FullSyncContextShell(getContext(source));
    }
}
