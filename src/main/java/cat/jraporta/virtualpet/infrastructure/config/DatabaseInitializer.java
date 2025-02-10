package cat.jraporta.virtualpet.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class DatabaseInitializer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeDatabase();
    }

    private final DatabaseClient databaseClient;

    public void initializeDatabase() {
        log.info("Creating 'player' table in database if not exists...");
        databaseClient.sql("""
                -- Create table if it doesn't exist
                CREATE TABLE IF NOT EXISTS player (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(35) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL,
                    role VARCHAR(20) NOT NULL
                );
                
                """).then().subscribe();
    }

}
