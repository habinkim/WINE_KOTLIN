package io.directional.wine.exception

import io.directional.wine.common.response.BaseExceptionResponse
import io.directional.wine.common.response.MessageCode
import io.directional.wine.common.response.ResponseMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler(
    private val responseMapper: ResponseMapper
) {

    @ExceptionHandler(CommonApplicationException::class)
    protected fun commonApplicationException(e: CommonApplicationException): ResponseEntity<BaseExceptionResponse> {
        show(e)
        return if (e.messageCode == null) {
            responseMapper.error(MessageCode.ERROR)
        } else {
            responseMapper.error(e.messageCode)
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun illegalArgumentExceptionHandle(e: MethodArgumentNotValidException): ResponseEntity<BaseExceptionResponse> {
        show(e)
        return responseMapper.error(MessageCode.EXCEPTION_ILLEGAL_ARGUMENT)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    protected fun missingServletRequestParameterExceptionHandle(e: MissingServletRequestParameterException): ResponseEntity<BaseExceptionResponse> {
        show(e)
        return responseMapper.error(MessageCode.EXCEPTION_ILLEGAL_ARGUMENT)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun methodArgumentTypeMismatchExceptionHandle(e: MethodArgumentTypeMismatchException): ResponseEntity<BaseExceptionResponse> {
        show(e)
        return responseMapper.error(MessageCode.EXCEPTION_ILLEGAL_ARGUMENT)
    }

    @ExceptionHandler(Exception::class)
    protected fun allExceptionHandle(e: Exception): ResponseEntity<BaseExceptionResponse> {
        show(e)
        return responseMapper.error(MessageCode.ERROR)
    }

    private fun show(e: Exception) {
        e.printStackTrace()
    }
}
