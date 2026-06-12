package medical.clinic.api.validation.cancelamento;

import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.model.Consulta;
import org.springframework.stereotype.Component;

@Component
public class ValidadorConsultaAtiva
        implements ValidadorCancelamentoConsulta {

    @Override
    public void validar(Consulta consulta) {

        if (!consulta.isAtiva()) {
            throw new RegraNegocioException(
                    "Consulta já está cancelada!"
            );
        }
    }
}