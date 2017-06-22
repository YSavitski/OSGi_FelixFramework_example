package com.packtpub.felix.bookshelf.service.activator;

import com.packtpub.felix.bookshelf.service.tui.BookshelfServiceProxy;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;

public class BookshelfTuiActivator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", BookshelfServiceProxy.SCOPE);
        props.put("osgi.command.function", BookshelfServiceProxy.FUNCTIONS);

        bundleContext.registerService(
                BookshelfServiceProxy.class.getName(),
                new BookshelfServiceProxy(bundleContext),
                props);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
