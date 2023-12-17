import Direction.*
import java.util.PriorityQueue

class Day17 {

    fun parse(input: List<String>): Array<IntArray> =
        input.map { it.map(Char::digitToInt) }.map(List<Int>::toIntArray).toTypedArray()

    fun add(
        queue: PriorityQueue<NavState>,
        heatLoss: Int,
        grid: Array<IntArray>,
        row: Int,
        col: Int,
        dr: Int,
        dc: Int,
        steps: Int = 1
    ) {
        val nr = row + dr
        val nc = col + dc
        if (nr in grid.indices && nc in grid[0].indices) {
            queue.offer(
                NavState(
                    heatLoss + grid[nr][nc],
                    nr to nc,
                    dr,
                    dc,
                    steps
                )
            )
        }

    }

    data class NavState(
        var total: Int,
        val currentCoords: Pair<Int, Int> = 0 to 0,
        val dRow: Int = 0,
        val dCol: Int = 0,
        var directionCount: Int,
    )

    fun part1(input: List<String>): Int {
        parse(input).forEach {
            it.forEach(::print)
            println("")
        }

        val grid = parse(input)
        val visited = mutableSetOf<NavState>()
        val queue = PriorityQueue(compareBy<NavState> { it.total })
        queue.offer(NavState(0, 0 to 0, 0, 0, 0))
        val end = input.size - 1 to input[0].length - 1

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val (row, col) = current.currentCoords
            if (row to col == end) return current.total

            if (!visited.add(
                    NavState(
                        current.total,
                        current.currentCoords,
                        current.dRow,
                        current.dCol,
                        current.directionCount
                    )
                )
            ) continue

            if (current.directionCount < 3 && row to col != 0 to 0) {
                val nr = row + current.dRow
                val nc = col + current.dCol
                if (nr in grid.indices && nc in grid[0].indices) {
                    add(
                        queue,
                        current.total,
                        grid,
                        row,
                        col,
                        current.dRow,
                        current.dCol,
                        current.directionCount + 1
                    )
                }
            }

            listOf(Up, Down, Left, Right)
                .forEach { dir ->
                    val (dr, dc) = dir.coords
                    if (dr to dc != current.dRow to current.dCol && dr to dc != (-current.dRow to -current.dCol)) {
                        add(queue, current.total, grid, row, col, dr, dc)
                    }
                }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day17")
    with(Day17()) {
        part1(input).println()
        /*part2(input).println()*/
    }
}
