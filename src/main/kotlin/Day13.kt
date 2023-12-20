class Day13 {

    private fun List<String>.reflection() =
        zipWithNext { s, other -> s == other }
            .mapIndexedNotNull { idx, b -> idx.takeIf { b }?.inc() }
            .firstOrNull { lines ->
                take(lines).reversed().zip(drop(lines)) { a, b -> a == b }.all { it }
            } ?: 0


    fun part1(input: List<String>): Int =
        input.split { it.isEmpty() }.sumOf {
            100 * it.reflection() + it.transpose().reflection()
        }

    fun part2(input: List<String>): Int = input.split { it.isEmpty() }.sumOf {
        100 * it.smudgeReflection() + it.transpose().smudgeReflection()
    }
}

fun main() {
    val input = readInput("Day13")
    with(Day13()) {
        part1(input).println()
        part2(input).println()
    }
}

private fun zipAlmostEqual(first: List<String>, second: List<String>): Boolean {
    var almostEqual = false
    for (i in 0..minOf(first.lastIndex, second.lastIndex)) {
        val a = first[i]
        val b = second[i]
        val delta = (0..maxOf(a.lastIndex, b.lastIndex)).count { a.getOrNull(it) != b.getOrNull(it) }
        if (delta > 1) return false
        if (delta == 1) if (almostEqual) return false else almostEqual = true
    }
    return almostEqual
}

private fun Iterable<String>.transpose(): List<String> = buildList {
    val strings = this@transpose.filterTo(mutableListOf()) { it.isNotEmpty() }
    var i = 0
    while (strings.isNotEmpty()) {
        add(buildString(strings.size) { for (string in strings) append(string[i]) })
        i++
        strings.removeAll { it.length == i }
    }
}

fun List<String>.transposed() = first().indices.map {
    buildString { for (line in this@transposed) append(line[it]) }
}

fun String.smudges(other: String) = zip(other) { a, b -> a != b }.count { it }

fun List<String>.smudgeReflection() =
    zipWithNext(String::smudges)
        .mapIndexedNotNull { idx, i -> idx.takeIf { i <= 1 }?.inc() }
        .firstOrNull { lines ->
            take(lines).reversed().zip(drop(lines), String::smudges).sum() == 1
        } ?: 0