package mt.lab.sysoev.parser

import mt.lab.sysoev.token.Token

data class ResultTree<T: Token>(
    val term: T,
    val children: List<ResultTree<T>> = listOf()
) {
    constructor(term: T, vararg children: ResultTree<T>): this(term, children.toList())
}