package io.directional.wine.domain.region

import io.directional.wine.domain.base.ProductBaseEntity
import io.directional.wine.domain.grape.GrapeShare
import io.directional.wine.domain.wine.Wine
import io.directional.wine.domain.winery.Winery
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.util.*

@Entity
@Table
class Region(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    @Comment("상위 지역 식별자")
    var parent: Region? = null,

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var grapeShares: List<GrapeShare> = ArrayList(),

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var wineries: List<Winery> = ArrayList(),

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var wines: List<Wine> = ArrayList()

) : ProductBaseEntity() {

}
