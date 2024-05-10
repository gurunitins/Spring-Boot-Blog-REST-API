DROP TABLE IF EXISTS post_tag;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS photos;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS todos;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS geo;

CREATE TABLE IF NOT EXISTS tags
(
    id         bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    name       varchar(255)                  NOT NULL,
    created_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by bigint check (created_by > 0) NOT NULL,
    updated_by bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS geo
(
    id         bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    lat        varchar(255),
    lng        varchar(255),
    created_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by bigint check (created_by > 0) NOT NULL,
    updated_by bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS company
(
    id           bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    name         varchar(255),
    catch_phrase varchar(255),
    bs           varchar(255),
    created_at   timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   bigint check (created_by > 0) NOT NULL,
    updated_by   bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS address
(
    id         bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    street     varchar(255),
    suite      varchar(255),
    city       varchar(255),
    zipcode    varchar(255),
    geo_id     bigint check (geo_id > 0)     NOT NULL REFERENCES geo (id),
    created_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by bigint check (created_by > 0) NOT NULL,
    updated_by bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id         bigint check (id > 0) NOT NULL GENERATED ALWAYS AS IDENTITY,
    first_name varchar(255)          NOT NULL,
    last_name  varchar(255)          NOT NULL,
    username   varchar(255)          NOT NULL,
    password   varchar(255)          NOT NULL,
    email      varchar(255)          NOT NULL,
    phone      varchar(255),
    website    varchar(255),
    address_id bigint check (address_id > 0)  DEFAULT NULL REFERENCES address (id),
    company_id bigint check (company_id > 0)  DEFAULT NULL REFERENCES company (id),
    created_at timestamp(0)          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS todos
(
    id         bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    title      varchar(255)                  NOT NULL,
    completed  boolean                                default false,
    user_id    bigint check (user_id > 0)             DEFAULT NULL REFERENCES users (id),
    created_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by bigint check (created_by > 0) NOT NULL,
    updated_by bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS albums
(
    id         bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    title      varchar(255)                  NOT NULL,
    user_id    bigint check (user_id > 0)             DEFAULT NULL REFERENCES users (id),
    created_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by bigint check (created_by > 0) NOT NULL,
    updated_by bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE photos
(
    id            bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    title         varchar(255)                  NOT NULL,
    url           varchar(255)                  NOT NULL,
    thumbnail_url varchar(255)                  NOT NULL,
    album_id      bigint check (album_id > 0)            DEFAULT NULL REFERENCES albums (id),
    created_at    timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    bigint check (created_by > 0) NOT NULL,
    updated_by    bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id         bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    title      varchar(255)                  NOT NULL,
    body       text                          NOT NULL,
    user_id    bigint check (user_id > 0)             DEFAULT NULL REFERENCES users (id),
    created_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by bigint check (created_by > 0) NOT NULL,
    updated_by bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE post_tag
(
    id      bigint check (id > 0)       NOT NULL GENERATED ALWAYS AS IDENTITY,
    post_id bigint check (post_id > 0)  NOT NULL REFERENCES posts (id),
    tag_id  bigint check ( tag_id > 0 ) NOT NULL REFERENCES tags (id),
    PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id         bigint check (id > 0)         NOT NULL GENERATED ALWAYS AS IDENTITY,
    name       varchar(255)                  NOT NULL,
    email      varchar(255)                  NOT NULL,
    body       text                          NOT NULL,
    post_id    bigint check ( post_id > 0 )           DEFAULT NULL REFERENCES posts (id),
    user_id    bigint check ( user_id > 0 )           DEFAULT NULL REFERENCES users (id),
    created_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(0)                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by bigint check (created_by > 0) NOT NULL,
    updated_by bigint check (updated_by > 0) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id   bigint check (id > 0) NOT NULL GENERATED ALWAYS AS IDENTITY,
    name varchar(255)          NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    id      bigint check (id > 0)        NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id bigint check ( user_id > 0 ) NOT NULL REFERENCES users (id),
    role_id bigint check ( role_id > 0 ) NOT NULL REFERENCES roles (id),
    PRIMARY KEY (id)
);

INSERT INTO roles(name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');
