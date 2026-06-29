package medical.clinic.api.controller;

import jakarta.validation.Valid;
import medical.clinic.api.dto.LoginRequest;
import medical.clinic.api.dto.TokenResponse;
import medical.clinic.api.dto.usuario.UsuarioUpdateSenhaDTO;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.security.JwtService;
import medical.clinic.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtService tokenService;
    private TokenResponse tokenResponse;

    public AuthController(AuthenticationManager authenticationManager, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
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
}
