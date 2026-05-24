package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.MedicoRequestDTO;
import medical.clinic.api.dto.MedicoResponseDTO;
import medical.clinic.api.mapper.MedicoMapper;
import medical.clinic.api.model.Medico;
import medical.clinic.api.repository.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }
    @Transactional
    public MedicoResponseDTO createMedico(MedicoRequestDTO medicoRequestDTO){
        Medico medico = medicoMapper.toEntity(medicoRequestDTO);
        Medico medicoResponse = medicoRepository.save(medico);
        return medicoMapper.toDTO(medicoResponse);
    }

    public Page<MedicoResponseDTO> listMedico(Pageable pageable){
        return medicoRepository.findAll(pageable)
                .map(medicoMapper::toDTO);
    }
}
