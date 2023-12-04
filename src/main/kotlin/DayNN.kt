class DayNN {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("DayNN")
    with(DayNN()) {
        part1(input).println()
        part2(input).println()
    }
}
