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
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    //Construtor para testes:
    public Paciente(
            String nome,
            String telefone,
            String cpf,
            Endereco endereco,
            boolean ativo
    ) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.endereco = endereco;
        this.ativo = ativo;
    }
}