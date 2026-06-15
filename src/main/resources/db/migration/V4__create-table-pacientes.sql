create table pacientes (
                           id bigserial primary key,
                           nome varchar(100) not null,
                           email varchar(100) not null unique,

                           cpf varchar(11) not null unique,
                           telefone varchar(20) not null,

                           logradouro varchar(100) not null,
                           bairro varchar(100) not null,
                           cep varchar(8) not null,
                           complemento varchar(100),
                           numero varchar(20),
                           uf char(2) not null,
                           cidade varchar(100) not null,

                           ativo boolean not null default true,

                           created_at timestamp default current_timestamp,
                           updated_at timestamp default current_timestamp
);