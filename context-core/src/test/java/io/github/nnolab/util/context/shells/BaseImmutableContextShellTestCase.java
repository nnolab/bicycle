package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link BaseImmutableContextShell}.
 *
 * @author nnolab
 */
public class BaseImmutableContextShellTestCase
        extends AbstractImmutableShellContextTestCase<BaseImmutableContextShell> {

    @Override
    protected Function<Context, BaseImmutableContextShell> getShellConstructor() {
        return BaseImmutableContextShell::new;
    }
}
