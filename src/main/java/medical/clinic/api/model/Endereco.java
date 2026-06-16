package medical.clinic.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.clinic.api.dto.endereco.EnderecoDTO;

@Getter
@NoArgsConstructor
@Embeddable
public class Endereco {
    @Setter
    private String logradouro;
    @Setter
    private String bairro;
    @Setter
    private String cep;
    @Setter
    private String cidade;
    @Setter
    @Column(name = "uf", length = 2)
    private String uf;
    @Setter
    private String numero;
    @Setter
    private String complemento;

    public Endereco(EnderecoDTO dto) {
        this.logradouro = dto.logradouro();
        this.bairro = dto.bairro();
        this.cep = dto.cep();
        this.cidade = dto.cidade();
        this.uf = dto.uf();
        this.numero = dto.numero();
        this.complemento = dto.complemento();
    }
    //Endereco pra teste
    public Endereco(
            String logradouro,
            String bairro,
            String cep,
            String cidade,
            String uf,
            String numero,
            String complemento
    ) {
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.uf = uf;
        this.numero = numero;
        this.complemento = complemento;
    }
}