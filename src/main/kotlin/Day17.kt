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

    private fun solve(input: List<String>, maxSteps: Int = 3, minSteps: Int = -1): Int {
        val grid = parse(input)
        val visited = mutableSetOf<Triple<Pair<Int, Int>, Pair<Int, Int>, Int>>()
        val queue = PriorityQueue(compareBy<NavState> { it.total })
        queue.offer(NavState(0, 0 to 0, 0, 0, 0))
        val start = 0 to 0
        val end = input.size - 1 to input[0].length - 1

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val (row, col) = current.currentCoords
            if (row to col == end && (minSteps < 0 || current.directionCount >= minSteps)) return current.total

            if (!visited.add(
                    Triple(
                        current.currentCoords,
                        (current.dRow to current.dCol),
                        current.directionCount
                    )
                )
            ) continue

            if (current.directionCount < maxSteps && row to col != start) {
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

            if (minSteps < 0 || (current.directionCount >= minSteps || row to col == start)) {
                listOf(Up, Down, Left, Right)
                    .forEach { dir ->
                        val (dr, dc) = dir.coords
                        if (dr to dc != current.dRow to current.dCol && dr to dc != (-current.dRow to -current.dCol)) {
                            add(queue, current.total, grid, row, col, dr, dc)
                        }
                    }
            }
        }
        error("No path found")
    }

    fun part1(input: List<String>): Int = solve(input)

    fun part2(input: List<String>): Int = solve(input, 10, 4)
}

fun main() {
    val input = readInput("Day17")
    with(Day17()) {
        part1(input).println()
        part2(input).println()
    }
}
