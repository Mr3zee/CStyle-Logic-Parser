package mt.lab.sysoev.test

import mt.lab.sysoev.test.util.*
import org.junit.jupiter.api.Test


class TokenSourceTest {
    @Test
    fun testEndSource() = withSource<EndToken>("") {
        assertEnded(EndToken.END)
    }

    @Test
    fun testEndWs() = withSource<EndToken>("   ") {
        assertEnded(EndToken.END)
    }

    @Test
    fun testNotEmpty() = withSource<AnyStringToken>("abcd  ") {
        assertToken(AnyStringToken.TOKEN)
        assertEnded(AnyStringToken.END)
    }

    @Test
    fun testOneToken() = withSource<OneToken>("token") {
        assertToken(OneToken.TOKEN)
        assertEnded()
    }

    @Test
    fun testMultipleTokens() = withSource<MultipleToken>("  token2 \ntoken1token3    token4 ") {
        assertTokens(MultipleToken.TOKEN2, MultipleToken.TOKEN1, MultipleToken.TOKEN3, MultipleToken.TOKEN4)
        assertEnded()
    }

    @Test
    fun testMultipleTokens2() = withSource<MultipleToken>("token2token2token2token2") {
        assertTokens(MultipleToken.TOKEN2, MultipleToken.TOKEN2, MultipleToken.TOKEN2, MultipleToken.TOKEN2)
        assertEnded()
    }

    @Test
    fun testFailEmpty() = withSourceFails<OneToken>("", 0) {
        takeToken()
    }

    @Test
    fun testFailMisspell() = withSourceFails<OneToken>("tokeen1", 0) {
        takeToken()
    }

    @Test
    fun testFailMisspellMultiple() = withSourceFails<MultipleToken>("token2oken2token2token2", 6) {
        takeTokens(4)
    }
}