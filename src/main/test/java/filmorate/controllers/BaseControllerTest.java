package filmorate.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.http.HttpClient;
import java.time.LocalDate;

public abstract class BaseControllerTest {

    public final String baseUrl = "http://localhost:8085";
    public final HttpClient client = HttpClient.newHttpClient();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonDeserializer<LocalDate>)
                    (json, type, context) -> LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
            .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonSerializer<LocalDate>)
                    (src, typeOfSrc, context) -> new com.google.gson.JsonPrimitive(src.toString()))
            .create();
}
