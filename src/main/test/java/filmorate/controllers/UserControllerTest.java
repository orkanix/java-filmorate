package filmorate.controllers;

import model.User;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest extends BaseControllerTest {

    private final String url = baseUrl + "/users";

    @Test
    public void testInvalidEmail() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("invalidEmailWithoutAtSymbol")
                .login("validLogin")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }

    @Test
    public void testValidUserEmail() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("valid.email@example.com")  // валидный email с '@'
                .login("validLogin")
                .birthday(LocalDate.of(1990, 5, 20)) // дата в прошлом
                .build();

        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testInvalidLoginWithSpaces() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("valid@example.com")
                .login("invalid login") // пробел в логине
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }

    @Test
    public void testValidUserLogin() throws Exception {
        User user = User.builder()
                .id(2L)
                .email("user@example.com")
                .login("validLoginNoSpaces")  // логин без пробелов
                .birthday(LocalDate.of(1985, 8, 15))
                .build();

        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testInvalidBirthdayInFuture() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("valid@example.com")
                .login("validLogin")
                .birthday(LocalDate.now().plusDays(1)) // дата в будущем
                .build();

        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }

    @Test
    public void testValidUserBirthday() throws Exception {
        User user = User.builder()
                .id(3L)
                .email("user123@example.com")
                .login("userLogin")
                .birthday(LocalDate.now().minusDays(1))  // дата рождения в прошлом
                .build();

        String json = gson.toJson(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testEmptyRequestBodyForUser() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))  // пустое тело запроса
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

}
