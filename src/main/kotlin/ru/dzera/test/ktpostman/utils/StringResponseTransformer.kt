package com.novardis.test.utils

import ru.dzera.test.ktpostman.utils.ResponseTransformer

/**
 * stub for converter
 */
class StringResponseTransformer : ResponseTransformer<String> {
    override fun transform(response: String): String {
        return response
    }
}