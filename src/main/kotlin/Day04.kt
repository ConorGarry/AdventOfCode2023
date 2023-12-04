data class Card(
    val id: Int,
    val winning: List<Int>,
    val entries: List<Int>,
)

fun getCards(input: List<String>): List<Card> =
    input.map {
        val splits = it.split(": ")
        val lists = splits[1].split(" | ")
        Card(
            splits[0].substringAfterLast(" ").toInt(),
            lists[0].split(" ").filter(String::isNotEmpty).map(String::toInt),
            lists[1].split(" ").filter(String::isNotEmpty).map(String::toInt),
        )
    }

fun Card.numWinners(): Int =
    with(entries.intersect(winning.toSet()).size) {
        if (this > 0) (1..<this).fold(1) { acc, _ -> acc * 2 } else 0
    }

class Day04 {
    fun part1(input: List<String>): Int { // 19855
        val cards = getCards(input)
        return cards.sumOf(Card::numWinners)
    }

    fun part2(input: List<String>): Long {
        val cards: List<Card> = getCards(input)
        val cartCounts = LongArray(cards.size) { 1 }
        println("cartCounts: ${cartCounts.size}")
        for (i in input.indices) {
            val card = cards[i]
            for (j in i + 1..i + card.numWinners()) {

                println("i: $i, j: $j, numwinner: ${card.numWinners()}")
                /*if (j == card.numWinners()) continue*/
                if (j >= cartCounts.size) {
                    continue
                }
                cartCounts[j] += cartCounts[i]
            }
        }
        return cartCounts.sum()
    }
}

fun main() {
    val input = readInput("Day04")
    with(Day04()) {
        /*part1(input).println()*/
        part2(input).println()
    }
}
