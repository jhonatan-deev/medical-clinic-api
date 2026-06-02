package medical.clinic.api.service;

import medical.clinic.api.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }
}
