package medical.clinic.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import medical.clinic.api.dto.paciente.PacienteRequestDTO;
import medical.clinic.api.dto.paciente.PacienteResponseDTO;
import medical.clinic.api.dto.paciente.PacienteUpdateDTO;
import medical.clinic.api.enuns.Perfil;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<Page<PacienteResponseDTO>> listPatients(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(pacienteService.listPatients(pageable));
    }

    // @AuthenticationPrincipal injeta o usuário autenticado.
    // Se houver um atendente autenticado, executa o fluxo de cadastro do atendente
    // Caso contrário, executa o fluxo do paciente se cadastrando.
    @PostMapping
    @PreAuthorize("hasAnyRole('PACIENTE', 'ATENDENTE')")
    public ResponseEntity<PacienteResponseDTO> createPatient(
            @Valid @RequestBody PacienteRequestDTO pacienteRequestDTO,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        boolean chamadoPorAtendente = usuarioLogado != null
                && usuarioLogado.getPerfil() == Perfil.ATENDENTE;

        PacienteResponseDTO pacienteResponseDTO = chamadoPorAtendente
                ? pacienteService.atendenteCadastrarPaciente(pacienteRequestDTO)
                : pacienteService.createPatiente(pacienteRequestDTO);

        return ResponseEntity.status(201).body(pacienteResponseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<PacienteResponseDTO> updatePatient(@PathVariable Long id,
                                                             @RequestBody @Valid PacienteUpdateDTO pacienteUpdateDTO) {
        PacienteResponseDTO paciente = pacienteService.updatePatient(id, pacienteUpdateDTO);
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<Void> deactivatePatient(@PathVariable Long id) {
        pacienteService.deactivatePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<PacienteResponseDTO> findPatientById(@PathVariable Long id) {
        PacienteResponseDTO pacienteResponseDTO = pacienteService.findPatientById(id);
        return ResponseEntity.ok().body(pacienteResponseDTO);
    }
}