package mt.lab.sysoev.test.util

import mt.lab.sysoev.parser.*
import mt.lab.sysoev.token.*
import org.junit.jupiter.api.*


inline fun <reified E> String.toTokenSource(): TokenSource<E> where E : Enum<E>, E: Token {
    return tokenSource(this)
}

inline fun <reified E> withSource(string: String, handler: TokenSource<E>.() -> Unit) where E: Enum<E>, E: Token {
    handler(string.toTokenSource())
}

inline fun <reified E> withSourceFails(string: String, pos: Int, handler: TokenSource<E>.() -> Unit) where E: Enum<E>, E: Token {
    val e = assertThrows<ParseException> {
        handler(string.toTokenSource())
    }
    Assertions.assertEquals(e.pos, pos)
    println("Expected error caught in test:\n\t ${e.message}, e.pos: ${e.pos}\n")
}

fun <T: Token> TokenSource<T>.assertEnded(endToken: T? = null) {
    skipWs()
    endToken?.let {
        Assertions.assertEquals(it, takeToken())
    }
    Assertions.assertFalse(hasNext())
}

fun <T: Token> TokenSource<T>.assertNotEnded(endToken: T) {
    Assertions.assertNotEquals(endToken, takeToken())
    Assertions.assertTrue(hasNext())
}

fun <T: Token> TokenSource<T>.assertToken(token: T) {
    Assertions.assertEquals(token, takeToken())
}

fun <T: Token> TokenSource<T>.assertTokens(vararg tokens: T) {
    tokens.forEach { assertToken(it) }
}

fun <T: Token> TokenSource<T>.takeTokens(cnt: Int) = repeat(cnt) { takeToken() }

fun <T: Token> ResultTree<T>.assertEquals(other: ResultTree<T>) {
    Assertions.assertEquals(term, other.term)
    Assertions.assertEquals(children.size, other.children.size)
    children.zip(other.children).forEach { (f, s) ->
        f.assertEquals(s)
    }
}

inline fun <reified P, reified T> withResult(
    source: String,
    handler: ResultTree<T>.() -> Unit,
) where P: AParser<T>, T: Token, T: Enum<T> {
    val parser = parser<P, T>()
    handler(parser.parse(source))
}