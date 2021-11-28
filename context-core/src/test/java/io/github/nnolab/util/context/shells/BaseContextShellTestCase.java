package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link BaseContextShell}.
 *
 * @author nnolab
 */
public class BaseContextShellTestCase extends AbstractContextShellTestCase<BaseContextShell> {

    @Override
    protected Function<Context, BaseContextShell> getShellConstructor() {
        return BaseContextShell::new;
    }
}
