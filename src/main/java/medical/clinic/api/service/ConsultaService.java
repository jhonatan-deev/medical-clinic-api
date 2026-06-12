package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.consulta.CancelamentoRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaResponseDTO;
import medical.clinic.api.exception.ConsultaNotFoundException;
import medical.clinic.api.exception.MedicoNotFoundException;
import medical.clinic.api.exception.PacienteNotFoundException;
import medical.clinic.api.mapper.ConsultaMapper;
import medical.clinic.api.model.Consulta;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.repository.ConsultaRepository;
import medical.clinic.api.repository.MedicoRepository;
import medical.clinic.api.repository.PacienteRepository;
import medical.clinic.api.validation.agendamento.ValidadorAgendamentoConsulta;
import medical.clinic.api.validation.cancelamento.ValidadorCancelamentoConsulta;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaMapper consultaMapper;
    private final List<ValidadorAgendamentoConsulta> validadores;
    private final List<ValidadorCancelamentoConsulta> validadoresCancelamento;

    public ConsultaService(ConsultaRepository consultaRepository,
                           MedicoRepository medicoRepository,
                           PacienteRepository pacienteRepository,
                           ConsultaMapper consultaMapper,
                           List<ValidadorAgendamentoConsulta> validadores,
                           List<ValidadorCancelamentoConsulta> validadoresCancelamento) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultaMapper = consultaMapper;
        this.validadores = validadores;
        this.validadoresCancelamento = validadoresCancelamento;
    }

    @Transactional
    public ConsultaResponseDTO agendar(ConsultaRequestDTO dto) {
        validadores.forEach(v -> v.validar(dto));
        Paciente paciente = pacienteRepository.findById(dto.pacienteId()).orElseThrow(() -> new PacienteNotFoundException("Paciente não encontrado!"));
        Medico medico;

        if (dto.medicoId() != null) {
            medico = medicoRepository.findById(dto.medicoId()).orElseThrow(() -> new MedicoNotFoundException("Médico não encontrado!"));
        } else {
            List<Medico> medicosDisponiveis = medicoRepository.findMedicosAtivosDisponiveisNaData(dto.data());
            medico = medicosDisponiveis.get(new Random().nextInt(medicosDisponiveis.size()));
        }

        Consulta consulta = consultaMapper.toEntity(dto, paciente, medico);
        Consulta consultaCriada = consultaRepository.save(consulta);
        return consultaMapper.toDTO(consultaCriada);
    }

    @Transactional
    public ConsultaResponseDTO cancelarConsulta(CancelamentoRequestDTO dto) {

        Consulta consulta = consultaRepository.findById(dto.idConsulta()).orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada!"));
        validadoresCancelamento.forEach(v -> v.validar(consulta));

        consulta.setMotivoCancelamento(dto.motivoCancelamento());
        consulta.setDataCancelamento(LocalDateTime.now());
        consulta.setAtiva(false);

        Consulta consultaCancelada = consultaRepository.save(consulta);
        return consultaMapper.toDTO(consultaCancelada);
    }

    public Page<ConsultaResponseDTO> findAllConsultas(Pageable pageable) {
        return consultaRepository.findByAtivaTrue(pageable).map(consultaMapper::toDTO);
    }
}