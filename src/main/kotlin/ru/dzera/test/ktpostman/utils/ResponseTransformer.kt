package ru.dzera.test.ktpostman.utils

import ru.dzera.test.ktpostman.inject.Bean
import java.io.Serializable

interface ResponseTransformer<T:Serializable> : Bean {
    fun transform(response: String): T?
}
