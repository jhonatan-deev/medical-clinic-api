package medical.clinic.api.mapper;

import medical.clinic.api.dto.endereco.EnderecoDTO;
import medical.clinic.api.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }

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

    public EnderecoDTO toDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return new EnderecoDTO(
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getCep(),
                endereco.getCidade(),
                endereco.getUf(),
                endereco.getComplemento(),
                endereco.getNumero()
        );
    }
}