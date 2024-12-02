package astahov.artem.tesk.task.controller;


import astahov.artem.tesk.task.service.TimeService;
import astahov.artem.tesk.task.service.dto.TimeEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final TimeService service;

    @GetMapping("/times")
    public List<TimeEntry> getTimes(){
        log.debug("Start processing get all TimeEntry");
        return service.getAll();
    }

}
