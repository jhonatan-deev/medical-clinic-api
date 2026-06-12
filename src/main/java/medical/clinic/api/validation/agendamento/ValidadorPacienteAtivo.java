package medical.clinic.api.validation.agendamento;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.repository.PacienteRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoConsulta {

    private final PacienteRepository pacienteRepository;

    public ValidadorPacienteAtivo(PacienteRepository pacienteRepository) {

        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public void validar(ConsultaRequestDTO dados) {

        var paciente = pacienteRepository.findById(dados.pacienteId()).orElseThrow(() -> new RegraNegocioException("Paciente não encontrado!"));

        if (!paciente.isAtivo()) {
            throw new RegraNegocioException("Paciente não está ativo!");
        }
    }
}
