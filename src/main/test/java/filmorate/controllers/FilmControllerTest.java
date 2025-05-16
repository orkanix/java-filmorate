package filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Film;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest extends BaseControllerTest {

    private final String url = baseUrl + "/films";

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonDeserializer<LocalDate>)
                    (json, type, context) -> LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonSerializer<LocalDate>)
                    (src, typeOfSrc, context) -> new com.google.gson.JsonPrimitive(src.toString()))
            .create();

    @Test
    public void testEmptyRequestBody() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }


    @Test
    public void testInvalidDescriptionTooLong() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("Тестовый фильм")
                .description("A".repeat(201))
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }

    @Test
    public void testValidDescriptionTooLong() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("Тестовый фильм")
                .description("A".repeat(200))
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testInvalidNameEmpty() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("")
                .description("Описание в норме")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }

    @Test
    public void testValidNameNotEmpty() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("Нормальное имя")
                .description("Описание в норме")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testInvalidReleaseDateTooEarly() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("Тестовый фильм")
                .description("Описание в норме")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(100)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }

    @Test
    public void testValidReleaseDate() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("Тестовый фильм")
                .description("Описание в норме")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(100)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void testInvalidDurationNegative() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("Тестовый фильм")
                .description("Описание в норме")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(-10)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, response.statusCode());
    }

    @Test
    public void testValidDurationPositive() throws Exception {
        Film film = Film.builder()
                .id(1L)
                .name("Тестовый фильм")
                .description("Описание в норме")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();

        String json = gson.toJson(film);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

}
