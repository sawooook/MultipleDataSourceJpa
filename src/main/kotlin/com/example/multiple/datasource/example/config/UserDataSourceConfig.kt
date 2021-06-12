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
    basePackages = ["com.example.multiple.datasource.example.user.repository"],
    entityManagerFactoryRef = "userEntityManagerFactory",
    transactionManagerRef = "userTransactionManager"
)

class UserDataSourceConfig(
    private val jpaProperties: JpaProperties,
    private val hibernateProperties: HibernateProperties
) {
    @Primary
    @Bean(name = ["userDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.hikari.user")
    fun userDataSource(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build()
    }

    @Primary
    @Bean(name = ["userEntityManagerFactory"])
    fun userEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean? {

        val properties: MutableMap<String, Any>? =
            hibernateProperties.determineHibernateProperties(
                jpaProperties.properties, HibernateSettings()
            )

        return builder
            .dataSource(userDataSource())
            .properties(properties)
            .packages(
                "com.example.multiple.datasource.example.user.entity"
            )
            .build()
    }

    @Primary
    @Bean(name = ["userTransactionManager"])
    fun userTransactionManager(
        entityManagerFactory: EntityManagerFactory?
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory!!)
    }
}