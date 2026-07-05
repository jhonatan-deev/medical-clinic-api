package medical.clinic.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailDeRecuperacao(String destinatario, String token) {

        // Monta o link que o usuário vai clicar no email.
        // O frontend lê o ?token= da URL e exibe o formulário de nova senha.
        String link = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Recuperação de senha");
        message.setText("""
                Olá!

                Recebemos uma solicitação para redefinir a senha da sua conta.
                Clique no link abaixo para continuar (válido por 30 minutos):

                %s

                Se você não solicitou a recuperação de senha, ignore este email.
                Sua senha permanece a mesma.
                """.formatted(link));

        mailSender.send(message);
    }
}
