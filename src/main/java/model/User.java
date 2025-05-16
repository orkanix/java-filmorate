package model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
@Data
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
