package medical.clinic.api.mapper;

import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaResponseDTO;
import medical.clinic.api.dto.consulta.ConsultaUpdateDTO;
import medical.clinic.api.model.Consulta;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapper {

    public Consulta toEntity(ConsultaRequestDTO dto, Paciente paciente, Medico medico) {
        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(dto.data());
        return consulta;
    }

    public ConsultaResponseDTO toDTO(Consulta consulta) {
        return new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getMedico().getId(),
                consulta.getPaciente().getId(),
                consulta.getData()
        );
    }

    public void updateEntity(ConsultaUpdateDTO dto, Consulta consulta, Medico medico) {

        if (medico != null) {
            consulta.setMedico(medico);
        }

        if (dto.data() != null) {
            consulta.setData(dto.data());
        }
    }
}