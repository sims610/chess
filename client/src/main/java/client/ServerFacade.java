package client;

import com.google.gson.Gson;
import model.requestresult.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String host, int port) {
        serverUrl = String.format(Locale.getDefault(), "http://%s:%d", host, port);
        authToken = null;
    }

    public void get(String host, int port, String urlPath) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .GET()
                .build();

    }

    RegisterResult register(RegisterRequest registerRequest) {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        RegisterResult result = handleResponse(response, RegisterResult.class);
        if (result != null) {
            authToken = result.authToken();
        }
        return result;
    }

    LoginResult login(LoginRequest loginRequest) {
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        LoginResult result = handleResponse(response, LoginResult.class);
        if (result != null) {
            authToken = result.authToken();
        }
        return result;
    }

    LogoutResult logout(LogoutRequest logoutRequest) {
        var request = buildRequest("DELETE", "/session", logoutRequest);
        var response = sendRequest(request);
        authToken = null;
        return handleResponse(response, LogoutResult.class);
    }

    CreateResult create(CreateRequest createRequest) {
        var request = buildRequest("POST", "/game", createRequest);
        var response = sendRequest(request);
        return handleResponse(response, CreateResult.class);
    }

    ListResult listGames(ListRequest listRequest) {
        var request = buildRequest("GET", "/game", listRequest);
        var response = sendRequest(request);
        return handleResponse(response, ListResult.class);
    }

    JoinResult joinGame(JoinRequest joinRequest) {
        var request = buildRequest("PUT", "/game", joinRequest);
        var response = sendRequest(request);
        return handleResponse(response, JoinResult.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .timeout(java.time.Duration.ofMillis(500000000))
                .method(method, makeRequestBody(body));
        if (authToken != null) {
            request.header("Authorization", authToken);
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Didn't work");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new RuntimeException("Didn't work");
            }
            throw new RuntimeException("Status code failure");
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
