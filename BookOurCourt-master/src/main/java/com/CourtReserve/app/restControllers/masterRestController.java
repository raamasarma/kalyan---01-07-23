package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class masterRestController {
    private final CourtRepository courtRepository;
    private final DayTypeRepository dayTypeRepository;
    private final SpecialDatesRepository specialDatesRepository;
    private final GameRepository gameRepository;
    private final SlotRepository slotRepository;

    public masterRestController(CourtRepository courtRepository,
                                DayTypeRepository dayTypeRepository,
                                SpecialDatesRepository specialDatesRepository,
                                GameRepository gameRepository,
                                SlotRepository slotRepository) {
        this.courtRepository = courtRepository;
        this.dayTypeRepository = dayTypeRepository;
        this.specialDatesRepository = specialDatesRepository;
        this.gameRepository = gameRepository;
        this.slotRepository = slotRepository;
    }

    @PostMapping("/removeCourt")
    public ResponseEntity removeCourt(@RequestParam Map body){
        courtRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    //removeDayType

    @PostMapping("/removeDayType")
    public ResponseEntity removeDayType(@RequestParam Map body){
        dayTypeRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    @PostMapping("/removeSpecialDates")
    public ResponseEntity removeSpecialDates(@RequestParam Map body){
        specialDatesRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    @PostMapping("/removeGames")
    public ResponseEntity removeGames(@RequestParam Map body){
        gameRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    @PostMapping("/removeSlot")
    public ResponseEntity removeSlot(@RequestParam Map body){
        slotRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
}
