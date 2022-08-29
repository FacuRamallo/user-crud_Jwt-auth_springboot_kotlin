package es.adevinta.spain.friends.infraestructure.database

import es.adevinta.spain.friends.infraestructure.IntegrationTest
import javax.sql.DataSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate


class DatabaseTestCase : IntegrationTest() {

  @Autowired
  private lateinit var dataSource: DataSource

  @Test
  internal fun `should connect to database`(){
    val jdbcTemplate = JdbcTemplate(dataSource)

    val actual = jdbcTemplate.queryForObject("SELECT version()", String::class.java)

    Assertions.assertThat(actual).startsWith("PostgreSQL 14.2")

  }
}
