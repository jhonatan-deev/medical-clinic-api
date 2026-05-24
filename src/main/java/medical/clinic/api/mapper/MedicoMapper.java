package medical.clinic.api.mapper;

import medical.clinic.api.dto.EnderecoDTO;
import medical.clinic.api.dto.MedicoRequestDTO;

import medical.clinic.api.dto.MedicoResponseDTO;
import medical.clinic.api.dto.MedicoUpdateDTO;
import medical.clinic.api.model.Endereco;
import medical.clinic.api.model.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {
    public Medico toEntity(MedicoRequestDTO dto) {
        Medico medico = new Medico();
        medico.setNome(dto.nome());
        medico.setEmail(dto.email());
        medico.setCrm(dto.crm());
        medico.setTelefone(dto.telefone());
        medico.setEspecialidade(dto.especialidade());
        medico.setEndereco(toEnderecoEntity(dto.endereco()));
        return medico;
    }

    private Endereco toEnderecoEntity(EnderecoDTO dto) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.logradouro());
        endereco.setBairro(dto.bairro());
        endereco.setCep(dto.cep());
        endereco.setCidade(dto.cidade());
        endereco.setUf(dto.uf());
        endereco.setComplemento(dto.complemento());
        endereco.setNumero(dto.numero());
        return endereco;
    }

    public MedicoResponseDTO toDTO(Medico medico){
        return new MedicoResponseDTO(
                medico.getId(),
                medico.getNome(),
                medico.getEmail(),
                medico.getCrm(),
                medico.getEspecialidade()
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
            medico.setEndereco(toEnderecoEntity(dto.endereco()));
        }
    }

}
