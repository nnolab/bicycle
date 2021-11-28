package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link LockSafeSynchContextShell}.
 *
 * @author nnolab
 */
public class LockSafeSynchContextShellTestCase
        extends AbstractContextShellTestCase<LockSafeSynchContextShell> {

    @Override
    protected Function<Context, LockSafeSynchContextShell> getShellConstructor() {
        return LockSafeSynchContextShell::new;
    }
}
