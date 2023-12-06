class Day06 {

    fun numsFromString(s: String) = "\\d+".toRegex().findAll(s).map { it.value.toInt() }.toList()

    fun parseInput(input: List<String>) = input.let { (times, distances) ->
        numsFromString(times) to (numsFromString(distances))
    }

    fun part1(input: List<String>): Int {
        val (times, distances) = parseInput(input)
        var sum = 1
        times.indices.forEach {
            var result = 0
            val total = times[it]
            val distance = distances[it]
            (1..total).forEach { pressMs ->
                val dist = (total - pressMs) * pressMs
                if (dist > distance) {
                    result++
                }
            }
            sum *= result
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val (times, distances) = parseInput(input)
        val time = times.joinToString("").toLong()
        val distance = distances.joinToString("").toLong()
        var result = 0
        (1..time).forEach { pressMs ->
            val dist = (time - pressMs) * pressMs
            if (dist > distance) {
                result++
            }
        }
        return input.size
    }
}

fun main() {
    val input = readInput("Day06")
    with(Day06()) {
        part1(input).println()
        part2(input).println()
    }
}
