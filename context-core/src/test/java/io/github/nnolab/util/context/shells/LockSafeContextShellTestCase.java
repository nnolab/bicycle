package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link LockSafeContextShell}.
 *
 * @author nnolab
 */
public class LockSafeContextShellTestCase extends AbstractContextShellTestCase<LockSafeContextShell> {

    @Override
    protected Function<Context, LockSafeContextShell> getShellConstructor() {
        return LockSafeContextShell::new;
    }
}
