package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SimpleTest extends IntegrationEnvironment {
    @Test
    void simpleTest() {
        assertNotNull(postgresDataSource);
    }

    @Test
    void createsTablesTest() throws SQLException {
        var conn = postgresDataSource.getConnection();

        var statement = conn.createStatement();
        assertDoesNotThrow(() -> statement.execute("SELECT * FROM chats;"));
        assertDoesNotThrow(() -> statement.execute("SELECT * FROM links;"));
    }
}
