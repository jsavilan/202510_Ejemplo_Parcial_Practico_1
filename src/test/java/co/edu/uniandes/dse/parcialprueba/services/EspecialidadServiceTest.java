package co.edu.uniandes.dse.parcialprueba.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EspecialidadService.class)
public class EspecialidadServiceTest {

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

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
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
            entityManager.persist(especialidad);
            especialidadList.add(especialidad);
        }
    }

    @Test
    public void testCreateEspecialidad_Success() {
        EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        // Se asigna una descripción válida (al menos 10 caracteres)
        especialidad.setDescripcion("Descripción válida");
        // Se asigna un nombre válido (si es necesario para la lógica)
        especialidad.setNombre("Especialidad Válida");

        // Se invoca el método a probar
        EspecialidadEntity result = especialidadService.createEspecialidad(especialidad);

        // Se valida que la especialidad se haya creado correctamente
        assertNotNull(result, "La especialidad debe crearse correctamente");
        EspecialidadEntity persisted = entityManager.find(EspecialidadEntity.class, result.getId());
        assertNotNull(persisted, "La especialidad debe estar persistida en la base de datos");
    }

    @Test
    public void testCreateEspecialidad_Invalid_ReturnsNull() {
        EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        // Se asigna una descripción inválida (menos de 10 caracteres)
        especialidad.setDescripcion("Corta");
        // Se asigna un nombre válido (si es necesario para la lógica)
        especialidad.setNombre("Especialidad Válida");

        // Se invoca el método a probar
        EspecialidadEntity result = especialidadService.createEspecialidad(especialidad);

        // Se espera que el método retorne null al no cumplir la regla de negocio
        assertNull(result, "La especialidad debe retornar null cuando la descripción no cumple con el mínimo de 10 caracteres");
    }
}
