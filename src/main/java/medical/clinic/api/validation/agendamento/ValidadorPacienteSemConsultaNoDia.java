package medical.clinic.api.validation.agendamento;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.repository.ConsultaRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteSemConsultaNoDia implements ValidadorAgendamentoConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidadorPacienteSemConsultaNoDia(ConsultaRepository consultaRepository) {

        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(ConsultaRequestDTO dados) {

        var inicio = dados.data().toLocalDate().atStartOfDay();

        var fim = dados.data().toLocalDate().atTime(23, 59, 59);

        boolean existeConsulta = consultaRepository.existsByPacienteIdAndDataBetween(dados.pacienteId(), inicio, fim);

        if (existeConsulta) {
            throw new RegraNegocioException("Já existe uma consulta cadastrada nesse dia, tente marcar em outra data!");
        }
    }
}