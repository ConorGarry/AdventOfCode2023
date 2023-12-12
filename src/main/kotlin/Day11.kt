import kotlin.math.abs

typealias Galaxy = Pair<Long, Long>

class Day11 {

    fun part1(input: List<String>, expansionScale: Long = 1): Long {
        val rowOffsets = input.indices.filter { '#' !in input[it] }
        val colOffsets = input[0].indices.filter { c -> input.none { it[c] == '#' } }

        val galaxies = input.flatMapIndexed { rowIndex, s ->
            s.mapIndexedNotNull { colIndex, c ->
                c.takeIf { it == '#' }?.run {
                    Galaxy(
                        (colIndex + colOffsets.count { colIndex > it }.toLong() * expansionScale),
                        (rowIndex + rowOffsets.count { rowIndex > it }.toLong() * expansionScale)
                    )
                }
            }
        }

        fun galaxyPairs(galaxies: List<Galaxy>): List<Pair<Galaxy, Galaxy>> =
            buildList {
                galaxies.forEachIndexed { index, galaxy ->
                    galaxies.subList(index + 1, galaxies.size).forEach { other ->
                        add(galaxy to other)
                    }
                }
            }

        return galaxyPairs(galaxies).sumOf {
            abs(it.first.first - it.second.first) + abs(it.first.second - it.second.second)
        }
    }

    fun part2(input: List<String>): Long {
        return part1(input, 999_999L).toLong()
    }
}

fun main() {
    val input = readInput("Day11")
    with(Day11()) {
        part1(input).println()
        part2(input).println()
    }
}
