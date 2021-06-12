package com.example.multiple.datasource.example.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.example.multiple.datasource.example.team.repository"],
    entityManagerFactoryRef = "teamEntityManagerFactory",
    transactionManagerRef = "teamTransactionManager"
)

class TeamDataSourceConfig(
    private val jpaProperties: JpaProperties,
    private val hibernateProperties: HibernateProperties
) {
    @Bean(name = ["teamDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.hikari.team")
    fun teamDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Bean(name = ["teamEntityManagerFactory"])
    fun teamEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean? {

        val properties: MutableMap<String, Any>? =
            hibernateProperties.determineHibernateProperties(
                jpaProperties.properties, HibernateSettings()
            )

        return builder
            .dataSource(teamDataSource())
            .properties(properties)
            .packages(
                "com.example.multiple.datasource.example.team.entity"
            )
            .build()
    }

    @Bean(name = ["teamTransactionManager"])
    fun teamTransactionManager(
        entityManagerFactory: EntityManagerFactory?
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory!!)
    }
}