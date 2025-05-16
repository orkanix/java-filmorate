package model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
@Data
@EqualsAndHashCode(of = "id")
public class Film {

    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private Integer duration;
}
