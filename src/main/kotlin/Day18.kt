import Direction.*
import kotlin.math.max

class Day18 {

    fun parse(input: List<String>): List<Triple<Direction, Int, String>> =
        input.map {
            it.split(" ").let { (a, b, c) ->
                Triple(
                    when (a) {
                        "U" -> Up
                        "D" -> Down
                        "L" -> Left
                        "R" -> Right
                        else -> error("Unknown direction $a")
                    },
                    b.toInt(),
                    c
                )
            }
        }


    fun solve(input: List<String>): Int {
        val digPlan = parse(input)
        val (width, height) = digPlan.runningFold(1 to 1) { acc, (d, count, _) ->
            when (d) {
                Right -> acc.first + count to acc.second
                Left -> acc.first - count to acc.second
                Down -> acc.first to acc.second + count
                Up -> acc.first to acc.second - count
                else -> acc
            }
        }.maxBy { it.first * it.second }
        val grid = Array(height * 3) { CharArray(width * 3) { '.' } }
        val pos = intArrayOf(grid.size / 2, grid[0].size / 2)
        var sum = 0
        digPlan.forEach { (dir, steps, color) ->
            when (dir) {
                Up -> {
                    repeat((0..<steps).count()) {
                        grid[pos[0] - it][pos[1]] = color[1]
                    }
                    pos[0] -= steps
                }

                Down -> {
                    repeat((0..<steps).count()) {
                        grid[pos[0] + it][pos[1]] = color[1]
                    }
                    pos[0] += steps
                }

                Left -> {
                    repeat((0..<steps).count()) {
                        grid[pos[0]][pos[1] - it] = color[1]
                    }
                    pos[1] -= steps
                }

                Right -> {
                    repeat((0..<steps).count()) {
                        grid[pos[0]][pos[1] + it] = color[1]
                    }
                    pos[1] += steps
                }

                else -> {}
            }
            sum += steps
        }

        val (startRow, startCol) = (pos[0] + 1..<grid.size).asSequence()
            .flatMap { r -> (pos[1] + 1..<grid[0].size).asSequence().map { c -> r to c } }
            .first { (r, c) -> grid[r][c] == '.' }

        println("startRow: $startRow, startCol: $startCol")

        val queue = mutableListOf(startRow to startCol)
        val visited = mutableSetOf(startRow to startCol)
        grid[startRow][startCol] = '#'
        var filled = 1
        while (queue.isNotEmpty()) {
            val (row, col) = queue.removeFirst()
            listOf(Up, Down, Left, Right)
                .map { dir ->
                    when (dir) {
                        Up -> row - 1 to col
                        Down -> row + 1 to col
                        Left -> row to col - 1
                        Right -> row to col + 1
                        else -> row to col
                    }
                }
                .filter { (r, c) -> r in grid.indices && c in grid[0].indices }
                .filter { (r, c) -> grid[r][c] == '.' }
                .filter { (r, c) -> visited.add(r to c) }
                .forEach { (r, c) ->
                    queue.add(r to c)
                    grid[r][c] = '#'
                    filled++
                }
        }
        return sum + filled
    }

    fun part1(input: List<String>): Int = solve(input)

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day18")
    with(Day18()) {
        part1(input).println()
        /*part2(input).println()*/
    }
}
