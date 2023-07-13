package io.directional.wine.domain.base

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
open class ProductBaseEntity(

    @Column
    var nameKorean: String? = null,

    @Column
    var nameEnglish: String? = null

) : BaseEntity() {

}
