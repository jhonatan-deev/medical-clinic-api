package medical.clinic.api.dto;

public record Endereco(
        String rua,
        String bairro,
        String cep,
        String cidade,
        String uf,
        String numero,
        String complemento
) {
}
