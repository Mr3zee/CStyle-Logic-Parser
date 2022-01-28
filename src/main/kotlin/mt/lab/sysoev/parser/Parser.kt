package mt.lab.sysoev.parser

import mt.lab.sysoev.token.Token
import mt.lab.sysoev.token.TokenSource
import java.io.InputStream

interface Parser<T: Token> {
    fun parse(stream: InputStream): ResultTree<T>

    fun parse(string: String): ResultTree<T> = parse(string.byteInputStream(Charsets.UTF_8))
}

abstract class AParser<T: Token>(tokens: List<T>): Parser<T> {
    protected lateinit var tokenSource: TokenSource<T>
    private val initSource: (InputStream) -> TokenSource<T> = { TokenSource(it, tokens) }

    override fun parse(stream: InputStream): ResultTree<T> {
        tokenSource = initSource(stream)

        with(tokenSource) {
            return startParse()
        }
    }

    protected abstract fun TokenSource<T>.startParse(): ResultTree<T>
}

inline fun <reified P, reified T> parser(): P where P: AParser<T>, T: Token, T: Enum<T> {
    return P::class.constructors.first().call(enumValues<T>().toList())
}