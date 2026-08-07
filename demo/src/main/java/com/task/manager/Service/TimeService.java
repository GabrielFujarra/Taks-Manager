package com.task.manager.Service;


import com.task.manager.DataBase.Model.TimeEntity;
import com.task.manager.DataBase.Repository.TimeRepository;
import com.task.manager.Dto.TimeDto;
import com.task.manager.Excepiton.BadRequestExcepiton;
import com.task.manager.Excepiton.NotFoundExcepiton;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository ;

    public void CriarTime (TimeDto timeDto) throws BadRequestExcepiton {

        TimeEntity time = timeRepository.findByNome(timeDto.nome())
                .orElse(null);

        if (time != null) {
            throw new BadRequestExcepiton("Time já existe");
        }

        timeRepository.save(TimeEntity.builder()
                .nome(timeDto.nome())
                .build());
    }

    public List<TimeEntity> ListarTimes() {

        return timeRepository.findAll();
    }

    public TimeEntity BuscarTimePorNome(String nome) throws NotFoundExcepiton {

        TimeEntity time = timeRepository.findByNome(nome)
                .orElse(null);

        if (time != null){
            return time ;
        }

        throw new NotFoundExcepiton("Time não encontrado");
    }

    public void DeletarTime(Long id) throws NotFoundExcepiton {

        TimeEntity time = timeRepository.findById(id)
                .orElseThrow(() -> new NotFoundExcepiton("Time não encontrado"));

        timeRepository.delete(time);
    }
}
