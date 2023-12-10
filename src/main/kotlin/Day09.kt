class Day09 {

    fun parse(input: List<String>, reverse: Boolean = false): List<List<Int>> =
        input.map {
            it.split(" ").map(String::trim).map(String::toInt).run {
                if (reverse) this.reversed() else this
            }
        }

    fun processLine(line: List<Int>): Int =
        line.fold(Pair(line, 0)) { (current, acc), _ ->
            val diffs = current.zipWithNext().map { (s1, s2) -> s2 - s1 }
            if (diffs.all { it == 0 }) {
                emptyList<Int>() to acc + (current.lastOrNull() ?: 0)
            } else {
                diffs to acc + (current.lastOrNull() ?: 0)
            }
        }.second

    fun part1(input: List<String>): Int =
        parse(input).sumOf(::processLine)

    fun part2(input: List<String>): Int =
        parse(input.reversed()).sumOf(::processLine)
}

fun main() {
    val input = readInput("Day09")
    with(Day09()) {
        part1(input).println()
        part2(input).println()
    }
}
