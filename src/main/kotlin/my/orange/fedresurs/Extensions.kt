package my.orange.fedresurs

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(if (T::class.isCompanion) T::class.java.enclosingClass else T::class.java)

inline fun <T, R> Flow<T>.flatMap(crossinline transform: suspend (value: T) -> Flow<R>) = flow {
    map(transform).collect { emitAll(it) }
}
