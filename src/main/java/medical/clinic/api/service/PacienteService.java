package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.paciente.PacienteRequestDTO;
import medical.clinic.api.dto.paciente.PacienteResponseDTO;
import medical.clinic.api.dto.paciente.PacienteUpdateDTO;
import medical.clinic.api.exception.DuplicateResourceException;
import medical.clinic.api.exception.PacienteNotFoundException;
import medical.clinic.api.mapper.PacienteMapper;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public PacienteService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    @Transactional
    public PacienteResponseDTO createPaciente(PacienteRequestDTO pacienteRequestDTO) {
        if(pacienteRepository.existsByEmail(pacienteRequestDTO.email())){
            throw new DuplicateResourceException("Email de usuário já existente");
        }
        if(pacienteRepository.existsByCpf(pacienteRequestDTO.cpf())){
            throw new DuplicateResourceException("CPF de usuário já existente");
        }
        Paciente paciente = pacienteMapper.toEntity(pacienteRequestDTO);
        Paciente pacienteEntity = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(pacienteEntity);
    }

    @Transactional
    public PacienteResponseDTO updatePaciente(Long id, PacienteUpdateDTO pacienteUpdateDTO) {
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
    public void deletePaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new PacienteNotFoundException("Paciente não encontrado!")
        );
        pacienteRepository.delete(paciente);
    }

    public Page<PacienteResponseDTO> listPacientes(Pageable pageable) {
        return pacienteRepository.findByAtivoTrue(pageable)
                .map(pacienteMapper::toDTO);
    }

    public PacienteResponseDTO findPacienteById(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(
                () -> new PacienteNotFoundException("Paciente não encontrado!")
        );
        return pacienteMapper.toDTO(paciente);
    }
}
