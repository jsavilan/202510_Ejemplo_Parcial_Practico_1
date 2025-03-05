package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class EspecialidadService {
    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Transactional
    public EspecialidadEntity createEspecialidad(EspecialidadEntity especialidadEntity) {
        log.info("Inicia proceso de creación de especialidad");

        EspecialidadEntity entidadEspecialidad;

        if (especialidadEntity.getNombre() == null || especialidadEntity.getNombre().length() < 10) {
            log.error("No se puede crear la especialidad porque el nombre es nulo o no cumple la longitud requerida");
            entidadEspecialidad = null;
        } else {
            log.info("Creando especialidad con nombre: " + especialidadEntity.getNombre());
            entidadEspecialidad = especialidadRepository.save(especialidadEntity);
        }

        return entidadEspecialidad;
    }

}
