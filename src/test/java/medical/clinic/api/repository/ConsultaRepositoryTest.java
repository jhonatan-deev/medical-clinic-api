package medical.clinic.api.repository;

import medical.clinic.api.enuns.Especialidade;
import medical.clinic.api.model.Consulta;
import medical.clinic.api.model.Endereco;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ConsultaRepositoryTest {
    private static long contador = 1;

    private Endereco criarEndereco() {
        return new Endereco(
                "Rua A",
                "Centro",
                "70000-000",
                "Brasília",
                "DF",
                "123",
                "Casa"
        );
    }

    private Medico criarMedico(boolean ativo) {
        long id = contador++;

        return new Medico(
                "João Silva " + id,
                "joao" + id + "@teste.com",
                "CRM" + id,
                String.valueOf(61990000000L + id),
                Especialidade.CARDIOLOGIA,
                criarEndereco(),
                ativo
        );
    }

    private Paciente criarPaciente() {
        long id = contador++;

        return new Paciente(
                "Maria Silva " + id,
                "maria" + id + "@test.com",
                String.valueOf(61993000000L + id),
                "CPF" + id,
                criarEndereco(),
                true
        );
    }

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    @DisplayName("Deve retornar true quando paciente possui consulta no período")
    void deveRetornarTrueQuandoPacientePossuiConsultaNoPeriodo() {

        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime dataConsultaFinal = LocalDateTime.of(2026, 6, 20, 11, 0);
        Paciente paciente = criarPaciente();
        Medico medico = criarMedico(true);
        Consulta criarConsulta = new Consulta(medico, paciente, dataConsulta);
        medicoRepository.save(medico);
        pacienteRepository.save(paciente);
        consultaRepository.save(criarConsulta);
        // ACT
        boolean consultas = consultaRepository.existsByPacienteIdAndDataBetween(paciente.getId(),dataConsulta, dataConsultaFinal );

        // ASSERT
        // verificar que retornou true
        assertTrue(consultas);
    }

    @Test
    @DisplayName("Deve retornar false quando paciente não possui consulta no período")
    void deveRetornarFalseQuandoPacienteNaoPossuiConsultaNoPeriodo() {

        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime dataConsultaFinal = LocalDateTime.of(2026, 6, 20, 11, 0);
        LocalDateTime consultaForaDoPeriodos = LocalDateTime.of(2026, 6, 20, 15, 30);
        Paciente paciente = criarPaciente();
        Medico medico = criarMedico(true);
        Consulta criarConsulta = new Consulta(medico, paciente, dataConsulta);

        medicoRepository.save(medico);
        pacienteRepository.save(paciente);
        consultaRepository.save(criarConsulta);
        // ACT
        boolean pacienteExiste = consultaRepository.existsByPacienteIdAndDataBetween(paciente.getId(), dataConsultaFinal,consultaForaDoPeriodos);

        // ASSERT
        assertFalse(pacienteExiste);
    }

    @Test
    @DisplayName("Deve retornar true quando médico possui consulta no horário")
    void deveRetornarTrueQuandoMedicoPossuiConsultaNoHorario() {
        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();

        Consulta criarConsulta = new Consulta(medico, paciente, dataConsulta);

        medicoRepository.save(medico);
        pacienteRepository.save(paciente);
        consultaRepository.save(criarConsulta);
        // ACT
        boolean medicoExiste =
                consultaRepository.existsByMedicoIdAndData(
                        medico.getId(),
                        dataConsulta
                );

        // ASSERT
        assertTrue(medicoExiste);
    }

    @Test
    @DisplayName("Deve retornar false quando médico não possui consulta no horário")
    void deveRetornarFalseQuandoMedicoNaoPossuiConsultaNoHorario() {

        // ARRANGE
        LocalDateTime dataConsultaPesquisada = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime dataConsultaDiferenteDoPesquisado = LocalDateTime.of(2026, 6, 20, 14, 0);
        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();
        Consulta criarConsulta = new Consulta(medico, paciente, dataConsultaDiferenteDoPesquisado);
        medicoRepository.save(medico);
        pacienteRepository.save(paciente);
        consultaRepository.save(criarConsulta);

        // ACT
        boolean consultas = consultaRepository.existsByMedicoIdAndData(medico.getId(), dataConsultaPesquisada);

        // ASSERT
        assertFalse(consultas);
    }


}