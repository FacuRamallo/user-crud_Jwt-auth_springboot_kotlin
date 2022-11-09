package com.facundo.friends.integrationTests.database

import com.facundo.friends.integrationTests.IntegrationTest
import javax.sql.DataSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate


class DatabaseTestCase : com.facundo.friends.integrationTests.IntegrationTest() {

  @Autowired
  private lateinit var dataSource: DataSource

  @Test
  internal fun `should connect to database`(){
    val jdbcTemplate = JdbcTemplate(dataSource)

    val actual = jdbcTemplate.queryForObject("SELECT version()", String::class.java)

    Assertions.assertThat(actual).startsWith("PostgreSQL 14.2")

  }
}
