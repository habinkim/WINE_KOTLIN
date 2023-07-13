package io.directional.wine.domain.wine

import io.directional.wine.domain.base.BaseEntity
import io.directional.wine.domain.grape.Grape
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table
class WineGrape(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wineId", referencedColumnName = "id")
    @Comment("와인 식별자")
    var wine: Wine? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grapeId", referencedColumnName = "id")
    @Comment("포도 품종 식별자")
    var grape: Grape? = null
) : BaseEntity()
