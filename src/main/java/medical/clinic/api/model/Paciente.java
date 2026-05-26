package medical.clinic.api.model;

import jakarta.persistence.*;
import lombok.*;
import medical.clinic.api.dto.paciente.PacienteRequestDTO;


@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String nome;
    @Setter
    @Column(unique = true, nullable = false)
    private String email;
    @Setter
    @Column(unique = true, nullable = false)
    private String telefone;
    @Setter
    @Column(unique = true, nullable = false)
    private String cpf;
    @Setter
    @Embedded
    private Endereco endereco;
    @Setter
    @Column(nullable = false)
    private boolean ativo = true;

}