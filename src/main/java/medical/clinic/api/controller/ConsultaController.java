package medical.clinic.api.controller;

import jakarta.validation.Valid;
import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.dto.consulta.ConsultaResponseDTO;
import medical.clinic.api.service.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
