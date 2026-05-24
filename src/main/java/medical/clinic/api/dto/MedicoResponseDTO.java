package medical.clinic.api.dto;

import medical.clinic.api.enuns.Especialidade;

public record MedicoResponseDTO(
        String nome,
        String email,
        String crm,
        Especialidade especialidade
){
}
