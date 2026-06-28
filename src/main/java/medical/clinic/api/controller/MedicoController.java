package medical.clinic.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/v1/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> createDoctor(@RequestBody @Valid MedicoRequestDTO dto) {
        MedicoResponseDTO medico = medicoService.createDoctor(dto);
        return ResponseEntity.status(201).body(medico);
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listDoctors(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(medicoService.listDoctors(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> updateDoctor(@RequestBody MedicoUpdateDTO dto, @PathVariable Long id) {
        MedicoResponseDTO medico = medicoService.updateDoctor(dto, id);
        return ResponseEntity.ok().body(medico);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMedical(@PathVariable Long id) {
//        medicoService.deleteMedico(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateDoctor(@PathVariable Long id) {
        medicoService.deactivateDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> findDoctorById(@PathVariable Long id) {
        MedicoResponseDTO medico = medicoService.findDoctorById(id);
        return ResponseEntity.ok().body(medico);
    }
}