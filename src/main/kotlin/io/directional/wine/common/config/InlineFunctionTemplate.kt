package io.directional.wine.common.config

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Path
import com.querydsl.core.types.Projections
import com.querydsl.core.types.QBean
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import io.directional.wine.domain.grape.QGrape.grape
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.FileReader


inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

inline fun <reified T> buildCsvToBean(csvFilePath: String): CsvToBean<T> {
    val reader = BufferedReader(FileReader(csvFilePath))
    return CsvToBeanBuilder<T>(reader)
        .withType(T::class.java)
        .withSeparator(',')
        .withIgnoreLeadingWhiteSpace(true)
        .withIgnoreEmptyLine(true)
        .withQuoteChar('"')
        .build()
}

inline fun <reified T> buildQBean(vararg paths: Expression<*>): QBean<T> {
    return Projections.fields(T::class.java, *paths)
}

class InlineFunctionTemplate
