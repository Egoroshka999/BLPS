package com.lab.blps.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Set;

@Getter
@Setter
//@EqualsAndHashCode(of = {"url", "username", "password"})
@ConnectionDefinition(
        connectionFactory = JiraConnectionFactory.class,
        connectionFactoryImpl = JiraConnectionFactoryImpl.class,
        connection = JiraConnection.class,
        connectionImpl = JiraConnectionImpl.class
)
public class JiraManagedConnectionFactory implements ManagedConnectionFactory, ResourceAdapterAssociation {
    private String url;
    private String username;
    private String password;
    private ResourceAdapter ra;
    private PrintWriter logWriter;

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        return new JiraConnectionFactoryImpl(this, cxManager);
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        return new JiraConnectionFactoryImpl(this, null);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxReqInfo) throws ResourceException {
        return new JiraManagedConnection(
                HttpClients.createDefault(), url, username, password
        );
    }

    @Override
    public ManagedConnection matchManagedConnections(Set connections, Subject subject, ConnectionRequestInfo cxReqInfo) throws ResourceException {
        for (Object mc : connections) {
            if (mc instanceof JiraManagedConnection) {
                return (ManagedConnection) mc;
            }
        }
        return null;
    }

    @Override public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out;
    }
    @Override public PrintWriter getLogWriter() throws ResourceException {
        return this.logWriter;
    }
    @Override public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
        if (this.ra != null && !(ra instanceof JiraResourceAdapter)) {
            throw new ResourceException("Invalid resource adapter provided");
        }
        this.ra = (JiraResourceAdapter) ra;
    }
    @Override public ResourceAdapter getResourceAdapter() { return this.ra; }
}
