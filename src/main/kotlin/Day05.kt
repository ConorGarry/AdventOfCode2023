class Day05 {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day05")
    with(DayNN()) {
        part1(input).println()
        part2(input).println()
    }
}
