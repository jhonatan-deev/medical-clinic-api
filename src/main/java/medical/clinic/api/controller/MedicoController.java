package medical.clinic.api.controller;

import jakarta.validation.Valid;
import medical.clinic.api.dto.MedicoRequestDTO;
import medical.clinic.api.dto.MedicoResponseDTO;
import medical.clinic.api.service.MedicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> cadastrar(@RequestBody @Valid MedicoRequestDTO dto) {
        MedicoResponseDTO medico = medicoService.createMedico(dto);
        return ResponseEntity.ok().body(medico);
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listarMedicos(Pageable pageable) {
        Page<MedicoResponseDTO> medicos = medicoService.listMedico(pageable);
        return ResponseEntity.ok().body(medicos);
    }
}