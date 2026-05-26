package medical.clinic.api.controller;

import jakarta.validation.Valid;
import medical.clinic.api.dto.medico.MedicoRequestDTO;
import medical.clinic.api.dto.medico.MedicoResponseDTO;
import medical.clinic.api.dto.medico.MedicoUpdateDTO;
import medical.clinic.api.service.MedicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> createMedical(@RequestBody @Valid MedicoRequestDTO dto) {
        MedicoResponseDTO medico = medicoService.createMedico(dto);
        return ResponseEntity.ok().body(medico);
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listMedical(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(medicoService.listMedico(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> updateMedical(@RequestBody
            MedicoUpdateDTO dto,@PathVariable Long id) {
        MedicoResponseDTO medico = medicoService.updateMedico(dto, id);
        return ResponseEntity.ok().body(medico);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMedical(@PathVariable Long id) {
//        medicoService.deleteMedico(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableMedical(@PathVariable Long id) {
        medicoService.desativarMedico(id);
        return ResponseEntity.noContent().build();
    };

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> getMedicalById(@PathVariable Long id) {
        MedicoResponseDTO medico = medicoService.findMedicoById(id);
        return ResponseEntity.ok().body(medico);
    }
}