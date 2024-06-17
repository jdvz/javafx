package ru.dzera.test.ktpostman.model

import javafx.beans.property.SimpleStringProperty

/**
 * exchange data model
 */
class ExchangeProperty {
    private val body = SimpleStringProperty()
    fun getBody() : String {
        return this.body.value
    }
    fun setBody(body: String?) {
        this.body.value = body
    }
    fun bodyProperty() = this.body

    private val responseBody = SimpleStringProperty()
    fun getResponseBody() : String {
        return this.responseBody.value
    }
    fun setResponseBody(responseBody: String?) {
        this.responseBody.value = responseBody
    }
    fun getResponseBodyProperty() = this.responseBody

    var parameters = mutableListOf(Couple())
    var headers = mutableListOf(Couple())
    var cookies = mutableListOf(Couple())
}