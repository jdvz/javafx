package ru.dzera.test.ktpostman.inject

import org.slf4j.LoggerFactory

class Autowire {
    companion object {
        val LOG = LoggerFactory.getLogger(Autowire::class.java)

        private val INSTANCE = Autowire()

        fun <T: Bean> autowire(clazz: Class<T>): T {
            return INSTANCE.inject(clazz)
        }
    }

    val beans = HashMap<Class<Bean>, Bean>()

    private fun <T: Bean> inject(clazz: Class<T>): T {
        var bean: Bean? = retrieveBean(clazz as Class<Bean>)
        if (bean == null) {
            bean = clazz.getDeclaredConstructor().newInstance()
            beans.put(clazz, bean)
        }
        return bean as T
    }

    private fun retrieveBean(clazz: Class<Bean>): Bean? {
        return beans[clazz]
    }
}

interface Bean {
}

inline fun <reified T : Bean> Bean.inject() : T {
    return Autowire.autowire(T::class.java)
}
