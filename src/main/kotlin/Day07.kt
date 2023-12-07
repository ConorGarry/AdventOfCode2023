import Hand.Type.*

private val handComparator by lazy {
    val charValues = mapOf(
        'A' to 12, 'K' to 11, 'Q' to 10,
        'J' to 9, 'T' to 8, '9' to 7,
        '8' to 6, '7' to 5, '6' to 4,
        '5' to 3, '4' to 2, '3' to 1, '2' to 0
    )

    Comparator<Hand> { h1, h2 ->
        when (val comp = h1.type.value.compareTo(h2.type.value)) {
            0 -> {
                val comparator: (Char) -> Int = { charValues.getOrDefault(it, 0) }
                h1.cards.zip(h2.cards).map { (l, r) -> comparator(l).compareTo(comparator(r)) }.first { it != 0 }
            }

            else -> comp
        }
    }
}

data class Hand(
    val cards: List<Char>,
    val bid: Int,
) {

    enum class Type(val value: Int) {
        FiveOfAKind(7),
        FourOfAKind(6),
        FullHouse(5),
        ThreeOfAKind(4),
        TwoPair(3),
        OnePair(2),
        HighCard(1),
    }

    val type: Type
        get() {
            val charMap = buildMap<Char, Int> {
                for (char in cards) {
                    this[char] = getOrDefault(char, 0) + 1
                }
            }
            /*println("charMap: $charMap for $cards")*/
            return when (charMap.values.max()) {
                5 -> FiveOfAKind
                4 -> FourOfAKind
                3 -> if (cards.toSet().size == 2) FullHouse else ThreeOfAKind
                2 -> if (cards.toSet().size == 3) TwoPair else OnePair
                else -> HighCard
            }
        }
}

class Day07 {
    fun part1(input: List<String>): Int {
        val hands = input.map {
            it.split(" ").let { (cards, bid) ->
                Hand(cards.toList(), bid.toInt())
            }
        }
        return hands.sortedWith(handComparator)
            .foldIndexed(0) { i, sum, (_, bid) ->
                sum + bid * (i + 1)
            }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day07")
    with(Day07()) {
        part1(input).println() // 241344943
        /*part2(input).println()*/
    }
}