package mt.lab.sysoev.token

interface SymbolProvider {
    fun takeIf(cond: (Char) -> Boolean): Char?

    fun takeNext(cond: (Char) -> Boolean): Boolean = takeIf(cond) != null

    fun hasNext(): Boolean

    fun skipWs()
}

typealias TokenParser = (SymbolProvider) -> Boolean

fun stringParser(string: String): TokenParser = { provider ->
    var flag = true
    provider.skipWs()
    for (c in string) {
        if (!provider.hasNext() || !provider.takeNext { it == c }) {
            flag = false
            break
        }
    }
    flag
}

val endParser: TokenParser = {
    it.skipWs()
    !it.hasNext()
}

interface Token {
    val parser: TokenParser
    val stringValue: String
    val terminal: String

    fun tryParse(symbolProvider: SymbolProvider): Boolean {
        return parser(symbolProvider)
    }
}