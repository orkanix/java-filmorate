package filmorate.controllers;

import filmorate.model.Rating;
import filmorate.service.MpaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Rating> getMpas() {
        return mpaService.getRatings();
    }

    @GetMapping("/{mpaId}")
    public Rating getMpa(@PathVariable long mpaId) {
        return mpaService.getMpa(mpaId);
    }
}
