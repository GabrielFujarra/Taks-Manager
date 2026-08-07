package com.task.manager.Service;


import com.task.manager.DataBase.Model.TimeEntity;
import com.task.manager.DataBase.Repository.TimeRepository;
import com.task.manager.Dto.request.TimeRequestDto;
import com.task.manager.Dto.response.TimeResponseDto;
import com.task.manager.Exception.BadRequestException;
import com.task.manager.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository ;

    public void criarTime (TimeRequestDto timeDto){

       boolean time = timeRepository.existsByNome(timeDto.nome());

        if (time) {
            throw new BadRequestException("Time já existe");
        }

        timeRepository.save(TimeEntity.builder()
                .nome(timeDto.nome())
                .build());
    }

    public List<TimeResponseDto> listarTimes() {

        return timeRepository.findAll()
                .stream()
                .map(time -> new TimeResponseDto(time.getNome(), time.getId()))
                .toList();
    }

    public TimeResponseDto buscarTimePorNome(String nome){

        TimeEntity time = timeRepository.findByNome(nome)
                .orElse(null);

        if (time != null){
            return new TimeResponseDto(time.getNome(), time.getId());
        }

        throw new NotFoundException("Time não encontrado");
    }

    public void deletarTime(String nome){

        TimeEntity time = timeRepository.findByNome(nome)
                .orElseThrow(() -> new NotFoundException("Time não encontrado"));

        timeRepository.delete(time);
    }
}
