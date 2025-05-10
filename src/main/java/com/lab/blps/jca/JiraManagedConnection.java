package com.lab.blps.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JiraManagedConnection implements ManagedConnection {
    private final CloseableHttpClient client;
    private final String url, user, pass;
    private final List<ConnectionEventListener> listeners = new ArrayList<>();
    private PrintWriter logWriter;

    public JiraManagedConnection(CloseableHttpClient client, String url, String user, String pass) {
        this.client = client;
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cxReqInfo) throws ResourceException {
        JiraConnectionImpl conn = new JiraConnectionImpl(client, url, user, pass);
        return conn;
    }

    @Override public void addConnectionEventListener(ConnectionEventListener listener) { listeners.add(listener); }
    @Override public void removeConnectionEventListener(ConnectionEventListener listener) { listeners.remove(listener); }
    @Override public void cleanup() throws ResourceException { /* no-op */ }

    @Override
    public void associateConnection(Object o) throws ResourceException {

    }

    @Override public void destroy() throws ResourceException { try { client.close(); } catch (IOException ignored) {} }
    @Override public XAResource getXAResource() throws ResourceException { return null; }
    @Override public LocalTransaction getLocalTransaction() throws ResourceException { return null; }
    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new ManagedConnectionMetaData() {
            @Override public String getEISProductName() { return "JiraAdapter"; }
            @Override public String getEISProductVersion() { return "1.0"; }
            @Override public int getMaxConnections() { return 1; }
            @Override public String getUserName() { return user; }
        };
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws ResourceException {
        this.logWriter = printWriter;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }
}
