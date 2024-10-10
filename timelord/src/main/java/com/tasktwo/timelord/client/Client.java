package com.tasktwo.timelord.client;

import java.net.http.HttpClient;

public class Client {
    private static final String BASE_URL = "http://localhost:8080/v1/users";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    public void userControl() {
        while (true) {

        }
    }
}
