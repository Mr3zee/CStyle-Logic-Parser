package mt.lab.sysoev.test.util

import mt.lab.sysoev.token.*

interface TestToken: Token {
    override val stringValue: String
        get() = "test_token"

    override val terminal: String
        get() = "test_terminal"
}

enum class EndToken(override val parser: (SymbolProvider) -> Boolean): TestToken {
    END(endParser);
}

enum class AnyStringToken(override val parser: (SymbolProvider) -> Boolean): TestToken {
    TOKEN(parser = {
        var flag = false
        while (it.hasNext() && it.takeNext { c -> c.isLetter() }) {
            flag = true
        }
        flag
    }),
    END(endParser);
}

enum class OneToken(override val parser: TokenParser): TestToken {
    TOKEN("token");

    constructor(string: String): this(stringParser(string))
}

enum class MultipleToken(override val parser: TokenParser): TestToken {
    TOKEN1("token1"),
    TOKEN2("token2"),
    TOKEN3("token3"),
    TOKEN4("token4"),
    ;

    constructor(string: String): this(stringParser(string))
}
