package io.directional.wine.domain.winery

import io.directional.wine.domain.base.ProductBaseEntity
import io.directional.wine.domain.region.Region
import io.directional.wine.domain.wine.Wine
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.util.*

@Entity
@Table
class Winery(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId", referencedColumnName = "id")
    @Comment("지역 식별자")
    var region: Region? = null,

    @OneToMany(mappedBy = "winery", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var wines: List<Wine> = ArrayList()
) : ProductBaseEntity() {

}
