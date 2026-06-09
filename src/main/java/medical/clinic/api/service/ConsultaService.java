package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.consulta.CancelamentoRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaResponseDTO;
import medical.clinic.api.exception.ConsultaNotFoundException;
import medical.clinic.api.exception.MedicoNotFoundException;
import medical.clinic.api.exception.PacienteNotFoundException;
import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.mapper.ConsultaMapper;
import medical.clinic.api.model.Consulta;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.repository.ConsultaRepository;
import medical.clinic.api.repository.MedicoRepository;
import medical.clinic.api.repository.PacienteRepository;
import medical.clinic.api.validation.ConsultaValidation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final ConsultaValidation consultaValidation;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaMapper consultaMapper;

    public ConsultaService(ConsultaRepository consultaRepository, ConsultaValidation consultaValidation, MedicoRepository medicoRepository, PacienteRepository pacienteRepository, ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.consultaValidation = consultaValidation;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultaMapper = consultaMapper;
    }
    @Transactional
    public ConsultaResponseDTO agendar(ConsultaRequestDTO dto) {
        consultaValidation.validar(dto.pacienteId(), dto.medicoId(), dto.data(), dto.especialidade());
        Paciente paciente = pacienteRepository.findById(dto.pacienteId()).orElseThrow(() -> new PacienteNotFoundException("Paciente não encontrado!"));
        Medico medico;
        if(dto.medicoId() != null){
            medico = medicoRepository.findById(dto.medicoId()).orElseThrow(() -> new MedicoNotFoundException("Médico não encontrado!"));
        }else {

            List<Medico> medicosDisponiveis =
                    medicoRepository.findMedicosAtivosDisponiveisNaData(dto.data());
            Random random = new Random();
            medico = medicosDisponiveis.get(
                    random.nextInt(medicosDisponiveis.size())
            );
        }

        Consulta consulta = consultaMapper.toEntity(
                dto,
                paciente,
                medico
        );

        Consulta consultaCriada = consultaRepository.save(consulta);
        return consultaMapper.toDTO(consultaCriada);
    }
    @Transactional
    public ConsultaResponseDTO cancelarConsulta(CancelamentoRequestDTO dto) {
        Consulta consulta = consultaRepository.findById(dto.idConsulta())
                .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada!"));
        if (!consulta.isAtiva()) {
            throw new RegraNegocioException("Consulta já está cancelada!");
        }
        if (consulta.getData().isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("Não é possível cancelar uma consulta que já ocorreu!");
        }

        LocalDateTime limiteCancelamento = LocalDateTime.now().plusHours(24);
        if (consulta.getData().isBefore(limiteCancelamento)) {
            throw new RegraNegocioException("Cancelamento só pode ser feito com 24 horas de antecedência!");
        }
        consulta.setMotivoCancelamento(dto.motivoCancelamento());
        consulta.setDataCancelamento(LocalDateTime.now());
        consulta.setAtiva(false);
        Consulta consultaCancelado = consultaRepository.save(consulta);
        return consultaMapper.toDTO(consultaCancelado);
    }

    public List<ConsultaResponseDTO> findAllConsultas() {
        return consultaRepository.findAll()
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
    };

}
