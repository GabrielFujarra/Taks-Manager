package com.task.manager.demo.Service;


import com.task.manager.demo.DataBase.Model.TimeEntity;
import com.task.manager.demo.DataBase.Repository.TimeRepository;
import com.task.manager.demo.Dto.TimeDto;
import com.task.manager.demo.Excepiton.BadRequestExcepiton;
import com.task.manager.demo.Excepiton.NotFoundExcepiton;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.LongFunction;

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
