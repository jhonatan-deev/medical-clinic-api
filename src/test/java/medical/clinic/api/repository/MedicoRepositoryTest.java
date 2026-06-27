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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Testa SÓ a camada JPA (repositórios, entities) com rollback automático
@ActiveProfiles("test")// Ativa configurações específicas do arquivo application-test.properties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)// Substitui QUALQUER banco real por um banco de teste embutido (ex: H2)
class MedicoRepositoryTest {

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
                "CRM" + id,
                String.valueOf(61990000000L + id),
                Especialidade.CARDIOLOGIA,
                criarEndereco(),
                ativo
        );
    }

    private Medico criarMedicoOcupado(boolean ativo) {
        long id = contador++;

        return new Medico(
                "João Ocupado " + id,
                "CRM-OCU" + id,
                String.valueOf(61991000000L + id),
                Especialidade.CARDIOLOGIA,
                criarEndereco(),
                ativo
        );
    }

    private Medico criarMedicoComEspecialidade(Especialidade esp) {
        long id = contador++;

        return new Medico(
                "Nome " + id,
                "CRM-ESP" + id,
                String.valueOf(61992000000L + id),
                esp,
                criarEndereco(),
                true
        );
    }

    private Paciente criarPaciente() {
        long id = contador++;

        return new Paciente(
                "Maria Silva " + id,
                String.valueOf(61993000000L + id),
                "CPF" + id,
                criarEndereco(),
                true
        );
    }
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Test
    @DisplayName("Deve retornar médicos ativos e disponíveis na data informada")
    void deveRetornarMedicosAtivosDisponiveisNaData() {
        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        Medico medico = criarMedico(true);

        // ACT
        medicoRepository.save(medico);
        List<Medico> resultado = medicoRepository.findMedicosAtivosDisponiveisNaData(dataConsulta);

        // ASSERT
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(medico));
    }

    @Test
    @DisplayName("Não deve retornar médicos inativos")
    void naoDeveRetornarMedicosInativos() {
        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        Medico medico = criarMedico(false);

        // ACT
        medicoRepository.save(medico);
        List<Medico> resultado = medicoRepository.findMedicosAtivosDisponiveisNaData(dataConsulta);

        // ASSERT
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Não deve retornar médico com consulta no mesmo horário")
    void naoDeveRetornarMedicoComConsultaNoMesmoHorario() {
        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();

        // ACT
        medicoRepository.save(medico);
        pacienteRepository.save(paciente);
        consultaRepository.save(new Consulta(medico, paciente, dataConsulta));

        List<Medico> resultado = medicoRepository.findMedicosAtivosDisponiveisNaData(dataConsulta);

        // ASSERT
        assertFalse(resultado.contains(medico));
    }

    @Test
    @DisplayName("Deve retornar médico quando consulta for em horário diferente")
    void deveRetornarMedicoQuandoConsultaForEmHorarioDiferente() {
        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime outroHorario = LocalDateTime.of(2026, 6, 20, 9, 0);

        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();

        // ACT
        medicoRepository.save(medico);
        pacienteRepository.save(paciente);
        consultaRepository.save(new Consulta(medico, paciente, outroHorario));

        List<Medico> resultado = medicoRepository.findMedicosAtivosDisponiveisNaData(dataConsulta);

        // ASSERT
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(medico));
    }

    @Test
    @DisplayName("Deve retornar apenas médicos disponíveis")
    void deveRetornarApenasMedicosDisponiveis() {
        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);

        Medico livre = criarMedico(true);
        Medico ocupado = criarMedico(true);
        Paciente paciente = criarPaciente();

        // ACT
        medicoRepository.save(livre);
        medicoRepository.save(ocupado);
        pacienteRepository.save(paciente);
        consultaRepository.save(new Consulta(ocupado, paciente, dataConsulta));

        List<Medico> resultado = medicoRepository.findMedicosAtivosDisponiveisNaData(dataConsulta);

        // ASSERT
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(livre));
        assertFalse(resultado.contains(ocupado));
    }

    @Test
    @DisplayName("Deve retornar médicos da especialidade informada")
    void deveRetornarMedicosDaEspecialidadeInformada() {
        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);

        Medico cardio = criarMedicoComEspecialidade(Especialidade.CARDIOLOGIA);
        Medico ortopedista = criarMedicoComEspecialidade(Especialidade.ORTOPEDIA);

        // ACT
        medicoRepository.save(cardio);
        medicoRepository.save(ortopedista);

        List<Medico> resultado =
                medicoRepository.findMedicosAtivosDisponiveisNaDataPorEspecialidade(
                        dataConsulta,
                        Especialidade.CARDIOLOGIA
                );

        // ASSERT
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(cardio));
        assertFalse(resultado.contains(ortopedista));
    }

    @Test
    @DisplayName("Deve retornar médico apenas se não houver consulta no horário exato")
    void deveRetornarMedicoApenasSeNaoHouverConsultaNoHorarioExato() {

        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime outroHorario = LocalDateTime.of(2026, 6, 20, 12, 0);

        Medico medico = criarMedico(true);
        Paciente paciente1 = criarPaciente();
        Paciente paciente2 = criarPaciente();

        medicoRepository.save(medico);
        pacienteRepository.save(paciente1);
        pacienteRepository.save(paciente2);

        consultaRepository.save(new Consulta(medico, paciente1, dataConsulta));
        consultaRepository.save(new Consulta(medico, paciente2, outroHorario));

        // ACT
        List<Medico> resultado =
                medicoRepository.findMedicosAtivosDisponiveisNaData(dataConsulta);

        // ASSERT
        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("Não deve retornar médico da especialidade quando estiver ocupado no horário")
    void naoDeveRetornarMedicoDaEspecialidadeQuandoEstiverOcupadoNoHorario() {

        // ARRANGE
        LocalDateTime dataConsulta = LocalDateTime.of(2026, 6, 20, 10, 0);

        Medico cardio = criarMedicoComEspecialidade(Especialidade.CARDIOLOGIA);
        Paciente paciente = criarPaciente();

        medicoRepository.save(cardio);
        pacienteRepository.save(paciente);

        consultaRepository.save(
                new Consulta(cardio, paciente, dataConsulta)
        );

        // ACT
        List<Medico> resultado = medicoRepository.findMedicosAtivosDisponiveisNaDataPorEspecialidade(dataConsulta, Especialidade.CARDIOLOGIA);

        // ASSERT
        assertTrue(resultado.isEmpty());
    }

}