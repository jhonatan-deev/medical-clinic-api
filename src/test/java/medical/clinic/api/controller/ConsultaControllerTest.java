package medical.clinic.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import medical.clinic.api.dto.consulta.ConsultaRequestDTO;
import medical.clinic.api.enuns.Especialidade;
import medical.clinic.api.enuns.Perfil;
import medical.clinic.api.model.Endereco;
import medical.clinic.api.model.Medico;
import medical.clinic.api.model.Paciente;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.repository.MedicoRepository;
import medical.clinic.api.repository.PacienteRepository;
import medical.clinic.api.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ConsultaControllerTest {

    private static long contador = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                "70000000",
                "Brasília",
                "DF",
                "123",
                "Casa"
        );
    }

    private Usuario criarUsuario(Perfil perfil) {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste" + contador++ + "@mail.com");
        usuario.setSenha("123456");
        usuario.setPerfil(perfil);

        usuario = usuarioRepository.saveAndFlush(usuario);

        return usuarioRepository.findById(usuario.getId())
                .orElseThrow();
    }

    private Medico criarMedico(boolean ativo) {
        Usuario usuario = criarUsuario(Perfil.MEDICO);

        Medico medico = new Medico(
                "João Silva " + contador,
                "CRM" + contador++,
                String.valueOf(61990000000L + contador),
                Especialidade.CARDIOLOGIA,
                criarEndereco(),
                ativo
        );

        medico.setUsuario(usuario);

        return medicoRepository.save(medico);
    }

    private Paciente criarPaciente() {
        Usuario usuario = criarUsuario(Perfil.PACIENTE);

        Paciente paciente = new Paciente(
                "Maria Silva " + contador,
                String.valueOf(61993000000L + contador),
                "CPF" + contador++,
                criarEndereco(),
                true
        );

        paciente.setUsuario(usuario);

        return pacienteRepository.save(paciente);
    }

    @Test
    @DisplayName("Deve retornar 400 quando informações estiverem inválidas")
    @WithMockUser
    void agendar_cenario1() throws Exception {
        var response = mockMvc.perform(post("/api/v1/consultas"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 201 quando informações estiverem válidas")
    @WithMockUser
    void agendar_cenario2() throws Exception {
        Medico medico = criarMedico(true);
        Paciente paciente = criarPaciente();

        LocalDateTime dataConsulta = LocalDateTime.now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                medico.getId(),
                paciente.getId(),
                dataConsulta,
                Especialidade.ORTOPEDIA
        );

        var response = mockMvc.perform(
                        post("/api/v1/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());

        String json = response.getContentAsString();

        assertThat(json).contains(String.valueOf(medico.getId()));
        assertThat(json).contains(String.valueOf(paciente.getId()));
    }
}