package co.edu.uniandes.dse.parcialprueba.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public List<MedicoEntity> createMedicos(List<MedicoEntity> medicos) {
        
        log.info("Inicia proceso de creación de medicos");

        List<MedicoEntity> medicosLista = new ArrayList<>(); 
        
        for (MedicoEntity medico : medicos) {
            if (medico.getRegistroMedico() != null && medico.getRegistroMedico().startsWith("RM") ) {
                log.info("Creando medico con nombre: " + medico.getNombre());
                medicosLista.add(medicoRepository.save(medico));
            } else {
                log.error("No se puede crear el medico con nombre: " + medico.getNombre() + " porque el registro medico no cumple con el formato RMXXXX");
            }
        }
        
        return medicosLista;
    }

}
