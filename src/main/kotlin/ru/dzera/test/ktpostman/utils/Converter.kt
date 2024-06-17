package ru.dzera.test.ktpostman.utils

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.core5.http.HttpStatus
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 * convert response
 */
class Converter {
    companion object {
        val LOG = LoggerFactory.getLogger(Converter::class.java)

        fun <T : Serializable> transformResponse(transformer: ResponseTransformer<T>, httpResponse: CloseableHttpResponse?) : T? {
            if (httpResponse == null) {
                return null
            }
            if (HttpStatus.SC_OK <= httpResponse.code && HttpStatus.SC_ACCEPTED >= httpResponse.code) {
                val content = EntityUtils.toString(httpResponse.entity)
                LOG.debug(content)
                return transformer.transform(content)
            } else {
                LOG.error("Incorrect response code ${httpResponse.code}")
            }
            return null
        }
    }
}
