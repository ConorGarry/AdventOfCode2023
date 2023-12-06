import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

data class Mapping(val destination: Long, val source: Long, val length: Long)

class Day05 {
    fun MutableMap<String, List<Mapping>>.addEntry(entry: List<String>) {
        put(
            entry[0].removeSuffix(" map:"),
            entry.slice(1..<entry.size)
                .map { it.split(" ").map(String::toLong) }
                .map { (dest, src, size) -> Mapping(dest, src, size) }
        )
    }

    fun List<String>.seeds(): List<Long> =
        first().split(": ")[1].split(" ").map(String::toLong)

    private var _almanac: Map<String, List<Mapping>>? = null
    fun List<String>.buildAlmanac(): Map<String, List<Mapping>> =
        _almanac ?: buildMap {
            val currentEntry = mutableListOf<String>()
            drop(2).forEach {
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
        }.also { _almanac = it }

    fun Map<String, List<Mapping>>.mapFor(seed: Long, key: Int): Long =
        values.toList()[key].fold(seed) { acc, (d, s, l) ->
            when (seed) {
                in s..<(s + l) -> acc + d - s
                else -> acc
            }
        }

    fun Map<String, List<Mapping>>.minLoc(seeds: List<Long>) =
        seeds.minOf { seed ->
            var idx = 0
            mapFor(seed, idx++)
                .run { mapFor(this, idx++) }
                .run { mapFor(this, idx++) }
                .run { mapFor(this, idx++) }
                .run { mapFor(this, idx++) }
                .run { mapFor(this, idx++) }
                .run { mapFor(this, idx++) }
        }

    fun part1(input: List<String>): Long =
        input.buildAlmanac().minLoc(input.seeds())

    fun part2(input: List<String>): Long {
        // X: 58880743
        //    2783212609
        val atomicLong = AtomicLong(Long.MAX_VALUE)
        val alamanac = input.buildAlmanac()
        /*val minBlock =*/
        val minBlock = runBlocking {
            input.seeds().let { (s1, e1, s2, e2) ->
                (
                        (s1..<s1 + e1).asSequence() +
                                (s2..<s2 + e2).asSequence()
                        )
            }
                .chunked(10).asIterable()
                /*.minOf { alamanac.minLoc(it) }*/
                .map {
                    async(Dispatchers.Default) {
                        alamanac.minLoc(it)
                            /*.also {
                                if (atomicLong.get() != it) {
                                    println("newMin: $it")
                                }
                            }*/
                    }
                }.awaitAll().min()
        }

        println("minLoc: $minBlock")
        return minBlock
    }
}

fun main() {
    val input = readInput("Day05")
    with(Day05()) {
        part1(input).println()
        part2(input).println()
    }
}
