package ru.dzera.test.ktpostman.model

import javafx.beans.property.SimpleStringProperty

/**
 * url text model
 */
class UrlText {
    private val urlTextProperty = SimpleStringProperty()
    fun setUrlText(url: String) {
        this.urlTextProperty.set(url)
    }
    fun getUrlText() : String {
        return this.urlTextProperty.get()
    }
    fun urlTextProperty() : SimpleStringProperty {
        return urlTextProperty;
    }
}