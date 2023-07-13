package io.directional.wine.wine.repository

import io.directional.wine.payload.wine.ImporterPayloads

interface ImporterRepositoryCustom {

    fun list(name: String?) : List<ImporterPayloads.ListResponse>

}
