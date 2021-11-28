package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link FullSyncContextShell}.
 *
 * @author nnolab
 */
public class FullSyncContextShellTestCase extends AbstractContextShellTestCase<FullSyncContextShell> {

    @Override
    protected Function<Context, FullSyncContextShell> getShellConstructor() {
        return FullSyncContextShell::new;
    }
}
