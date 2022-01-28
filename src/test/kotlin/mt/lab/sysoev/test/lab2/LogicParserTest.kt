package mt.lab.sysoev.test.lab2

import mt.lab.sysoev.lab2.*
import mt.lab.sysoev.test.util.assertEquals
import org.junit.jupiter.api.Test

class LogicParserTest: LogicBaseTest() {
    @Test
    fun testTerm() = withResult("a") {
        assertEquals(single(LogicToken.VAR))
    }

    @Test
    fun testOr() = withResult("a|b") {
        assertEquals(simpleNode(LogicToken.OR, LogicToken.VAR, LogicToken.VAR))
    }

    @Test
    fun testTwoOr() = withResult("a|b|c") {
        assertEquals(chainBinaryNode(LogicToken.OR, 2, LogicToken.VAR))
    }

    @Test
    fun testMultipleOr() = withResult("a|a|a|a|a|a|a|a|a") {
        assertEquals(chainBinaryNode(LogicToken.OR, 8, LogicToken.VAR))
    }

    @Test
    fun testXor() = withResult("a^b") {
        assertEquals(simpleNode(LogicToken.XOR, LogicToken.VAR, LogicToken.VAR))
    }

    @Test
    fun testTwoXor() = withResult("a^b^c") {
        assertEquals(chainBinaryNode(LogicToken.XOR, 2, LogicToken.VAR))
    }

    @Test
    fun testMultipleXor() = withResult("a^a^a^a^a^a^a^a^a") {
        assertEquals(chainBinaryNode(LogicToken.XOR, 8, LogicToken.VAR))
    }

    @Test
    fun testImpl() = withResult("a->b") {
        assertEquals(chainBinaryNode(LogicToken.IMPL, 1, LogicToken.VAR))
    }

    @Test
    fun testTwoImpl() = withResult("a->b->c") {
        assertEquals(chainBinaryNode(LogicToken.IMPL, 2, LogicToken.VAR))
    }

    @Test
    fun testMultipleImpl() = withResult("a->a->a->a->a->a->a->a->a") {
        assertEquals(chainBinaryNode(LogicToken.IMPL, 8, LogicToken.VAR))
    }

    @Test
    fun testAnd() = withResult("a&b") {
        assertEquals(chainBinaryNode(LogicToken.AND, 1, LogicToken.VAR))
    }

    @Test
    fun testTwoAnd() = withResult("a&b&c") {
        assertEquals(chainBinaryNode(LogicToken.AND, 2, LogicToken.VAR))
    }

    @Test
    fun testMultipleAnd() = withResult("a&a&a&a&a&a&a&a&a") {
        assertEquals(chainBinaryNode(LogicToken.AND, 8, LogicToken.VAR))
    }

    @Test
    fun testNot() = withResult("!b") {
        assertEquals(simpleNode(LogicToken.NOT, LogicToken.VAR))
    }

    @Test
    fun testTwoNot() = withResult("!!b") {
        assertEquals(chainNode(LogicToken.NOT, 2, LogicToken.VAR))
    }

    @Test
    fun testManyNot() = withResult("!!!!!!!!b") {
        assertEquals(chainNode(LogicToken.NOT, 8, LogicToken.VAR))
    }

    @Test
    fun testParenthesis() = withResult("(a)") {
        assertEquals(single(LogicToken.VAR))
    }

    @Test
    fun testMultipleParenthesis() = withResult("(((((((a)))))))") {
        assertEquals(single(LogicToken.VAR))
    }

    @Test
    fun testNotParenthesis() = withResult("!(a)") {
        assertEquals(simpleNode(LogicToken.NOT, LogicToken.VAR))
    }

    @Test
    fun testPrefixNode() = withResult("!(a)") {
        assertEquals(prefixNode(LogicToken.NOT, LogicToken.VAR))
    }

    @Test
    fun testPriorities() = withResult("a|b&c") {
        assertEquals(prefixNode(LogicToken.OR, LogicToken.VAR, LogicToken.AND, LogicToken.VAR, LogicToken.VAR))
    }

    @Test
    fun testPriorities2() = withResult("a & b ^ c | a & ! b") {
        assertEquals(prefixNode(
            LogicToken.OR,
                LogicToken.XOR,
                    LogicToken.AND,
                        LogicToken.VAR,
                        LogicToken.VAR,
                    LogicToken.VAR,
                LogicToken.AND,
                    LogicToken.VAR,
                    LogicToken.NOT,
                        LogicToken.VAR,
        ))
    }

    @Test
    fun testRandom() = withResult("(!a | b) & a & (a | !(b ^ c))") {
        assertEquals(prefixNode(
            LogicToken.AND,
                LogicToken.OR,
                    LogicToken.NOT,
                        LogicToken.VAR,
                    LogicToken.VAR,
                LogicToken.AND,
                    LogicToken.VAR,
                    LogicToken.OR,
                        LogicToken.VAR,
                        LogicToken.NOT,
                            LogicToken.XOR,
                                LogicToken.VAR,
                                LogicToken.VAR,
        ))
    }
}