package io.directional.wine.domain.base

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.aspectj.weaver.tools.cache.SimpleCacheFactory.enabled
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
open class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected val id: Long? = null,

    @Column
    @Comment("고유번호")
    val uuid: String? = UUID.randomUUID().toString(),

    @JsonIgnore
    @Column
    @Comment("활성화 여부")
    protected var enabled: Boolean = true,

    @CreationTimestamp
    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("생성일")
    protected val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("수정일")
    protected var updatedAt: LocalDateTime? = null
) {

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
    }


}
