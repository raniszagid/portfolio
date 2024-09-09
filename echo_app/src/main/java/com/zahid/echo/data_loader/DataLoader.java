package com.zahid.echo.data_loader;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DataLoader {
    public final String URL;
    public DataLoader(String URL) {
        this.URL = URL;
    }
    public JSONObject getJSONObject() throws IOException, InterruptedException {
        return new JSONObject(getResponse().body());
    }
    public String getRequestText() {
        HttpRequest request = getRequest();
        return "Запрос: " + request.method() + " " + request.uri() + "\n";
    }
    public String getResponseText() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse();
        return "Ответ: " + response.statusCode() + " " + response.body() + "\n";
    }
    private HttpRequest getRequest() {
        return HttpRequest.newBuilder(URI.create(URL)).GET().build();
    }
    private HttpResponse<String> getResponse() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        return client.send(getRequest(), HttpResponse.BodyHandlers.ofString());
    }

}
