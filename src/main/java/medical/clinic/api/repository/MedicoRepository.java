package medical.clinic.api.repository;

import medical.clinic.api.enuns.Especialidade;
import medical.clinic.api.model.Medico;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable pageable);
    @Query("""
    SELECT m FROM Medico m WHERE m.ativo = true AND NOT EXISTS(SELECT c FROM Consulta c WHERE c.medico = m AND c.data = :dataHora)""")
    List<Medico> findMedicosAtivosDisponiveisNaData(
            @Param("dataHora") LocalDateTime dataHora);

    @Query("""
    SELECT m FROM Medico m 
    WHERE m.ativo = true 
    AND m.especialidade = :especialidade
    AND NOT EXISTS(SELECT c FROM Consulta c WHERE c.medico = m AND c.data = :dataHora)
    """)
    List<Medico> findMedicosAtivosDisponiveisNaDataPorEspecialidade(
            @Param("dataHora") LocalDateTime dataHora,
            @Param("especialidade") Especialidade especialidade
    );
    boolean existsByEmail(String email);
    boolean existsByCrm(String crm);
}
