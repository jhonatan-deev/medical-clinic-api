package medical.clinic.api.validation.agendamento;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.repository.MedicoRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoConsulta {

    private final MedicoRepository medicoRepository;

    public ValidadorMedicoAtivo(MedicoRepository medicoRepository) {

        this.medicoRepository = medicoRepository;
    }

    @Override
    public void validar(ConsultaRequestDTO dados) {

        if (dados.medicoId() == null) {
            return;
        }

        var medico = medicoRepository.findById(dados.medicoId()).orElseThrow(() -> new RegraNegocioException("Médico não encontrado!"));

        if (!medico.isAtivo()) {
            throw new RegraNegocioException("Médico não está ativo!");
        }
    }
}