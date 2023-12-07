import Type.*

val charValues = mapOf(
    'A' to 14, 'K' to 13, 'Q' to 12,
    'J' to 11, 'T' to 10, '9' to 9,
    '8' to 8, '7' to 7, '6' to 6,
    '5' to 5, '4' to 4, '3' to 3, '2' to 2
)

fun comparatorFor(type: (List<Char>) -> Type, values: Map<Char, Int>): Comparator<Hand> =
    Comparator { h1, h2 ->
        when (val comp = type(h1.cards).value.compareTo(type(h2.cards).value)) {
            0 -> {
                val comparator: (Char) -> Int = { values.getOrDefault(it, 0) }
                h1.cards.zip(h2.cards).map { (l, r) ->
                    comparator(l).compareTo(comparator(r))
                }.first { it != 0 }
            }

            else -> comp
        }
    }

enum class Type(val value: Int) {
    FiveOfAKind(7),
    FourOfAKind(6),
    FullHouse(5),
    ThreeOfAKind(4),
    TwoPair(3),
    OnePair(2),
    HighCard(1),
}

fun List<Char>.unique() = distinct().size

fun typeForCards(cards: List<Char>): Type =
    when (buildCharMap(cards).values.max()) {
        5 -> FiveOfAKind
        4 -> FourOfAKind
        3 -> if (cards.unique() == 2) FullHouse else ThreeOfAKind
        2 -> if (cards.unique() == 3) TwoPair else OnePair
        else -> HighCard
    }

fun typeForCardsWithJoker(cards: List<Char>): Type {
    val map = buildCharMap(cards)
    fun Map<Char, Int>.anyJokerAvailable() = containsKey('J') || (this['J'] != null)
    fun Map<Char, Int>.availableJokers() = this['J'].takeIf { it != null } ?: 0
    return when (map.values.max()) {
        5 -> FiveOfAKind
        4 ->
            if (map['J'] == 4) {
                FiveOfAKind
            } else {
                if (map.anyJokerAvailable()) FiveOfAKind else FourOfAKind
            }

        3 ->
            if (map['J'] == 3) {
                if (cards.unique() == 2) FiveOfAKind else FourOfAKind
            } else {
                val jokers = map.availableJokers()
                if (jokers > 0) {
                    if (jokers == 2) FiveOfAKind else FourOfAKind
                } else {
                    if (cards.unique() == 2) FullHouse else ThreeOfAKind
                }
            }

        2 ->
            if (map['J'] == 2) {
                when (cards.unique()) {
                    4 -> ThreeOfAKind
                    3 -> FourOfAKind
                    else -> OnePair
                }
            } else {
                when (map.availableJokers()) {
                    3 -> FiveOfAKind
                    2 -> FourOfAKind
                    1 -> if (cards.unique() == 3) FullHouse else ThreeOfAKind
                    else -> {
                        if (cards.unique() == 3) TwoPair else OnePair
                    }
                }
            }

        else -> if (map['J'] == 1 && map.anyJokerAvailable()) OnePair else HighCard
    }
}

private fun buildCharMap(cards: List<Char>): Map<Char, Int> =
    buildMap<Char, Int> {
        for (char in cards) {
            this[char] = getOrDefault(char, 0) + 1
        }
    }

data class Hand(val cards: List<Char>, val bid: Int)

class Day07 {

    val parse: (List<String>) -> List<Hand> = {
        it.map { line ->
            line.split(" ").let { (cards, bid) ->
                Hand(cards.toList(), bid.toInt())
            }
        }
    }

    fun part1(input: List<String>): Int =
        parse(input).sortedWith(comparatorFor(::typeForCards, charValues))
            .foldIndexed(0) { i, sum, (_, bid) ->
                sum + bid * (i + 1)
            }

    fun part2(input: List<String>): Int =
        parse(input).sortedWith(
            comparatorFor(
                ::typeForCardsWithJoker,
                (charValues as MutableMap<Char, Int>).apply { this['J'] = 0 })
        ).foldIndexed(0) { i, sum, (_, bid) ->
            sum + bid * (i + 1)
        }
}

fun main() {
    val input = readInput("Day07")
    with(Day07()) {
        part1(input).println()
        part2(input).println()
    }
}