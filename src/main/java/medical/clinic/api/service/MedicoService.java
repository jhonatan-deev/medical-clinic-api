package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.MedicoRequestDTO;
import medical.clinic.api.dto.MedicoResponseDTO;
import medical.clinic.api.dto.MedicoUpdateDTO;
import medical.clinic.api.exception.MedicoNotFoundException;
import medical.clinic.api.mapper.MedicoMapper;
import medical.clinic.api.model.Medico;
import medical.clinic.api.repository.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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

    @Transactional
    public MedicoResponseDTO updateMedico(MedicoUpdateDTO medicoUpdateDTO, Long id){
        Medico medico = medicoRepository.findById(id).orElseThrow(
                () -> new MedicoNotFoundException("Médico não Encontrado!"));
        medicoMapper.toUpdate(medicoUpdateDTO, medico);
        Medico medicoResponse = medicoRepository.save(medico);
        return medicoMapper.toDTO(medicoResponse);
    }
    @Transactional
    public void deleteMedico(Long id){
        Medico medico = medicoRepository.findById(id).orElseThrow(
                () -> new MedicoNotFoundException("Médico não encontrado!")
        );
        medicoRepository.delete(medico);
    }

    @Transactional
    public Page<MedicoResponseDTO> listMedico(Pageable pageable){
        return medicoRepository.findAllByAtivoTrue(pageable)
                .map(medicoMapper::toDTO);
    }

    @Transactional
    public void desativarMedico(Long id){
        Medico medico = medicoRepository.findById(id).orElseThrow(
                ()-> new MedicoNotFoundException("Médico não encontrado!")
        );
        if(!medico.isAtivo()){
            throw new RuntimeException("Médico já está inativo");
        }
        medico.setAtivo(false);
    }
}
