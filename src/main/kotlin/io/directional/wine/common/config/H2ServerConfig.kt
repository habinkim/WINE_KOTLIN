package io.directional.wine.common.config

import org.h2.tools.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.sql.SQLException

@Profile("local")
@Configuration
class H2ServerConfig {

    @Bean
    @Throws(SQLException::class)
    fun h2TcpServer(): Server? {
        return Server.createTcpServer().start()
    }

}
