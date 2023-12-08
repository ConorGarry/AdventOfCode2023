import kotlin.math.abs

data class NodeMap(val directions: String, val nodes: Map<String, Pair<String, String>>)

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
                    val (key, left, right) =
                        """(\w+) = \((\w+), (\w+)\)"""
                            .toRegex().matchEntire(it)!!.destructured
                    put(key, left to right)
                }
            }
        )

    fun countSteps(
        start: String,
        nodeMap: NodeMap,
        until: (String) -> Boolean
    ): Long {
        var sum = 0L
        var i = 0
        var current = start
        while (until(current)) {
            val idx = i++ % nodeMap.directions.length
            val direction = nodeMap.directions[idx]
            current = nodeMap.nodes[current]!!
                .let { (l, r) -> if (direction == 'L') l else r }
            sum++
        }
        return sum
    }

    fun part1(input: List<String>): Long =
        countSteps("AAA", parse(input), until = { it != "ZZZ" })

    fun part2(input: List<String>): Long =
        parse(input).let { nodeMap ->
            lcmOfList(
                nodeMap.nodes.keys
                    .filter { it.endsWith('A') }
                    .map { start ->
                        countSteps(start, nodeMap, until = { !it.endsWith('Z') })
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
