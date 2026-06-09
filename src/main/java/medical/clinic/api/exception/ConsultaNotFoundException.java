package medical.clinic.api.exception;

public class ConsultaNotFoundException extends RuntimeException {
    public ConsultaNotFoundException(String message) {
        super(message);
    }
}
