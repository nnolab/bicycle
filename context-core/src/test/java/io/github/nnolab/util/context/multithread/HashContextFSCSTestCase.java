package io.github.nnolab.util.context.multithread;

import io.github.nnolab.util.context.impl.HashContext;
import io.github.nnolab.util.context.shells.FullSyncContextShell;

/**
 * Test case for {@link HashContext} in {@link FullSyncContextShell}.
 *
 * @author nnolab
 */
public class HashContextFSCSTestCase
        extends AbstractFullSyncContextShellTestCase<HashContext>
        implements HashContextProvider {
}
