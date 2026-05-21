package medical.clinic.api.controller;

import medical.clinic.api.dto.RequestDadosMedicoDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @PostMapping
    public void createMedico(@RequestBody RequestDadosMedicoDTO requestDadosMedicoDTO){
        System.out.println(requestDadosMedicoDTO);
    }
}
