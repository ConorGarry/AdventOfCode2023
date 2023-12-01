fun main() {
    fun part1(input: List<String>): Int =
        input.fold(0) { acc, i ->
            acc + i.firstAndLastDigitInt()
        }

    fun part2(input: List<String>): Int {
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

        val keys = numMap.keys
        val regex = buildString {
            append("(")
            append(((1..9) + keys).joinToString("|"))
            append(")")
        }.toRegex()

        return input.fold(0) { acc, s ->
            val (first, last) = s.firstAndLastMatch(regex)
            val left = if (first.length == 1) { // This means it's a digit.
                first.first().digitToInt()
            } else {
                numMap[first].orZero()
            }
            val right = if (last.length == 1) {
                last.first().digitToInt()
            } else {
                numMap[last].orZero()
            }

            acc + intArrayOf(left, right).toInt()
        }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

// Extensions
/**
 * Take first and last digits in a String and concatenate them in number form.
 */
fun String.firstAndLastDigitInt(): Int =
    first { it.isDigit() }.digitToInt() * 10 + last { it.isDigit() }.digitToInt()

// Concat thw digits of an IntArray to number form.
fun IntArray.toInt(): Int = fold(0) { acc, i -> acc * 10 + i }

// Cleaner than always having to use elvis operator.
fun Int?.orZero(): Int = this ?: 0

/**
 * Find the first and last items from Regex result groups and return them as a Pair.
 * If no result, return pair of "0".
 */
fun String.firstAndLastMatch(regex: Regex): Pair<String, String> =
    regex.find(this, 0)?.let { result ->
        val first = result.value
        var startIndex = 0
        lateinit var last: String
        while (true) {
            val match = regex.find(this, startIndex) ?: break
            last = match.value
            startIndex = match.range.first + 1
        }
        first to last
    } ?: ("0" to "0")
