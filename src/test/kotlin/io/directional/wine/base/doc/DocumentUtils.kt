package io.directional.wine.base.doc

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors

object DocumentUtils {
    val documentRequest: OperationRequestPreprocessor
        get() = Preprocessors.preprocessRequest(
            Preprocessors.modifyUris()
                .scheme("http")
                .host("localhost")
                .port(7500),
            Preprocessors.prettyPrint()
        )

    val documentResponse: OperationResponsePreprocessor
        get() = Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
}
