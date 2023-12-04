fun main() {
    fun part1(input: List<String>): Int =
        input.fold(0) { sum, line ->
            sum +
                line.first { it.isDigit() }.digitToInt() * 10 +
                line.last { it.isDigit() }.digitToInt()
        }

    val numMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )

    fun part2(input: List<String>): Int =
        input.fold(0) { sum, line ->
            line.firstAndLastMatch(
                "(${((1..9) + numMap.keys).joinToString("|")})".toRegex()
            ).let { (firstMatch, lastMatch) ->
                with(firstMatch) {
                    if (length == 1) first().digitToInt() else numMap[this] ?: 0
                } to with(lastMatch) {
                    if (length == 1) first().digitToInt() else numMap[this] ?: 0
                }
            }.let { (first, last) ->
                sum + first * 10 + last
            }
        }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

/**
 * Find the first and last items from Regex result groups and return them as a Pair.
 * If no result, return pair of "0".
 * Can't use findAll because it doesn't account for overlaps (at least not that I can see).
 */
fun String.firstAndLastMatch(regex: Regex): Pair<String, String> =
    generateSequence(regex.find(this, 0)) {
        regex.find(this, it.range.first + 1)
    }.toList()
        .takeIf { it.isNotEmpty() }
        ?.run { first().value to last().value }
        ?: ("0" to "0")
