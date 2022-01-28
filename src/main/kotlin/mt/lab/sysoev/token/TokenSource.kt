package mt.lab.sysoev.token

import java.io.IOException
import java.io.InputStream

class TokenSource<T: Token>(
    private val stream: InputStream,
    private val tokens: List<T>,
): SymbolProvider {
    private var char: Char = Char.MIN_VALUE
    private var posVal: Int = 0
    private var tokenVal: T? = null

    private var writeToBuffer = false
    private var buffer = mutableListOf<Char>()
    private var bufferPos = BUFFER_POS_START
    private var lastValueAdded = false

    val pos: Int
        get() = posVal - buffer.size

    var token: T?
        get() = tokenVal
        private set(new) {
            tokenVal = new
        }

    fun takeToken() = token?.also {
        nextToken()
    } ?: throw ParseException(pos, "no valid tokens found, illegal character: ${if (!hasNext()) "<END>" else char}")

    init {
        nextChar()
        nextToken()
    }

    private fun isBlank() = char.isWhitespace()

    private fun nextChar() {
        if (bufferPos < buffer.size) {
            char = buffer[bufferPos++]
            return
        }

        posVal++
        try {
            char = stream.read().let { if (it == -1) Char.MIN_VALUE else it.toChar() }

            if (writeToBuffer) {
                buffer.add(char)
                bufferPos++

                lastValueAdded = false
            }
        } catch (e: IOException) {
            throw ParseException(pos, e.message)
        }
    }

    private fun nextToken() {
        tokens.forEach {
            mark()
            if (it.tryParse(this)) {
                token = it
                accept()
                return
            }
            rollback()
        }
        token = null
    }

    private fun mark() {
        writeToBuffer = true

        if (!lastValueAdded) {
            buffer.add(char)
            bufferPos++

            lastValueAdded = true
        }
    }

    private fun rollback() {
        writeToBuffer = false
        bufferPos = BUFFER_POS_START
        lastValueAdded = true
        nextChar()
    }

    private fun accept() {
        writeToBuffer = false
        buffer = buffer.subList(bufferPos, buffer.size)

        bufferPos = BUFFER_POS_START
    }

    override fun skipWs() {
        while (isBlank()) {
            nextChar()
        }
    }

    override fun hasNext() = char != Char.MIN_VALUE

    override fun takeIf(cond: (Char) -> Boolean): Char? {
        return if (cond(char)) {
            char.also { nextChar() }
        } else null
    }

    companion object {
        private const val BUFFER_POS_START = 0
    }
}

inline fun <reified E> tokenSource(stream: InputStream): TokenSource<E> where E: Token, E: Enum<E> {
    return TokenSource(stream, enumValues<E>().toList())
}

inline fun <reified E> tokenSource(string: String): TokenSource<E> where E: Token, E: Enum<E> {
    return tokenSource(string.byteInputStream(Charsets.UTF_8))
}

class ParseException(val pos: Int, message: String? = null): RuntimeException(buildString {
    append("Exception occurred at pos $pos")
    message?.let {
        append(", message: $it")
    }
})