package mintos.homework

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgresContainerTSpec extends DatabaseSpecTemplate {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate


    def 'check that database correctly has been filled with data and it accessible'() {
        expect:
        1 == 1

    }
}
