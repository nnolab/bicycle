package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link BaseSyncContextShell}.
 *
 * @author nnolab
 */
public class BaseSyncContextShellTestCase extends AbstractContextShellTestCase<BaseSyncContextShell> {

    @Override
    protected Function<Context, BaseSyncContextShell> getShellConstructor() {
        return BaseSyncContextShell::new;
    }
}
