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

        val cache = mutableMapOf<Pair<Long, String>, Long>()


        /*return seeds.minOf { seed ->*/
        return seeds.let { (s1, e1, s2, e2) -> (s1..s1 + e1).toList() + (s2..s2 + e2).toList() }.map { seed ->
            /*.minOf { seed ->*/
            with(alamanac) {
                mapFor(seed, "seed-to-soil")
                    .run {
                        cache.getOrPut(this to "soil-to-fertilizer") {
                            mapFor(this, "soil-to-fertilizer")
                        }
                    }
                    .run {
                        cache.getOrPut(this to "fertilizer-to-water") {
                            mapFor(this, "fertilizer-to-water")
                        }
                    }
                    .run {
                        cache.getOrPut(this to "water-to-light") {
                            mapFor(this, "water-to-light")
                        }
                    }
                    .run {
                        cache.getOrPut(this to "light-to-temperature") {
                            mapFor(this, "light-to-temperature")
                        }
                    }
                    .run {
                        cache.getOrPut(this to "temperature-to-humidity") {
                            mapFor(this, "temperature-to-humidity")
                        }
                    }
                    .run {
                        cache.getOrPut(this to "humidity-to-location") {
                            mapFor(this, "humidity-to-location")
                        }
                    }
            }
        }.first()

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
