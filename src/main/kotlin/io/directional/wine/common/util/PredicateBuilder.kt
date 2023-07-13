package io.directional.wine.common.util

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.*
import org.springframework.util.StringUtils
import java.time.LocalDate
import java.time.LocalDateTime

class PredicateBuilder {
    private val predicateBuilders: MutableList<Predicate?> = ArrayList()
    fun <P : Predicate?> and(pr: P): PredicateBuilder {
        predicateBuilders.add(pr)
        return this
    }

    fun build(): Predicate? {
        return ExpressionUtils.allOf(predicateBuilders)
    }

    fun eqString(column: StringPath, value: String?): PredicateBuilder {
        if (StringUtils.hasText(value)) {
            predicateBuilders.add(column.eq(value))
        }
        return this
    }

    fun containsString(column: StringPath, value: String?): PredicateBuilder {
        if (StringUtils.hasText(value)) {
            predicateBuilders.add(column.contains(value))
        }
        return this
    }

    fun containsStringDesc(column1: StringPath, column2: StringPath, value: String?): PredicateBuilder {
        if (StringUtils.hasText(value)) {
            predicateBuilders.add(
                BooleanBuilder()
                    .andAnyOf(column1.contains(value), column2.contains(value))
            )
        }
        return this
    }

    fun <N> betweenNumberDynamic(
        column: NumberPath<N>,
        min: N?,
        max: N?
    ): PredicateBuilder where N : Number?, N : Comparable<*>? {
        if (min != null) {
            predicateBuilders.add(column.goe(min))
        }
        if (max != null) {
            predicateBuilders.add(column.loe(max))
        }
        return this
    }

    fun <N> eqNumber(column: NumberPath<N>, value: N?): PredicateBuilder where N : Number?, N : Comparable<*>? {
        if (value != null) {
            predicateBuilders.add(column.eq(value))
        }
        return this
    }

    fun <N> neNumber(column: NumberPath<N>, value: N?): PredicateBuilder where N : Number?, N : Comparable<*>? {
        if (value != null) {
            predicateBuilders.add(column.ne(value))
        }
        return this
    }

    fun <N> inNumber(
        column: NumberPath<N>,
        valueList: List<N>?
    ): PredicateBuilder where N : Number?, N : Comparable<*>? {
        if (valueList != null) {
            predicateBuilders.add(column.`in`(valueList))
        }
        return this
    }

    companion object {
        fun builder(): PredicateBuilder {
            return PredicateBuilder()
        }
    }
}
