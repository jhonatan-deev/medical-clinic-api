package medical.clinic.api.dto.medico;

import medical.clinic.api.enuns.Especialidade;
import medical.clinic.api.model.Endereco;

public record MedicoResponseDTO(
        Long id,
        String nome,
        String crm,
        Especialidade especialidade,
        boolean ativo,
        Endereco endereco
){
}
