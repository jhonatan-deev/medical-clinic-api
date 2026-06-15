package medical.clinic.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import medical.clinic.api.enuns.MotivoCancelamento;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    @Setter
    private Medico medico;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    @Setter
    private Paciente paciente;
    @Setter
    private LocalDateTime data;
    @Enumerated(EnumType.STRING)
    @Setter
    private MotivoCancelamento motivoCancelamento;
    @Setter
    private boolean ativa = true;
    @Setter
    private LocalDateTime dataCancelamento;

    public Consulta(
            Medico medico,
            Paciente paciente,
            LocalDateTime data
    ) {
        this.medico = medico;
        this.paciente = paciente;
        this.data = data;
    }
}
