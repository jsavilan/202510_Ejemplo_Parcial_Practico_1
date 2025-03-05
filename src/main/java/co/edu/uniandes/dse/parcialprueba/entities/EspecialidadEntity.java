package co.edu.uniandes.dse.parcialprueba.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class EspecialidadEntity extends BaseEntity {

    private String nombre;
    private String descripcion;

    @ManyToMany(mappedBy = "especialidades")
    private List<MedicoEntity> medicos;

}
