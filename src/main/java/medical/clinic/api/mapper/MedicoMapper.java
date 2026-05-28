package medical.clinic.api.mapper;


import medical.clinic.api.dto.medico.MedicoRequestDTO;

import medical.clinic.api.dto.medico.MedicoResponseDTO;
import medical.clinic.api.dto.medico.MedicoUpdateDTO;

import medical.clinic.api.model.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {
    private EnderecoMapper enderecoMapper;
    public MedicoMapper(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }
        public Medico toEntity(MedicoRequestDTO dto) {
        Medico medico = new Medico();
        medico.setNome(dto.nome());
        medico.setEmail(dto.email());
        medico.setCrm(dto.crm());
        medico.setTelefone(dto.telefone());
        medico.setEspecialidade(dto.especialidade());
        medico.setEndereco(enderecoMapper.toEntity(dto.endereco()));
        return medico;
    }


    public MedicoResponseDTO toDTO(Medico medico){
        return new MedicoResponseDTO(
                medico.getId(),
                medico.getNome(),
                medico.getEmail(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.isAtivo(),
                medico.getEndereco()
        );
    }

    public void toUpdate(MedicoUpdateDTO dto, Medico medico){
        if(dto.nome() != null && !dto.nome().isBlank()) {
            medico.setNome(dto.nome());
        }
        if(dto.telefone() != null && !dto.telefone().isBlank()) {
            medico.setTelefone(dto.telefone());
        }
        if(dto.endereco() != null){
            medico.setEndereco(enderecoMapper.toEntity(dto.endereco()));
        }
    }

}
