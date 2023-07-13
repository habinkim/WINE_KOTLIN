package io.directional.wine.domain.wine

import io.directional.wine.domain.base.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table
class Importer(

    @Column
    @Comment("수입사명")
    var name: String? = null,

    @OneToMany(mappedBy = "importer", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var wines: List<Wine> = ArrayList()

) : BaseEntity()
