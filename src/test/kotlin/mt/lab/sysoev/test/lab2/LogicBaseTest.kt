package mt.lab.sysoev.test.lab2

import mt.lab.sysoev.lab2.*

open class LogicBaseTest {
    protected fun single(token: LogicToken): LogicResult = LogicResult(token)

    protected fun simpleNode(op: LogicToken, vararg tokens: LogicToken): LogicResult {
        return LogicResult(op, tokens.map { LogicResult(it) })
    }

    protected fun chainNode(op: LogicToken, times: Int, last: LogicToken): LogicResult {
        return if (times == 0) LogicResult(last) else LogicResult(op, chainNode(op, times - 1, last))
    }

    protected fun chainBinaryNode(op: LogicToken, times: Int, term: LogicToken): LogicResult {
        return if (times == 0)
            LogicResult(term)
        else
            LogicResult(op, LogicResult(term), chainBinaryNode(op, times - 1, term))
    }

    protected fun prefixNode(vararg tokens: LogicToken) = prefixNode(tokens.toMutableList())

    protected fun prefixNode(tokens: MutableList<LogicToken>): LogicResult {
        return when (val first = tokens.removeFirst()) {
            LogicToken.VAR -> single(LogicToken.VAR)
            LogicToken.NOT -> LogicResult(first, prefixNode(tokens))
            else -> LogicResult(first, prefixNode(tokens), prefixNode(tokens))
        }
    }

    protected inline fun withResult(
        source: String,
        handler: LogicResult.() -> Unit,
    ) = mt.lab.sysoev.test.util.withResult<LogicParser, LogicToken>(source, handler)
}