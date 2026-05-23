package medical.clinic.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import medical.clinic.api.dto.RequestDadosMedicoDTO;
import medical.clinic.api.model.Medico;
import medical.clinic.api.repository.MedicoRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoRepository repository;

    public MedicoController(MedicoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid RequestDadosMedicoDTO dto) {
        repository.save(new Medico(dto));
    }
}