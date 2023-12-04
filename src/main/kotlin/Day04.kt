data class Card(val id: Int, val winning: List<Int>, val entries: List<Int>)

fun getCards(input: List<String>): List<Card> =
    input.map {
        val splits = it.split(": ")
        val (winners, entries) = splits[1].split(" | ")
            .map { it.split(" ").filter(String::isNotEmpty).map(String::toInt) }
        Card(
            splits[0].substringAfterLast(" ").toInt(),
            winners,
            entries,
        )
    }

val Card.numWinners get() = entries.count { it in winning }

class Day04 {
    fun part1(input: List<String>): Int =
        getCards(input).sumOf(Card::numWinners)

    fun part2(input: List<String>): Int {
        val cards = getCards(input)
        val counts = IntArray(cards.size) { 1 }
        for (i in input.indices) {
            for (n in i + 1..<i + cards[i].numWinners + 1) {
                counts[n] += counts[i]
            }
        }
        return counts.sum()
    }
}

fun main() {
    val input = readInput("Day04")
    with(Day04()) {
        part1(input).println()
        part2(input).println()
    }
}
