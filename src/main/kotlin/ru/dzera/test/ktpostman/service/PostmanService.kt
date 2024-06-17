package ru.dzera.test.ktpostman.service

import javafx.collections.ObservableList
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.slf4j.LoggerFactory
import ru.dzera.test.ktpostman.controller.PostmanController
import ru.dzera.test.ktpostman.enums.Method
import ru.dzera.test.ktpostman.inject.inject
import ru.dzera.test.ktpostman.model.Couple
import ru.dzera.test.ktpostman.model.ExchangeProperty
import java.io.File
import java.io.Serializable

class PostmanService : IService {
    companion object {
        val LOG = LoggerFactory.getLogger(PostmanService::class.java)
    }

    private val configurationService : ConfigurationService = inject()

    fun send(method: Method, url: String, requestModel: ExchangeProperty): CloseableHttpResponse? {
        return method.send(
            url,
            requestModel.getBody(),
            requestModel.parameters,
            requestModel.headers,
            requestModel.cookies)
    }

    fun remove(name: String?, collection: ObservableList<Couple>) {
        val c = collection.find { c -> c.nameProperty().equals(name) }
        if (c != null) {
            PostmanController.LOG.info("C will be deleted by $name")
            collection.remove(c)
        }
    }

    fun readBodyFromFile(file: File?) {
        if (file != null && file.isFile()) {
            configurationService.fileDirectory = file.parentFile.absolutePath
//            requestModel.body = file.readText()
        }
    }

    fun getInitialUrlValue() : String = configurationService.urlValue ?: PostmanController.URL_INITIAL_VALUE

    private fun methods(): Collection<Method> {
        return Method.values().asList()
    }
}

class RequestModel(url: String, body: String? = null) : Serializable {
    var url: String = url
    var body: String? = body
    var responseBody: String = ""

    var parameters = mutableListOf(Couple())
    var headers = mutableListOf(Couple())
    var cookies = mutableListOf(Couple())
}
