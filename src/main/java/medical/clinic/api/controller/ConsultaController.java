package medical.clinic.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import medical.clinic.api.dto.consulta.CancelamentoRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaResponseDTO;
import medical.clinic.api.service.ConsultaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {
    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> createConsultation(@RequestBody @Valid ConsultaRequestDTO consultaRequestDTO) {
        ConsultaResponseDTO consulta = consultaService.createConsultation(consultaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
    }

    @PostMapping("/cancelar")
    public ResponseEntity<ConsultaResponseDTO> cancelConsultation(@RequestBody @Valid CancelamentoRequestDTO dto) {
        ConsultaResponseDTO consultaCancelada = consultaService.cancelConsultation(dto);
        return ResponseEntity.status(HttpStatus.OK).body(consultaCancelada);
    }

    @GetMapping
    public ResponseEntity<Page<ConsultaResponseDTO>> listConsultations(@PageableDefault(size = 10, sort = "data") Pageable pageable) {
        return ResponseEntity.ok(consultaService.listConsultations(pageable));
    }
}
