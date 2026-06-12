package medical.clinic.api.repository;

import medical.clinic.api.model.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
   boolean existsByPacienteIdAndDataBetween(
           Long pacienteId,
           LocalDateTime inicio,
           LocalDateTime fim
   );
   Page<Consulta> findByAtivaTrue(Pageable pageable);

   boolean existsByMedicoIdAndData(Long medicoId, LocalDateTime data);
}
