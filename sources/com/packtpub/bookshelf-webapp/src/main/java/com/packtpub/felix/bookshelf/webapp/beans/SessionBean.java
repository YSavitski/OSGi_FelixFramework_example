package com.packtpub.felix.bookshelf.webapp.beans;

import com.packtpub.felix.bookshelf.service.api.BookShelfService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import javax.servlet.ServletContext;

public class SessionBean {
    static final String OSGI_BUNDLECONTEXT = "osgi-bundlecontext";

    private BundleContext context;

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void initialize(ServletContext servletContext){
        this.context = (BundleContext) servletContext.getAttribute(OSGI_BUNDLECONTEXT);
    }

    public BookShelfService getService(){
        ServiceReference ref = context.getServiceReference(BookShelfService.class.getName());
        BookShelfService bookShelfService = (BookShelfService) context.getService(ref);
        return bookShelfService;
    }

    public boolean sessionIsValid(){
        return getService().sessionIsValid(getSessionId());
    }
}
