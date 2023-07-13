package io.directional.wine.domain.grape

import io.directional.wine.domain.region.Region
import io.directional.wine.domain.base.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.util.*

@Entity
@Table
class GrapeShare(
    @Column
    @Comment("포도 품종의 비율")
    var share: Float? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grapeId", referencedColumnName = "id")
    @Comment("포도 품종 식별자")
    var grape: Grape? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId", referencedColumnName = "id")
    @Comment("지역 식별자")
    var region: Region? = null
) : BaseEntity() {

}
