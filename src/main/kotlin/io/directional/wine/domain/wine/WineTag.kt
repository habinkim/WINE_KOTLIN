package io.directional.wine.domain.wine

import io.directional.wine.common.enums.WineTagType
import io.directional.wine.domain.base.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.util.*

@Entity
@Table
class WineTag(
    @Enumerated(EnumType.STRING)
    @Column
    @Comment("태그 유형")
    var type: WineTagType? = null,

    @Column
    @Comment("태그 값")
    var value: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wineId", referencedColumnName = "id")
    @Comment("와인 식별자")
    var wine: Wine? = null
) : BaseEntity() {

}
