class Day06 {

    fun numsFromString(s: String) = "\\d+".toRegex().findAll(s).map { it.value.toInt() }.toList()

    fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> = input.let { (times, distances) ->
        numsFromString(times) to (numsFromString(distances))
    }

    fun part1(input: List<String>): Int =
        parseInput(input).first.zipWithNext().fold(1) { acc, (time, distance) ->
            var res = 0
            (1..time).forEach { press ->
                val d = (time - press) * press
                if (d > distance) {
                    res++
                }
            }
            acc * res
        }

    fun part2(input: List<String>): Int {
        val (time, distance) =
            parseInput(input).toList().map { it.joinToString("").toLong() }
        var res = 0
        (1..time).forEach { pressMs ->
            val dist = (time - pressMs) * pressMs
            if (dist > distance) {
                res++
            }
        }
        return res
    }
}

fun main() {
    val input = readInput("Day06")
    with(Day06()) {
        part1(input).println()
        part2(input).println()
    }
}
