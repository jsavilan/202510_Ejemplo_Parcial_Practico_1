package co.edu.uniandes.dse.parcialprueba.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicoEspecialidadService.class)
public class MedicoEspecialidadServiceTest {

    @Autowired
    private MedicoEspecialidadService medicoEspecialidadService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicoEntity> medicoList = new ArrayList<>();
    private List<EspecialidadEntity> especialidadList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager()
            .createQuery("delete from EspecialidadEntity")
            .executeUpdate();
        entityManager.getEntityManager()
            .createQuery("delete from MedicoEntity")
            .executeUpdate();
    }


    private void insertData() {
        // Inserción de un MedicoEntity
        MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
        // Se persiste el médico
        entityManager.persist(medico);
        medicoList.add(medico);

        // Inserción de un EspecialidadEntity
        EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        // Se persiste la especialidad
        entityManager.persist(especialidad);
        especialidadList.add(especialidad);
    }


    @Test
    public void testAddEspecialidad_Success() throws EntityNotFoundException {
        // Se obtiene el médico y la especialidad insertados
        MedicoEntity medico = medicoList.get(0);
        EspecialidadEntity especialidad = especialidadList.get(0);

        // Se invoca el método a probar
        MedicoEntity result = medicoEspecialidadService.addEspecialidad(medico.getId(), especialidad.getId());

        // Se valida que el resultado no sea nulo
        assertNotNull(result, "El médico no debe ser nulo");
        // Se verifica que la especialidad se haya agregado a la lista de especialidades del médico
        assertTrue(result.getEspecialidades().contains(especialidad), "El médico debe contener la especialidad asociada");
    }

    @Test
    public void testAddEspecialidad_MedicoNotFound() {
        // Se define un ID de médico inexistente
        Long invalidMedicoId = 999L;
        // Se obtiene una especialidad existente
        EspecialidadEntity especialidad = especialidadList.get(0);

        // Se espera que se lance la excepción al invocar el método con un médico inexistente
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            medicoEspecialidadService.addEspecialidad(invalidMedicoId, especialidad.getId());
        });
        // Se valida que el mensaje de la excepción contenga la información del ID incorrecto
        assertTrue(exception.getMessage().contains("No se encontró el profesional con id = " + invalidMedicoId));
    }

    @Test
    public void testAddEspecialidad_EspecialidadNotFound() {
        // Se obtiene un médico existente
        MedicoEntity medico = medicoList.get(0);
        // Se define un ID de especialidad inexistente
        Long invalidEspecialidadId = 999L;

        // Se espera que se lance la excepción al invocar el método con una especialidad inexistente
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            medicoEspecialidadService.addEspecialidad(medico.getId(), invalidEspecialidadId);
        });
        // Se valida que el mensaje de la excepción contenga la información del ID incorrecto
        assertTrue(exception.getMessage().contains("No se encontró la especialidad con id = " + invalidEspecialidadId));
    }
}