package medical.clinic.api.validation;

import medical.clinic.api.exception.RegraNegocioException;
import medical.clinic.api.model.Consulta;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.repository.ConsultaRepository;
import medical.clinic.api.repository.MedicoRepository;
import medical.clinic.api.repository.PacienteRepository;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConsultaValidation {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    public ConsultaValidation(PacienteRepository pacienteRepository, MedicoRepository medicoRepository, ConsultaRepository consultaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
    }

    public void validar(Long pacienteId, Long medicoId, LocalDateTime dataHora) {
        validaDiaHoraDeConsulta(dataHora);
        validaAntecedenciaMinima(dataHora);
        validaPacienteAtivo(pacienteId);
        validaPacienteSemConsultaNoDia(pacienteId, dataHora);
        validaMedicoAtivo(medicoId);
        validaMedicoDisponivel(medicoId, dataHora);
    }

    private void validaDiaHoraDeConsulta(LocalDateTime dataHora) {

        if (dataHora == null) {
            throw new RegraNegocioException("Data/hora não informada");
        }

        DayOfWeek dia = dataHora.getDayOfWeek();
        int hora = dataHora.getHour();

        if(dia == DayOfWeek.SUNDAY){
            throw new RegraNegocioException("Não é possível agendar consulta aos domingos!");
        }

        if(hora < 7){
            throw new RegraNegocioException("Erro: só é possível agendar consulta depois das 7h!");
        }
        LocalDateTime termino = dataHora.plusHours(1);
        if (termino.getHour() > 19 || (termino.getHour() == 19 && termino.getMinute() > 0)) {
            throw new RegraNegocioException("Infelizmente essa consulta excede nosso horário de fechamento consulta deve terminar até às 19h!");
        }
    }

    private void validaAntecedenciaMinima(LocalDateTime dataHora) {
        var horaAtual = LocalDateTime.now();
        var horaLimite = horaAtual.plusMinutes(30);
        if (dataHora.isBefore(horaLimite)) {
            throw new RegraNegocioException("Consulta deve ser agendada com pelo menos 30 minutos de antecedência!");
        }
    }

    private void validaPacienteAtivo(Long pacienteId) {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new RegraNegocioException("Paciente não encontrado!"));

            if(!paciente.isAtivo()){
                throw new RegraNegocioException("Paciente não está ativo!");
            }
    }

    private void validaMedicoAtivo(Long medicoId) {
            if(medicoId != null){
                Medico medico = medicoRepository.findById(medicoId)
                        .orElseThrow(() -> new RegraNegocioException("Médico não encontrado!"));
                if(!medico.isAtivo()){
                    throw new RegraNegocioException("Médico não está ativo!");
                }
            }
    }

    private void validaPacienteSemConsultaNoDia(Long pacienteId, LocalDateTime dataHora) {
            var dataAtual = dataHora.toLocalDate();
            boolean existeConsulta = consultaRepository.existsByPacienteIdAndData(pacienteId, dataAtual);
            if(existeConsulta){
                throw new RegraNegocioException("Já existe um consulta cadastrada nesse dia tente marcar em outra data!");
            }
    }
    private void validaMedicoDisponivel(Long medicoId, LocalDateTime dataHora) {

        // CASO 1: Paciente escolheu um médico específico
        if (medicoId != null) {
            // Verificar se esse médico já tem consulta nesse horário
            boolean medicoOcupado = consultaRepository.existsByMedicoIdAndDataHora(medicoId, dataHora);
            if (medicoOcupado) {
                throw new RegraNegocioException("Médico não está disponível neste horário!");
            }
        }

        // CASO 2: Paciente NÃO escolheu um médico
        else {
            // Buscar todos os médicos ativos e disponíveis nesse horário
            List<Medico> medicosDisponiveis = medicoRepository.findMedicosAtivosDisponiveisNaData(dataHora);

            // Se não houver nenhum médico disponível
            if (medicosDisponiveis.isEmpty()) {
                throw new RegraNegocioException("Nenhum médico disponível neste horário!");
            }

            // Escolher um médico aleatório da lista
            // (você vai guardar esse médico para usar depois no agendamento)
        }
    }

}
