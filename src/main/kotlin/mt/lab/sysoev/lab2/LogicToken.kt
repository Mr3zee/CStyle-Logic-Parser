package mt.lab.sysoev.lab2

import mt.lab.sysoev.token.*

enum class LogicToken(
    override val stringValue: String,
    override val terminal: String,
    override val parser: (SymbolProvider) -> Boolean,
): Token {
    AND("&"),
    OR("|"),
    NOT("!"),
    XOR("^"),
    IMPL("->"),
    LPAR("("),
    RPAR(")"),
    VAR("term", "T", parser = {
        it.skipWs()
        it.hasNext() && it.takeNext { c -> c.isLetter() }
    }),
    END("", "$", endParser),
    ;

    constructor(string: String, terminal: String = string) : this(string, terminal, stringParser(string))
}

