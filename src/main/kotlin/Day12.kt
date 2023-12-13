class Day12 {
    fun part1(input: List<String>): Int {
        fun count(cfg: String, nums: List<Int>): Int {
            if (cfg.isEmpty()) return if (nums.isEmpty()) 1 else 0
            if (nums.isEmpty()) return if ('#' !in cfg) 1 else 0

            var result = 0
            if (cfg.first() in ".?") {
                result += count(cfg.substring(1), nums)
            }

            if (cfg.first() in "#?") { // Start of a group.
                if (nums.first() <= cfg.length && '.' !in cfg.substring(0, nums.first()) &&
                    (nums[0] == cfg.length || cfg[nums[0]] != '#')
                ) {
                    result += count(
                        nums.first().plus(1)
                            .takeIf { it < cfg.length }?.let(cfg::substring).orEmpty(),
                        nums.subList(1, nums.size)
                    )
                }
            }
            return result
        }

        return input.fold(0) { acc, line ->
            val (config, nums) = line.split(" ")
            val numList = nums.split(",").map { it.toInt() }
            acc + count(config, numList)
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day12")
    with(Day12()) {
        part1(input).println()
        /*part2(input).println()*/
    }
}
