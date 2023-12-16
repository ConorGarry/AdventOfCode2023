import Day16.Direction.*
import java.util.*

class Day16 {

    enum class Direction(val row: Int, val col: Int) {
        Up(-1, 0),
        Down(1, 0),
        Left(0, -1),
        Right(0, 1),
    }

    data class Tile(val row: Int, val col: Int, val prevDir: Direction)

    fun part1(input: List<String>): Int {
        val visited = mutableSetOf<Tile>()
        val grid = input.map { it.toCharArray() }.toTypedArray()

        val stack = ArrayDeque<Tile>()
        stack.push(Tile(0, 0, Right))

        while (stack.isNotEmpty()) {
            val current = stack.pop()
            val (row, col, prevDir) = current

            if (row < 0 || row >= grid.size || col < 0 || col >= grid[0].size || current in visited) {
                continue
            }

            visited.add(current)

            val nextDirections = when (grid[row][col]) {
                '|' -> listOf(Up, Down)
                '-' -> listOf(Left, Right)
                '\\' -> when (prevDir) {
                    Up -> listOf(Left)
                    Down -> listOf(Right)
                    Left -> listOf(Up)
                    Right -> listOf(Down)
                }

                '/' -> when (prevDir) {
                    Up -> listOf(Right)
                    Down -> listOf(Left)
                    Left -> listOf(Down)
                    Right -> listOf(Up)
                }

                '.' -> listOf(prevDir)
                else -> error("")
            }

            nextDirections.forEach {
                stack.push(Tile(row + it.row, col + it.col, it))
            }
        }

        return visited.distinctBy { it.row to it.col }.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day16")
    with(Day16()) {
        part1(input).println()
        /*part2(input).println()*/
    }
}
