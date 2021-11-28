package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link FullImmutableContextShell}.
 *
 * @author nnolab
 */
public class FullImmutableContextShellTestCase
        extends AbstractImmutableShellContextTestCase<FullImmutableContextShell> {

    @Override
    protected Function<Context, FullImmutableContextShell> getShellConstructor() {
        return FullImmutableContextShell::new;
    }
}
