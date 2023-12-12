import Direction.*
import Tile.*

enum class Direction(val coords: Pair<Int, Int>) {
    Up(-1 to 0),
    Down(1 to 0),
    Left(0 to -1),
    Right(0 to 1),
    None(0 to 0)
}

enum class Tile(val directions: Pair<Direction, Direction>) {
    Vertical(Up to Down),
    Horizontal(Left to Right),
    NorthToEast(Up to Right),
    NorthToWest(Up to Left),
    SouthToWest(Down to Left),
    SouthToEast(Down to Right),
    Start(None to None),
    Ground(None to None);

    companion object {
        fun tile(char: Char): Tile =
            when (char) {
                '|' -> Vertical
                '-' -> Horizontal
                'L' -> NorthToEast
                'J' -> NorthToWest
                '7' -> SouthToWest
                'F' -> SouthToEast
                '.' -> Ground
                'S' -> Start
                else -> error("")
            }
    }
}

class Day10 {

    fun determineStartTile(maze: Array<CharArray>, rowCol: Pair<Int, Int>): Tile {
        val (row, col) = rowCol
        val t = maze.getOrNull(row - 1)?.getOrNull(col)
        val l = maze.getOrNull(row)?.getOrNull(col - 1)
        val r = maze.getOrNull(row)?.getOrNull(col + 1)
        val b = maze.getOrNull(row + 1)?.getOrNull(col)

        val topConnected = t?.run(Tile::tile)?.run {
            this in setOf(Vertical, SouthToEast, SouthToWest)
        } ?: false

        val leftConnected = l?.run(Tile::tile)?.run {
            this in setOf(NorthToEast, Horizontal, SouthToEast)
        } ?: false


        val rightConnected = r?.run(Tile::tile)?.run {
            this in setOf(NorthToWest, Horizontal, SouthToWest)
        } ?: false

        val bottomConnected = b?.run(Tile::tile)?.run {
            this in setOf(NorthToEast, NorthToWest, Vertical)
        } ?: false

        return when {
            topConnected && leftConnected -> NorthToWest
            topConnected && rightConnected -> NorthToEast
            topConnected && bottomConnected -> Vertical
            bottomConnected && leftConnected -> SouthToWest
            bottomConnected && rightConnected -> SouthToEast
            leftConnected && rightConnected -> Horizontal
            else -> Ground
        }
    }

    fun parse(input: List<String>): Array<CharArray> =
        input.map { row -> row.map { it }.toCharArray() }.toTypedArray()

    fun part1(input: List<String>): Int {
        val array = parse(input)
        val start = findStart(array)
        val sp = start.first to start.second
        val notVisited = mutableListOf(sp to 0)
        val visitCount = mutableMapOf(sp to 0)

        tailrec fun dfs(current: Pair<Int, Int>, count: Int) {
            visitCount[current] = count
            val tile = if (current == sp) {
                determineStartTile(array, current)
            } else {
                Tile.tile(array[current.first][current.second])
            }

            tile.directions.toList().map { it.coords }.forEach { (dr, dc) ->
                val newRow = current.first + dr
                val newCol = current.second + dc
                if (newRow in array.indices && newCol in 0 until array[0].size) {
                    val newPair = newRow to newCol
                    if (newPair !in visitCount.keys) {
                        notVisited.add(newPair to count + 1)
                    }
                }
            }

            if (notVisited.isNotEmpty()) {
                val (nextCurrent, nextCount) = notVisited.removeFirst()
                dfs(nextCurrent, nextCount)
            }
        }

        dfs(sp, 0)
        return visitCount.values.max()
    }

    fun findStart(array: Array<CharArray>): Pair<Int, Int> =
        array.indexOfFirst { it.contains('S') }.let { row ->
            row to array[row].indexOf('S')
        }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day10")
    with(Day10()) {
        part1(input).println()
        /*part2(input).println()*/
    }
}
