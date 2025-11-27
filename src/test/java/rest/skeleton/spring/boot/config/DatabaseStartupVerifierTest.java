package rest.skeleton.spring.boot.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DatabaseStartupVerifierTest {

    @Test
    void databaseCheckRunner_logsInfo_whenConnectivityAndFlywayOk() throws Exception {
        // Mocks for DataSource and connection metadata
        DataSource ds = mock(DataSource.class);
        Connection conn = mock(Connection.class);
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        when(ds.getConnection()).thenReturn(conn);
        when(conn.getMetaData()).thenReturn(meta);
        when(meta.getURL()).thenReturn("jdbc:h2:mem:test");

        // Mocks for Flyway info
        Flyway flyway = mock(Flyway.class);
        MigrationInfoService infoService = mock(MigrationInfoService.class);
        when(flyway.info()).thenReturn(infoService);
        when(infoService.current()).thenReturn((MigrationInfo) null); // no current migration
        when(infoService.pending()).thenReturn(new MigrationInfo[0]);

        DatabaseStartupVerifier verifier = new DatabaseStartupVerifier();
        var runner = verifier.databaseCheckRunner(ds, flyway);

        assertDoesNotThrow(() -> runner.run(new DefaultApplicationArguments(new String[]{})));
    }

    @Test
    void databaseCheckRunner_throws_whenConnectivityFails() throws Exception {
        DataSource ds = mock(DataSource.class);
        when(ds.getConnection()).thenThrow(new RuntimeException("DB down"));

        Flyway flyway = mock(Flyway.class);

        DatabaseStartupVerifier verifier = new DatabaseStartupVerifier();
        var runner = verifier.databaseCheckRunner(ds, flyway);

        assertThrows(Exception.class, () -> runner.run(new DefaultApplicationArguments(new String[]{})));
    }

    @Test
    void databaseCheckRunner_throws_whenFlywayInfoFails() throws Exception {
        DataSource ds = mock(DataSource.class);
        Connection conn = mock(Connection.class);
        DatabaseMetaData meta = mock(DatabaseMetaData.class);
        when(ds.getConnection()).thenReturn(conn);
        when(conn.getMetaData()).thenReturn(meta);
        when(meta.getURL()).thenReturn("jdbc:h2:mem:test");

        Flyway flyway = mock(Flyway.class);
        when(flyway.info()).thenThrow(new RuntimeException("Flyway error"));

        DatabaseStartupVerifier verifier = new DatabaseStartupVerifier();
        var runner = verifier.databaseCheckRunner(ds, flyway);

        assertThrows(Exception.class, () -> runner.run(new DefaultApplicationArguments(new String[]{})));
    }
}
