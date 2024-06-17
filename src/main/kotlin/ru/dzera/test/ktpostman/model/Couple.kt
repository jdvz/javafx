package ru.dzera.test.ktpostman.model

import javafx.beans.property.SimpleStringProperty
import org.apache.hc.core5.http.message.BasicNameValuePair
import java.io.Serializable

/**
 * pair with sugar
 */
class Couple(name: String = "", content: String = "") : Serializable {
    companion object {
        fun fromArray(data : Collection<String>) : Couple {
            return Couple(data.elementAt(0), data.elementAtOrElse(1) { "" })
        }
    }

    private val name : SimpleStringProperty = SimpleStringProperty(name)
    fun getName() : String {
        return this.name.value
    }
    fun setName(name: String) {
        this.name.value = name
    }
    fun nameProperty() : SimpleStringProperty {
        return this.name
    }

    private val content = SimpleStringProperty(content)
    fun getContent() : String {
        return this.content.value
    }
    fun setContent(content: String) {
        this.content.value = content
    }
    fun contentProperty() : SimpleStringProperty {
        return this.content
    }

    fun toBasicNameValuePair() = BasicNameValuePair(this.name.value, this.content.value)
    fun isEmpty() = this.name.value.isBlank() || content.value.isBlank()

    override fun toString(): String {
        return "Couple(${this.name.value}:=${this.content.value})"
    }
}