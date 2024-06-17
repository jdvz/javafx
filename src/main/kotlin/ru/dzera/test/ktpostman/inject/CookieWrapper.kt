package ru.dzera.test.ktpostman.inject

import org.apache.hc.client5.http.cookie.BasicCookieStore
import org.apache.hc.client5.http.cookie.Cookie
import org.apache.hc.client5.http.cookie.CookieStore
import java.io.Serializable
import java.util.*

/**
 * cookies support
 */
class CookieWrapper : Bean, CookieStore, Serializable {
    val cookieStore = BasicCookieStore()
    override fun clear() {
        cookieStore.clear()
    }

    override fun addCookie(cookie: Cookie?) {
        cookieStore.addCookie(cookie)
    }

    override fun clearExpired(date: Date?): Boolean {
        return cookieStore.clearExpired(date)
    }

    override fun getCookies(): MutableList<Cookie> {
        return cookieStore.cookies
    }
}