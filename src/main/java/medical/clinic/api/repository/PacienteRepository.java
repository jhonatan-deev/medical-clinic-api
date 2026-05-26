package medical.clinic.api.repository;

import medical.clinic.api.dto.paciente.PacienteResponseDTO;
import medical.clinic.api.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Page<Paciente> findByAtivoTrue(Pageable pageable);
}
