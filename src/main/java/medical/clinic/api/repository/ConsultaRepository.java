package medical.clinic.api.repository;

import medical.clinic.api.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
   boolean existsByPacienteIdAndData(Long idOfPaciente, LocalDate dataHora );

   boolean existsByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);
}
