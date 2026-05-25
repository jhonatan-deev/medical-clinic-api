package medical.clinic.api.dto;

import medical.clinic.api.enuns.Especialidade;

public record MedicoResponseDTO(
        Long id,
        String nome,
        String email,
        String crm,
        Especialidade especialidade,
        boolean ativo
){
}
