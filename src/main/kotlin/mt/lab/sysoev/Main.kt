package mt.lab.sysoev

import mt.lab.sysoev.lab2.LogicParser
import mt.lab.sysoev.lab2.LogicToken
import mt.lab.sysoev.parser.parser

fun main() {
    val parser = parser<LogicParser, LogicToken>()

    println(parser.parse(""))
}