package medical.clinic.api.validation.agendamento;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;

public interface ValidadorAgendamentoConsulta {
    void validar(ConsultaRequestDTO dados);

}
