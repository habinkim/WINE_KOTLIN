package io.directional.wine.domain.wine

import io.directional.wine.domain.base.ProductBaseEntity
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.winery.Winery
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.util.*

@Entity
@Table
class Wine(
    @Column
    @Comment("와인의 종류")
    var type: String? = null,

    @Column
    @Comment("알코올 도수")
    var alcohol: Float? = null,

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

    @Column
    @Comment("권장 섭취온도")
    var servingTemperature: Float? = null,

    @Column
    @Comment("와인의 점수")
    var score: Float? = null,

    @Column
    @Comment("와인의 가격")
    var price: Int? = null,

    @Column
    @Comment("와인의 스타일")
    var style: String? = null,

    @Column
    @Comment("와인의 등급")
    var grade: String? = null,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importerId", referencedColumnName = "id")
    @Comment("수입사 식별자")
    var importer: Importer? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wineryId", referencedColumnName = "id")
    @Comment("와이너리 식별자")
    var winery: Winery? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId", referencedColumnName = "id")
    @Comment("지역 식별자")
    var region: Region? = null,

    @OneToMany(mappedBy = "wine", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var wineGrapes: List<WineGrape> = ArrayList(),

    @OneToMany(mappedBy = "wine", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var wineTags: List<WineTag> = ArrayList()
) : ProductBaseEntity()
