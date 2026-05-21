package medical.clinic.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import medical.clinic.api.dto.Endereco;
import medical.clinic.api.enuns.Especialidade;

@Entity
@Table(name = "medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    @Email(message = "Email inválido")
    private String email;
    @Column(unique = true, nullable = false)
    private String crm;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    @Embedded
    private Endereco endereco;
}
