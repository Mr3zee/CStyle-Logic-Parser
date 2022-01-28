package mt.lab.sysoev.test.lab2

import mt.lab.sysoev.lab2.*
import mt.lab.sysoev.parser.parser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import java.io.File
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class GraphLogicTest: LogicBaseTest() {
    @Test
    fun testRandomGraph(testInfo: TestInfo) = withGraph(testInfo.displayName, "(!a | b) & a & (a | !(b ^ c))")

    @Test
    fun testImpl(testInfo: TestInfo) = withGraph(testInfo.displayName, "a -> a -> a")

    private fun withGraph(
        name: String,
        source: String,
    )  {
        val parser = parser<LogicParser, LogicToken>()
        val result = parser.parse(source)
        println("Generated graph:")
        val graph = result.generateGraph()
        println(graph)
        generatePicture(name, graph)
    }

    private fun LogicResult.generateGraph(): String = buildString {
        val index = AtomicInteger(0)
        val map = mutableMapOf<LogicResult, Int>()
        append("digraph {\n")
        generateStringGraph(this@generateGraph, index, map)
        append("}")
    }

    private fun StringBuilder.generateStringGraph(
        result: LogicResult,
        index: AtomicInteger,
        map: MutableMap<LogicResult, Int>,
    ) {
        val currentIndex = index.getAndIncrement()
        map[result] = currentIndex
        append("$currentIndex")
        append("[label=\"${result.term.terminal}\"]\n")

        result.children.forEach {
            generateStringGraph(it, index, map)
            append("$currentIndex -> ${map[it]}\n")
        }
    }

    private fun generatePicture(name: String, graphString: String) {
        val file = File("$OUTPUT_DIR/$name.txt")

        file.createNewFile()

        println("Writing to file: ${file.absoluteFile}")

        file.writer(Charsets.UTF_8).use {
            it.write(graphString)
        }

        "dot -Tsvg ${file.name} -o $name.svg".runCommand(File(OUTPUT_DIR))
    }

    private fun String.runCommand(workingDir: File) {
        ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(30, TimeUnit.SECONDS)
    }

    companion object {
        private const val OUTPUT_DIR = "src/test/kotlin/mt/lab/sysoev/test/lab2/test_graphs"
    }
}
