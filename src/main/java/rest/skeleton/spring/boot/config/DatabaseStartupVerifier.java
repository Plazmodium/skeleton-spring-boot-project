package rest.skeleton.spring.boot.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseStartupVerifier {
    private static final Logger log = LoggerFactory.getLogger(DatabaseStartupVerifier.class);

    @Bean
    public ApplicationRunner databaseCheckRunner(DataSource dataSource, Flyway flyway) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                try {
                    String url = dataSource.getConnection().getMetaData().getURL();
                    log.info("[DB-STARTUP] Verifying connectivity to datasource: {}", url);

                    // Flyway migrations are auto-run by Spring Boot before this runner executes.
                    // We surface the current migration state for observability.
                    var info = flyway.info();
                    var current = info.current();
                    var pendingCount = info.pending().length;
                    log.info("[DB-STARTUP] Flyway state — CurrentVersion={}, PendingMigrations={}",
                            current != null ? current.getVersion() : "<none>", pendingCount);
                } catch (Exception e) {
                    log.error("[DB-STARTUP] Database connectivity or migration verification failed: {}", e.getMessage(), e);
                    // Fail fast with a clear message so orchestration notices the failure (Req 12)
                    throw e;
                }
            }
        };
    }
}
