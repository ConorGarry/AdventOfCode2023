import kotlin.math.abs

data class NodeMap(val directions: String, val nodes: Map<String, Pair<String, String>>)
data class Traversal(var current: String, var sum: Int = 0)

fun lcm(a: Long, b: Long): Long {
    fun gcd(a: Long, b: Long): Long = if (b == 0L) abs(a) else gcd(b, a % b)
    return if (a == 0L || b == 0L) 0 else abs(a * b) / gcd(a, b)
}

fun lcmOfList(numbers: List<Long>): Long =
    numbers.fold(numbers.first()) { acc, i -> lcm(acc, i) }

class Day08 {

    fun parse(input: List<String>) =
        NodeMap(
            directions = input.first(),
            nodes = buildMap {
                input.slice(2..<input.size).forEach {
                    val (key, value1, value2) =
                        """(\w+) = \((\w+), (\w+)\)""".toRegex().matchEntire(it)!!.destructured
                    put(key, value1 to value2)
                }
            }
        )

    fun part1(input: List<String>): Int {
        val nodeMap = parse(input)
        var current = "AAA"
        var sum = 0
        var i = 0
        while (current != "ZZZ") {
            val idx = i++ % nodeMap.directions.length
            val direction = nodeMap.directions[idx]
            current = nodeMap.nodes[current]!!.let { (l, r) -> if (direction == 'L') l else r }
            sum++
        }
        return sum
    }

    fun part2(input: List<String>): Long =
        parse(input).let { nodeMap ->
            lcmOfList(
                nodeMap.nodes.keys.filter { it.endsWith('A') }
                    .map { Traversal(it) }.map {
                        var i = 0
                        while (!it.current.endsWith('Z')) {
                            val idx = i++ % nodeMap.directions.length
                            val direction = nodeMap.directions[idx]
                            it.current = nodeMap.nodes[it.current]!!.let { (l, r) ->
                                if (direction == 'L') l else r
                            }
                            it.sum++
                        }
                        it.sum.toLong()
                    }
            )
        }
}

fun main() {
    val input = readInput("Day08")
    with(Day08()) {
        part1(input).println()
        part2(input).println()
    }
}
