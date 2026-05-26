package medical.clinic.api.mapper;

import medical.clinic.api.dto.paciente.PacienteRequestDTO;
import medical.clinic.api.dto.paciente.PacienteUpdateDTO;
import medical.clinic.api.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {
    private final EnderecoMapper enderecoMapper;

    public PacienteMapper(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }
    public Paciente toEntity(PacienteRequestDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNome(dto.nome());
        paciente.setEmail(dto.email());
        paciente.setTelefone(dto.telefone());
        paciente.setCpf(dto.cpf());
        paciente.setEndereco(enderecoMapper.toEntity(dto.endereco()));
        return paciente;
    }

    public PacienteRequestDTO toDTO(Paciente paciente) {
        return new PacienteRequestDTO(
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getCpf(),
                enderecoMapper.toDTO(paciente.getEndereco())
        );
    }

    public void toUpdate(Paciente paciente, PacienteUpdateDTO dto) {
        if (dto.nome() != null && !dto.nome().isBlank()) {
            paciente.setNome(dto.nome());
        }
        if (dto.telefone() != null && !dto.telefone().isBlank()) {
            paciente.setTelefone(dto.telefone());
        }
        if (dto.endereco() != null) {
            paciente.setEndereco(enderecoMapper.toEntity(dto.endereco()));
        }
    }
}
