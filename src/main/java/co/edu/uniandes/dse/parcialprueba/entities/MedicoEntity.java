package co.edu.uniandes.dse.parcialprueba.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class MedicoEntity extends BaseEntity{

    private String nombre;
    private String apellido;
    private String registroMedico;

    @ManyToMany
    private List<EspecialidadEntity> especialidades;

}
