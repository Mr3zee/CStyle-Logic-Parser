package mt.lab.sysoev.lab2

import mt.lab.sysoev.parser.AParser
import mt.lab.sysoev.parser.ResultTree
import mt.lab.sysoev.token.ParseException
import mt.lab.sysoev.token.TokenSource

typealias LogicResult = ResultTree<LogicToken>
typealias LogicSource = TokenSource<LogicToken>

class LogicParser(tokens: List<LogicToken>): AParser<LogicToken>(tokens) {
    override fun LogicSource.startParse(): LogicResult {
        return I().also {
            if (takeToken() != LogicToken.END) {
                throw ParseException(pos, "Expected end of expression")
            }
        }
    }

    private fun LogicSource.binary(levelToken: LogicToken, nextLevel: LogicSource.() -> LogicResult): LogicResult {
        return nextLevel().let {
            if (hasNext() && token == levelToken) {
                takeToken()

                LogicResult(levelToken, it, binary(levelToken, nextLevel))
            } else it
        }
    }

    private fun LogicSource.I() = binary(LogicToken.IMPL) { S() }

    private fun LogicSource.S() = binary(LogicToken.OR) { O() }

    private fun LogicSource.O() = binary(LogicToken.XOR) { X() }

    private fun LogicSource.X() = binary(LogicToken.AND) { A() }

    private fun LogicSource.A(): LogicResult {
        return when(token) {
            LogicToken.NOT -> {
                takeToken()
                LogicResult(LogicToken.NOT, A())
            }
            else -> E()
        }
    }

    private fun LogicSource.E(): LogicResult {
        return when(token) {
            LogicToken.LPAR -> {
                takeToken()
                val retval = I()
                if (takeToken() == LogicToken.RPAR) {
                    return retval
                }
                throw ParseException(pos, "Expected ${LogicToken.RPAR}, found: $token")
            }
            else -> T()
        }
    }

    private fun LogicSource.T(): LogicResult {
        when (token) {
            LogicToken.VAR -> {
                takeToken()
                return LogicResult(LogicToken.VAR)
            }
            else -> throw ParseException(pos, "Expected ${LogicToken.VAR}, found: $token")
        }
    }
}