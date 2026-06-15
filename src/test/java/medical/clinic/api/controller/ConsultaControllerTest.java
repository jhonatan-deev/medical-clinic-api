package medical.clinic.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import medical.clinic.api.dto.consulta.ConsultaRequestDTO;;
import medical.clinic.api.enuns.Especialidade;
import medical.clinic.api.model.Endereco;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.repository.MedicoRepository;
import medical.clinic.api.repository.PacienteRepository;
import medical.clinic.api.service.MedicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ConsultaControllerTest {
    private static long contador = 1;
    private Endereco criarEndereco() {
        return new Endereco(
                "Rua A",
                "Centro",
                "70000000",
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
                "joao3" + id + "@teste.com",
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
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;


    @Test
    @DisplayName("Deve devolver codigo http 400 quando informacaoes estão invalidas")
    @WithMockUser
    void agendar_cenario1() throws Exception {
        var response = mockMvc.perform(post("/consultas")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve devolver código http 201 quando informações estão válidas")
    @WithMockUser
    void agendar_cenario2() throws Exception {

        // ARRANGE
        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();

        medicoRepository.save(medico);
        pacienteRepository.save(paciente);

        LocalDateTime dataConsulta = LocalDateTime.now().plusDays(1);

        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                medico.getId(),
                paciente.getId(),
                dataConsulta,
                Especialidade.ORTOPEDIA
        );

        // ACT
        var response = mockMvc.perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andReturn()
                .getResponse();

        // ASSERT
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());

        String jsonRetornado = response.getContentAsString();

        assertThat(jsonRetornado).contains(String.valueOf(medico.getId()));
        assertThat(jsonRetornado).contains(String.valueOf(paciente.getId()));
    }
}