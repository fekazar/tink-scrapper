--liquibase formatted sql

--changeset fyodor:1
CREATE TABLE chats(
    id INTEGER PRIMARY KEY
);

--changeset fyodor:2
CREATE TABLE links(
    id SERIAL PRIMARY KEY,
    url VARCHAR(2000) NOT NULL,
    chat_id INTEGER REFERENCES chats (id),
    last_update TIMESTAMP WITH TIME ZONE DEFAULT '1954-01-01'
)