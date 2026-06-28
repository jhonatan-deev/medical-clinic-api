package medical.clinic.api.repository;

import medical.clinic.api.enuns.Especialidade;
import medical.clinic.api.model.Consulta;
import medical.clinic.api.model.Endereco;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.model.Usuario;
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

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste" + contador++ + "@mail.com");
        usuario.setSenha("123456");
        return usuarioRepository.save(usuario);
    }

    private Medico criarMedico(boolean ativo) {
        Usuario usuario = criarUsuario();
        long id = contador++;
        Medico medico = new Medico(
                "João Silva " + id,
                "CRM" + id,
                String.valueOf(61990000000L + id),
                Especialidade.CARDIOLOGIA,
                criarEndereco(),
                ativo
        );
        medico.setUsuario(usuario);
        return medicoRepository.save(medico);
    }

    private Paciente criarPaciente() {
        Usuario usuario = criarUsuario();
        long id = contador++;
        Paciente paciente = new Paciente(
                "Maria Silva " + id,
                String.valueOf(61993000000L + id),
                "CPF" + id,
                criarEndereco(),
                true
        );
        paciente.setUsuario(usuario);
        return pacienteRepository.save(paciente);
    }

    @Test
    @DisplayName("Deve retornar true quando paciente possui consulta no período")
    void deveRetornarTrueQuandoPacientePossuiConsultaNoPeriodo() {
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime dataConsultaFinal = LocalDateTime.of(2026, 6, 20, 11, 0);
        Paciente paciente = criarPaciente();
        Medico medico = criarMedico(true);
        Consulta criarConsulta = new Consulta(medico, paciente, dataConsulta);
        consultaRepository.save(criarConsulta);

        boolean consultas = consultaRepository.existsByPacienteIdAndDataBetween(
                paciente.getId(), dataConsulta, dataConsultaFinal);

        assertTrue(consultas);
    }

    @Test
    @DisplayName("Deve retornar false quando paciente não possui consulta no período")
    void deveRetornarFalseQuandoPacienteNaoPossuiConsultaNoPeriodo() {
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime dataConsultaFinal = LocalDateTime.of(2026, 6, 20, 11, 0);
        LocalDateTime consultaForaDoPeriodo = LocalDateTime.of(2026, 6, 20, 15, 30);
        Paciente paciente = criarPaciente();
        Medico medico = criarMedico(true);
        Consulta criarConsulta = new Consulta(medico, paciente, dataConsulta);
        consultaRepository.save(criarConsulta);

        boolean pacienteExiste = consultaRepository.existsByPacienteIdAndDataBetween(
                paciente.getId(), dataConsultaFinal, consultaForaDoPeriodo);

        assertFalse(pacienteExiste);
    }

    @Test
    @DisplayName("Deve retornar true quando médico possui consulta no horário")
    void deveRetornarTrueQuandoMedicoPossuiConsultaNoHorario() {
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();
        Consulta criarConsulta = new Consulta(medico, paciente, dataConsulta);
        consultaRepository.save(criarConsulta);

        boolean medicoExiste = consultaRepository.existsByMedicoIdAndData(
                medico.getId(), dataConsulta);

        assertTrue(medicoExiste);
    }

    @Test
    @DisplayName("Deve retornar false quando médico não possui consulta no horário")
    void deveRetornarFalseQuandoMedicoNaoPossuiConsultaNoHorario() {
        LocalDateTime dataConsultaPesquisada = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime dataConsultaDiferente = LocalDateTime.of(2026, 6, 20, 14, 0);
        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();
        Consulta criarConsulta = new Consulta(medico, paciente, dataConsultaDiferente);
        consultaRepository.save(criarConsulta);

        boolean consultas = consultaRepository.existsByMedicoIdAndData(
                medico.getId(), dataConsultaPesquisada);

        assertFalse(consultas);
    }
}