package medical.clinic.api.repository;

import medical.clinic.api.model.Medico;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByCrm(String crm);
}
