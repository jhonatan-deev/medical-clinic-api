package medical.clinic.api.validation.atualizacao;

import medical.clinic.api.dto.consulta.ConsultaUpdateDTO;
import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.model.Consulta;
import medical.clinic.api.repository.ConsultaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorAtualizacaoConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidadorAtualizacaoConsulta(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public void validar(Long consultaId, Consulta consulta, ConsultaUpdateDTO dto) {
        validarAntecedencia(dto.data());
        validarPacienteSemConsultaNoDia(consultaId, consulta, dto.data());
    }

    private void validarAntecedencia(LocalDateTime data) {
        LocalDateTime horaLimite = LocalDateTime.now().plusMinutes(30);

        if (data.isBefore(horaLimite)) {
            throw new RegraNegocioException("A consulta deve ser atualizada com pelo menos 30 minutos de antecedência.");
        }
    }

    private void validarPacienteSemConsultaNoDia(Long consultaId, Consulta consulta, LocalDateTime data) {

        LocalDateTime inicio = data.toLocalDate().atStartOfDay();
        LocalDateTime fim = data.toLocalDate().atTime(23, 59, 59);

        boolean existeConsulta = consultaRepository.existsByPacienteIdAndDataBetweenAndIdNot(consulta.getPaciente().getId(), inicio, fim, consultaId);

        if (existeConsulta) {
            throw new RegraNegocioException("O paciente já possui uma consulta cadastrada nesse dia.");
        }
    }
}