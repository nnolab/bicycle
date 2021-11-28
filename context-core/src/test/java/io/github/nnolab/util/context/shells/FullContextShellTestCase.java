package io.github.nnolab.util.context.shells;

import io.github.nnolab.util.context.Context;

import java.util.function.Function;

/**
 * Test case for {@link FullContextShell}.
 *
 * @author nnolab
 */
public class FullContextShellTestCase extends AbstractContextShellTestCase<FullContextShell> {

    @Override
    protected Function<Context, FullContextShell> getShellConstructor() {
        return FullContextShell::new;
    }
}
