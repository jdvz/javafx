package ru.dzera.test.ktpostman.enums

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import ru.dzera.test.ktpostman.model.Couple
import java.util.*

interface IMethod {
    fun send(
        url: String,
        body: String? = null,
        parameters: List<Couple> = Collections.emptyList(),
        headers: List<Couple>? = null,
        cookies: List<Couple>? = null
    ): CloseableHttpResponse?
}
