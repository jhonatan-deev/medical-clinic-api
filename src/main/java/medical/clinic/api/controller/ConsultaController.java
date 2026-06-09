package medical.clinic.api.controller;

import jakarta.validation.Valid;
import medical.clinic.api.dto.consulta.CancelamentoRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaResponseDTO;
import medical.clinic.api.service.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> createConsulta(@RequestBody @Valid ConsultaRequestDTO consultaRequestDTO) {
        ConsultaResponseDTO consulta = consultaService.agendar(consultaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
    }

    @PostMapping("/cancelar")
    public ResponseEntity<ConsultaResponseDTO> cancelarConsulta(@RequestBody @Valid CancelamentoRequestDTO dto) {
        ConsultaResponseDTO consultaCancelada = consultaService.cancelarConsulta(dto);
        return ResponseEntity.status(HttpStatus.OK).body(consultaCancelada);
    }

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> getAllConsultas() {
        List<ConsultaResponseDTO> consultas = consultaService.findAllConsultas();
        return ResponseEntity.status(HttpStatus.OK).body(consultas);
    }
}
