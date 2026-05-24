package medical.clinic.api.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.clinic.api.dto.MedicoRequestDTO;
import medical.clinic.api.enuns.Especialidade;

@Entity
@Table(name = "medicos")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String nome;
    @Setter
    @Column(unique = true, nullable = false)
    private String email;
    @Setter
    @Column(unique = true, nullable = false)
    private String crm;
    @Setter
    @Column(unique = true, nullable = false)
    private String telefone;
    @Setter
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    @Setter
    @Embedded
    private Endereco endereco;
    @Setter
    @Column(nullable = false)
    private boolean ativo = true;

    public Medico(MedicoRequestDTO dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.crm = dto.crm();
        this.telefone = dto.telefone();
        this.especialidade = dto.especialidade();
        this.endereco = new Endereco(dto.endereco());
        this.ativo = true;
    }
}