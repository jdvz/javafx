package ru.dzera.test.ktpostman.enums

import org.apache.hc.client5.http.classic.methods.*
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.net.URIBuilder
import ru.dzera.test.ktpostman.inject.CookieWrapper
import ru.dzera.test.ktpostman.inject.Autowire
import ru.dzera.test.ktpostman.model.Couple

enum class Method: IMethod {
    GET {
        override fun send(
            url: String,
            body: String?,
            parameters: List<Couple>,
            headers: List<Couple>?,
            cookies: List<Couple>?
        ): CloseableHttpResponse? {
            return createRequest(headers, cookies) {
                val urlBuilder = URIBuilder(url)
                parameters.forEach { couple -> urlBuilder.addParameter(couple.getName(), couple.getContent()) }
                HttpGet(urlBuilder.build())
            }
        }
    },
    POST {
        override fun send(
            url: String,
            body: String?,
            parameters: List<Couple>,
            headers: List<Couple>?,
            cookies: List<Couple>?
        ): CloseableHttpResponse? {
            return createRequest(headers, cookies) {
                val urlBuilder = URIBuilder(url)
                val post = HttpPost(urlBuilder.build())
                if (!body.isNullOrBlank()) {
                    post.entity = StringEntity(body)
                } else {
                    post.entity = UrlEncodedFormEntity(parameters.map(Couple::toBasicNameValuePair))
                }
                post
            }
        }
    },
    PUT {
        override fun send(
            url: String,
            body: String?,
            parameters: List<Couple>,
            headers: List<Couple>?,
            cookies: List<Couple>?
        ): CloseableHttpResponse? {
            return createRequest(headers, cookies) {
                val urlBuilder = URIBuilder(url)
                val post = HttpPut(urlBuilder.build())
                post.entity = UrlEncodedFormEntity(parameters.filter { couple -> !couple.isEmpty() }
                    .map(Couple::toBasicNameValuePair))
                post
            }
        }
    },
    DELETE {
        override fun send(
            url: String,
            body: String?,
            parameters: List<Couple>,
            headers: List<Couple>?,
            cookies: List<Couple>?
        ): CloseableHttpResponse? {
            return createRequest(headers, cookies) {
                val urlBuilder = URIBuilder(url)
                parameters.forEach { couple -> if (!couple.isEmpty()) { urlBuilder.addParameter(couple.getName(), couple.getContent()) } }
                HttpDelete(urlBuilder.build())
            }
        }
    },
    HEAD {
        override fun send(
            url: String,
            body: String?,
            parameters: List<Couple>,
            headers: List<Couple>?,
            cookies: List<Couple>?
        ): CloseableHttpResponse? {
            return createRequest(headers, cookies) {
                val urlBuilder = URIBuilder(url)
                parameters.forEach { couple -> if (!couple.isEmpty()) { urlBuilder.addParameter(couple.getName(), couple.getContent()) } }
                HttpHead(urlBuilder.build())
            }
        }
    };

    protected fun <F : HttpUriRequestBase> createRequest(headers: List<Couple>?,
                                                      cookies: List<Couple>?,
                                                      function: () -> F) : CloseableHttpResponse? {
        val client = HttpClientBuilder.create().build()
        val request = function()
        val store = Autowire.autowire(CookieWrapper::class.java)

        request.addHeader("User-Agent", "Test/0.1")
        headers?.forEach { e ->
            if (!e.isEmpty()) request.addHeader(e.getName(), e.getContent())
        }
        cookies?.forEach { e ->
            if (!e.isEmpty()) {
                val cookie = BasicClientCookie(e.getName(), e.getContent())
                cookie.setDomain(request.uri.getHost())
                cookie.setPath("/")
                store.addCookie(cookie)
            }
        }

        return client.execute(request)
    }
}