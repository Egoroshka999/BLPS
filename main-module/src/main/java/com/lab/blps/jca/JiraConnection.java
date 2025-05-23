package com.lab.blps.jca;

import jakarta.resource.cci.Connection;

public interface JiraConnection extends Connection {
    String createIssue(String projectKey, String summary, String description) throws Exception;
    void transitionIssue(String issueKey, String transitionName) throws Exception;
}
