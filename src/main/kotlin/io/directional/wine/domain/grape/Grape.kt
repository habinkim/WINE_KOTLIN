package io.directional.wine.domain.grape

import io.directional.wine.domain.base.ProductBaseEntity
import io.directional.wine.domain.wine.WineGrape
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.util.*

@Entity
@Table
class Grape(
    @Column
    @Comment("산도")
    var acidity: Int? = null,

    @Column
    @Comment("바디감")
    var body: Int? = null,

    @Column
    @Comment("단맛")
    var sweetness: Int? = null,

    @Column
    @Comment("타닌")
    var tannin: Int? = null,

    @OneToMany(mappedBy = "grape", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var grapeShares: List<GrapeShare> = ArrayList(),

    @OneToMany(mappedBy = "grape", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var wineGrapes: List<WineGrape> = ArrayList()
) : ProductBaseEntity() {

}
