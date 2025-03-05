package co.edu.uniandes.dse.parcialprueba.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;

import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicoService.class)
public class MedicoServiceTest {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicoEntity> medicoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
            entityManager.persist(medico);
            medicoList.add(medico);
        }
    }


    @Test
    public void testCreateMedicos_Success() {
        List<MedicoEntity> medicos = new ArrayList<>();

        // Se crea un médico con registro válido (inicia con "RM")
        MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
        medico.setRegistroMedico("RM1234");
        medico.setNombre("Dr. Correcto");
        medicos.add(medico);

        // Se invoca el método a probar
        List<MedicoEntity> result = medicoService.createMedicos(medicos);

        // Se valida que se haya creado correctamente
        assertNotNull(result, "El resultado no debe ser nulo para médicos válidos");
        assertEquals(1, result.size(), "Debe crearse un único médico válido");

        MedicoEntity persisted = entityManager.find(MedicoEntity.class, result.get(0).getId());
        assertNotNull(persisted, "El médico debe estar persistido en la base de datos");
    }


    @Test
    public void testCreateMedicos_Invalid_ReturnsNull() {
        List<MedicoEntity> medicos = new ArrayList<>();

        // Se crea un médico con registro inválido (no inicia con "RM")
        MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
        medico.setRegistroMedico("XX1234");  // Violación de la regla de negocio
        medico.setNombre("Dr. Erróneo");
        medicos.add(medico);

        // Se invoca el método a probar
        List<MedicoEntity> result = medicoService.createMedicos(medicos);

        // Se espera que el método retorne null al detectar la violación de la regla
        assertNull(result, "El resultado debe ser nulo cuando se viola la regla de negocio");
    }
}