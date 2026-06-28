package medical.clinic.api.model;

import jakarta.persistence.*;
import lombok.*;
import medical.clinic.api.enuns.Especialidade;

@Entity
@Table(name = "medicos")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id
    private Long id;

    @Setter
    private String nome;

    @Setter
    @Column(unique = true, nullable = false)
    private String crm;

    @Setter
    private String telefone;

    @Setter
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Setter
    @Embedded
    private Endereco endereco;

    @Setter
    private boolean ativo = true;

    @Setter
    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id") // PK = FK para usuarios.id
    private Usuario usuario;

    // Construtor para testes
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