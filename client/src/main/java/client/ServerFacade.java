package client;

import model.requestresult.LoginRequest;
import model.requestresult.LoginResult;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Locale;

public class ServerFacade {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void doGet(String host, int port, String urlPath) throws URISyntaxException, IOException, URISyntaxException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .GET()
                .build();

    }

    LoginResult login(LoginRequest loginRequest) {return null;}
}
