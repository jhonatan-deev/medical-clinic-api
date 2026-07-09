CREATE TABLE account_confirmation_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(255) NOT NULL UNIQUE,
    usuario_id BIGINT       NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    usado       BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_act_user
        FOREIGN KEY (usuario_id)
            REFERENCES usuarios (id)
);