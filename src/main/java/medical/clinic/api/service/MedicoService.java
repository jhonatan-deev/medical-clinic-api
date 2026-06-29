package medical.clinic.api.service;

import jakarta.transaction.Transactional;
import medical.clinic.api.dto.medico.MedicoRequestDTO;
import medical.clinic.api.dto.medico.MedicoResponseDTO;
import medical.clinic.api.dto.medico.MedicoUpdateDTO;
import medical.clinic.api.enuns.Perfil;
import medical.clinic.api.exception.DuplicateResourceException;
import medical.clinic.api.exception.MedicoNotFoundException;
import medical.clinic.api.mapper.MedicoMapper;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.repository.MedicoRepository;
import medical.clinic.api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final UsuarioService usuarioService;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper, UsuarioService usuarioService) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public MedicoResponseDTO createDoctor(MedicoRequestDTO dto) {
        if (medicoRepository.existsByCrm(dto.crm())) {
            throw new DuplicateResourceException("CRM já cadastrado.");
        }
        Usuario usuario = usuarioService.criarUsuario(
                dto.usuario(),
                Perfil.MEDICO
        );
        Medico medico = medicoMapper.toEntity(dto);
        medico.setUsuario(usuario);
        Medico salvo = medicoRepository.save(medico);
        return medicoMapper.toDTO(salvo);
    }

    @Transactional
    public MedicoResponseDTO updateDoctor(MedicoUpdateDTO medicoUpdateDTO, Long id){
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

    public Page<MedicoResponseDTO> listDoctors(Pageable pageable){
        return medicoRepository.findAllByAtivoTrue(pageable)
                .map(medicoMapper::toDTO);
    }

    @Transactional
    public void deactivateDoctor(Long id){
        Medico medico = medicoRepository.findById(id).orElseThrow(
                ()-> new MedicoNotFoundException("Médico não encontrado!")
        );
        if(!medico.isAtivo()){
            throw new RuntimeException("Médico já está inativo.");
        }
        medico.setAtivo(false);
    }

    public MedicoResponseDTO findDoctorById(Long id){
        Medico medico = medicoRepository.findById(id).orElseThrow(
                ()-> new MedicoNotFoundException("Médico não encontrado!")
        );
        return medicoMapper.toDTO(medico);
    }
}
