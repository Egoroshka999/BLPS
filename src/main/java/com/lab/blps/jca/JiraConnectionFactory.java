package com.lab.blps.jca;

import jakarta.resource.ResourceException;

public interface JiraConnectionFactory {
    JiraConnection getConnection() throws ResourceException;
}
