package io.directional.wine.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.querydsl.jpa.JPQLTemplates
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource


@Configuration
class CommonBeans {

    @Bean
    fun messageSource(): MessageSource {
        val basename = "messages/messages"
        val charSet = "UTF-8"
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename(basename)
        messageSource.setDefaultEncoding(charSet)
        return messageSource
    }

    @PersistenceContext
    lateinit var em: EntityManager

    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(JPQLTemplates.DEFAULT, em)
    }

    @Bean
    fun objectMapper() : ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }


}
