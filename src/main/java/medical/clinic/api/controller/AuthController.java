package medical.clinic.api.controller;

import jakarta.validation.Valid;
import medical.clinic.api.dto.LoginRequest;
import medical.clinic.api.dto.TokenResponse;
import medical.clinic.api.model.Usuario;
import medical.clinic.api.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService tokenService;
    private TokenResponse tokenResponse;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid LoginRequest dados){
        var autenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = authenticationManager.authenticate(autenticationToken);
        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new  TokenResponse(tokenJWT));
    }
}
