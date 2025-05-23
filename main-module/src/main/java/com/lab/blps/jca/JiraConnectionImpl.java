package com.lab.blps.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.ConnectionMetaData;
import jakarta.resource.cci.Interaction;
import jakarta.resource.cci.LocalTransaction;
import jakarta.resource.cci.ResultSetInfo;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;

public class JiraConnectionImpl implements JiraConnection {
    private final CloseableHttpClient httpClient;
    private final String url, user, pass;
    private final ObjectMapper jackson = new ObjectMapper();

    public JiraConnectionImpl(CloseableHttpClient client, String url, String user, String pass) {
        this.httpClient = client;
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public String createIssue(String projectKey, String summary, String description) throws Exception {
        HttpPost post = new HttpPost(url + "/rest/api/2/issue");
        post.addHeader("Content-Type", "application/json");
        String auth = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
        post.addHeader("Authorization", "Basic " + auth);
        Map<String, Object> payload = Map.of("fields", Map.of(
                "project", Map.of("key", projectKey),
                "summary", summary,
                "description", description,
                "issuetype", Map.of("name", "Task")
        ));
        post.setEntity(new StringEntity(jackson.writeValueAsString(payload), ContentType.APPLICATION_JSON));
        try (var resp = httpClient.execute(post)) {
            var node = jackson.readTree(resp.getEntity().getContent());
            return node.get("key").asText();
        }
    }

    @Override
    public void transitionIssue(String issueKey, String transitionName) throws Exception {
        HttpGet get = new HttpGet(url + "/rest/api/2/issue/" + issueKey + "/transitions");
        String auth = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
        get.addHeader("Authorization", "Basic " + auth);
        try (var resp = httpClient.execute(get)) {
            var root = jackson.readTree(resp.getEntity().getContent());
            for (var t : root.get("transitions")) {
                if (t.get("name").asText().equalsIgnoreCase(transitionName)) {
                    String id = t.get("id").asText();
                    HttpPost post = new HttpPost(url + "/rest/api/2/issue/" + issueKey + "/transitions");
                    post.addHeader("Content-Type", "application/json");
                    post.addHeader("Authorization", "Basic " + auth);
                    Map<String,Object> body = Map.of("transition", Map.of("id", id));
                    post.setEntity(new StringEntity(jackson.writeValueAsString(body), ContentType.APPLICATION_JSON));
                    httpClient.execute(post).close();
                    return;
                }
            }
            throw new RuntimeException("Transition '" + transitionName + "' not found");
        }
    }

    @Override
    public Interaction createInteraction() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    public ResultSetInfo getResultSetInfo() throws ResourceException {
        return null;
    }

    @Override
    public void close() throws ResourceException {

    }
}