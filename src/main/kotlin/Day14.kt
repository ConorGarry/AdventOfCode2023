class Day14 {

    fun Array<CharArray>.tilt(direction: Char) {
        when (direction) {
            'n' -> {
                (0..<first().size).forEach { c ->
                    var changes = true
                    while (changes) {
                        changes = false
                        (1..<size).forEach { r ->
                            if (this[r][c] == 'O' && this[r - 1][c] == '.') {
                                this[r][c] = '.'
                                this[r - 1][c] = 'O'
                                changes = true
                            }
                        }
                    }
                }
            }

            's' -> {
                (0..<first().size).forEach { c ->
                    var changes = true
                    while (changes) {
                        changes = false
                        (0..<size - 1).forEach { r ->
                            if (this[r][c] == 'O' && this[r + 1][c] == '.') {
                                this[r][c] = '.'
                                this[r + 1][c] = 'O'
                                changes = true
                            }
                        }
                    }
                }
            }

            'e' -> {
                indices.forEach { r ->
                    var changes = true
                    while (changes) {
                        changes = false
                        (0..<this[r].size - 1).forEach { c ->
                            if (this[r][c] == 'O' && this[r][c + 1] == '.') {
                                this[r][c] = '.'
                                this[r][c + 1] = 'O'
                                changes = true
                            }
                        }
                    }
                }
            }

            'w' -> {
                indices.forEach { r ->
                    var changes = true
                    while (changes) {
                        changes = false
                        (1..<this[r].size).forEach { c ->
                            if (this[r][c] == 'O' && this[r][c - 1] == '.') {
                                this[r][c] = '.'
                                this[r][c - 1] = 'O'
                                changes = true
                            }
                        }
                    }
                }
            }
        }
    }

    fun parse(input: List<String>): Array<CharArray> {
        val grid = Array(input.size) { CharArray(input.first().length) }
        input.forEachIndexed { r, row ->
            row.forEachIndexed { c, col -> grid[r][c] = col }
        }
        return grid
    }

    fun part1(input: List<String>): Int {
        // input to Array of CharArray
        val grid = parse(input)
        input.forEach(::println)
        println("")
        grid.tilt('w')
        println("")
        grid.printGrid()
        return grid.sumOf {
            it.count { it == 'O' } * (grid.size - grid.indexOf(it))
        }
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)

        fun doCycle() {
            grid.tilt('n')
            grid.tilt('w')
            grid.tilt('s')
            grid.tilt('e')
        }

        val cache = mutableSetOf<Int>()
        val results = mutableListOf<Array<CharArray>>()
        var cycle = 0
        while (true) {
            val hash = grid.contentDeepHashCode()
            if (cache.contains(hash)) break
            cache.add(hash)
            results.add(grid.map { it.copyOf() }.toTypedArray())
            doCycle()
            cycle++
        }

        val first = cache.indexOf(grid.contentDeepHashCode())
        val res = results[(1000000000 - first) % (cycle - first) + first]
        var idx = res.size
        return res.sumOf {
            it.count { c -> c == 'O' } * (idx--)
        }
    }
}

fun main() {
    val input = readInput("Day14")
    with(Day14()) {
        part1(input).println()
        part2(input).println()
    }
}
