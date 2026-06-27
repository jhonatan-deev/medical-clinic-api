package medical.clinic.api.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.clinic.api.dto.medico.MedicoRequestDTO;
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
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    //CONSTRUTOR PARA TESTE
    public Medico(
            String nome,
            String crm,
            String telefone,
            Especialidade especialidade,
            Endereco endereco,
            boolean ativo
    ) {
        this.nome = nome;
        this.crm = crm;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.endereco = endereco;
        this.ativo = ativo;
    }
}