package co.edu.uniandes.dse.parcialprueba.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class MedicoEspecialidadService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Transactional
    public MedicoEntity addEspecialidad(Long medicoId, Long especialidadId) throws EntityNotFoundException{
        
        log.info("Inicia proceso de asociarle una especialidad al medico con id = {0}", medicoId);

        Optional<MedicoEntity> medicoEntity = medicoRepository.findById(medicoId);
        Optional<EspecialidadEntity> especialidadEntity = especialidadRepository.findById(especialidadId);

        if (medicoEntity.isEmpty()){
            throw new EntityNotFoundException("No se encontró el profesional con id = " + medicoId);
        }

        if (especialidadEntity.isEmpty()){
            throw new EntityNotFoundException("No se encontró la especialidad con id = " + especialidadId);
        }

        medicoEntity.get().getEspecialidades().add(especialidadEntity.get());

        log.info("Termina proceso de asociarle una especialidad al medico con id = {0}", medicoId);

        return medicoEntity.get();
    }

}
