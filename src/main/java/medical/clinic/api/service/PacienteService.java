package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.paciente.PacienteRequestDTO;
import medical.clinic.api.dto.paciente.PacienteResponseDTO;
import medical.clinic.api.dto.paciente.PacienteUpdateDTO;
import medical.clinic.api.exception.DuplicateResourceException;
import medical.clinic.api.exception.PacienteNotFoundException;
import medical.clinic.api.mapper.PacienteMapper;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final UsuarioService usuarioService;

    public PacienteService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper, UsuarioService usuarioService) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public PacienteResponseDTO createPatient(PacienteRequestDTO dto) {

        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new DuplicateResourceException("CPF já existente.");
        }
        Usuario usuario = usuarioService.criarUsuario(dto.usuario());
        Paciente paciente = pacienteMapper.toEntity(dto);
        paciente.setUsuario(usuario);
        Paciente salvo = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(salvo);
    }

    @Transactional
    public PacienteResponseDTO updatePatient(Long id, PacienteUpdateDTO pacienteUpdateDTO) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new PacienteNotFoundException("Paciente não encontrado!"));
        pacienteMapper.toUpdate(paciente, pacienteUpdateDTO);
        Paciente pacienteEntity = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(pacienteEntity);
    }


    @Transactional
    public void desativarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new PacienteNotFoundException("Paciente não encontrado!")
        );
        if(!paciente.isAtivo()) {
            throw new RuntimeException("Paciente já está inativo.");
        }
        paciente.setAtivo(false);
    }

    @Transactional
    public void deactivatePatient(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new PacienteNotFoundException("Paciente não encontrado!")
        );
        if(!paciente.isAtivo()) {
            throw new RuntimeException("Médico já está inativo.");
        }
        paciente.setAtivo(false);
    }

    public Page<PacienteResponseDTO> listPatients(Pageable pageable) {
        return pacienteRepository.findByAtivoTrue(pageable)
                .map(pacienteMapper::toDTO);
    }

    public PacienteResponseDTO findPatientById(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new PacienteNotFoundException("Paciente não encontrado!")
        );
        return pacienteMapper.toDTO(paciente);
    }
}
