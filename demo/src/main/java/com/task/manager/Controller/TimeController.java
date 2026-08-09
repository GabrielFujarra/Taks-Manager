package com.task.manager.Controller;

import com.task.manager.Dto.request.TimeRequestDto;
import com.task.manager.Dto.response.TimeResponseDto;
import com.task.manager.Service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/time")
public class TimeController {

    private final TimeService timeService;

    @GetMapping
    public ResponseEntity<List<TimeResponseDto>> listarTimes() {
        List<TimeResponseDto> times = timeService.listarTimes();
        return ResponseEntity.ok(times);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<TimeResponseDto> buscarTimePorNome (@PathVariable String nome) {
        TimeResponseDto time = timeService.buscarTimePorNome(nome);
        return ResponseEntity.ok(time);
    }

    @PostMapping
    public ResponseEntity<Void> criarTime (@RequestBody TimeRequestDto timeRequest) {
        timeService.criarTime(timeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{nome}")
    public ResponseEntity<Void> deletarTime (@PathVariable String nome) {
        timeService.deletarTime(nome);
        return ResponseEntity.noContent().build();
    }
}
