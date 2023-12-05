data class Mapping(val destination: Long, val source: Long, val length: Long)

class Day05 {
    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ")[1].split(" ").map(String::toLong)
        fun MutableMap<String, List<Mapping>>.addEntry(entry: List<String>) {
            put(entry[0].removeSuffix(" map:"),
                entry.slice(1..<entry.size)
                    .map { it.split(" ").map(String::toLong) }
                    .map { (dest, src, size) -> Mapping(dest, src, size) }
            )
        }

        val alamanac = buildMap {
            val currentEntry = mutableListOf<String>()
            input.drop(2).forEach {
                if (it.isBlank()) {
                    if (currentEntry.isNotEmpty()) {
                        addEntry(currentEntry)
                        currentEntry.clear()
                    }
                } else {
                    currentEntry.add(it)
                }
                if (currentEntry.isNotEmpty()) {
                    addEntry(currentEntry)
                }
            }
        }

        fun Map<String, List<Mapping>>.mapFor(seed: Long, key: String): Long =
            this[key]!!.fold(seed) { acc, (d, s, l) ->
                when (seed) {
                    in s..<(s + l) -> acc + d - s
                    else -> acc
                }
            }

        return seeds.minOf { seed ->
            with(alamanac) {
                mapFor(seed, "seed-to-soil")
                    .run { mapFor(this, "soil-to-fertilizer") }
                    .run { mapFor(this, "fertilizer-to-water") }
                    .run { mapFor(this, "water-to-light") }
                    .run { mapFor(this, "light-to-temperature") }
                    .run { mapFor(this, "temperature-to-humidity") }
                    .run { mapFor(this, "humidity-to-location") }
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day05")
    with(Day05()) {
        part1(input).println()
        /*part2(input).println()*/
    }
}
