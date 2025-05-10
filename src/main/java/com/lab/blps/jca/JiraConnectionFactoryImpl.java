package com.lab.blps.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;

public class JiraConnectionFactoryImpl implements JiraConnectionFactory {
    private final JiraManagedConnectionFactory mcf;
    private final ConnectionManager cm;

    public JiraConnectionFactoryImpl(JiraManagedConnectionFactory mcf, ConnectionManager cm) {
        this.mcf = mcf;
        this.cm = cm;
    }

    @Override
    public JiraConnection getConnection() throws ResourceException {
        if (cm != null) {
            return (JiraConnection) cm.allocateConnection(mcf, null);
        } else {
            var mc = (JiraManagedConnection) mcf.createManagedConnection(null, null);
            return (JiraConnection) mc.getConnection(null, null);
        }
    }
}
