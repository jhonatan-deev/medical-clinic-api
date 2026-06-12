package medical.clinic.api.validation.agendamento;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.model.Medico;
import medical.clinic.api.repository.ConsultaRepository;
import medical.clinic.api.repository.MedicoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidadorMedicoDisponivel implements ValidadorAgendamentoConsulta {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;

    public ValidadorMedicoDisponivel(ConsultaRepository consultaRepository, MedicoRepository medicoRepository) {

        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
    }

    @Override
    public void validar(ConsultaRequestDTO dados) {

        if (dados.medicoId() != null) {

            boolean medicoOcupado = consultaRepository.existsByMedicoIdAndData(dados.medicoId(), dados.data());

            if (medicoOcupado) {
                throw new RegraNegocioException("Médico não está disponível neste horário!");
            }

            return;
        }

        List<Medico> medicosDisponiveis;

        if (dados.especialidade() != null) {

            medicosDisponiveis = medicoRepository.findMedicosAtivosDisponiveisNaDataPorEspecialidade(dados.data(), dados.especialidade());

        } else {

            medicosDisponiveis = medicoRepository.findMedicosAtivosDisponiveisNaData(dados.data());
        }

        if (medicosDisponiveis.isEmpty()) {

            String mensagem = "Nenhum médico disponível neste horário!";

            if (dados.especialidade() != null) {
                mensagem += " para a especialidade " + dados.especialidade();
            }

            throw new RegraNegocioException(mensagem);
        }
    }
}