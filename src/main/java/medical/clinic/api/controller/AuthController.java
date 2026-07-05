package medical.clinic.api.controller;

import jakarta.validation.Valid;
import medical.clinic.api.dto.LoginRequest;
import medical.clinic.api.dto.TokenResponse;
import medical.clinic.api.dto.password.ForgotPasswordRequest;
import medical.clinic.api.dto.password.ResetPasswordRequest;
import medical.clinic.api.dto.usuario.UsuarioUpdateSenhaDTO;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.security.JwtService;
import medical.clinic.api.service.PasswordResetService;
import medical.clinic.api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService tokenService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthenticationManager authenticationManager, UsuarioService usuarioService, JwtService tokenService, PasswordResetService passwordResetService) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid LoginRequest dados){
        var autenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(autenticationToken);
        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new  TokenResponse(tokenJWT));
    }

    @PatchMapping("/atualizar-senha")
    public ResponseEntity<Void> alterarSenha(
            @RequestBody @Valid UsuarioUpdateSenhaDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        usuarioService.alterarSenha(dto, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.solicitarRecuperacao(request.email());
        return ResponseEntity.ok(
                "Se o email informado estiver cadastrado, você receberá as instruções em breve."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request) {
        passwordResetService.redefinirSenha(request.token(), request.novaSenha());
        return ResponseEntity.ok("Senha redefinida com sucesso. Você já pode fazer login.");
    }
}
