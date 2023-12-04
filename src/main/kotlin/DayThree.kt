fun main() {
    val lines = readInput("Day03")

    data class Point(val x: Int, val y: Int)
    class Number(val value: Int)

    val grid = buildMap {
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                when {
                    contains(Point(x, y)) -> null as Any?
                    char.isDigit() -> {
                        val numberString = line.drop(x).takeWhile { it.isDigit() }
                        val number = Number(numberString.toInt())
                        (0..numberString.lastIndex).forEach { i -> put(Point(x + i, y), number) }
                    }

                    char != '.' -> put(Point(x, y), char)
                }
            }
        }
    }

    fun getAdjacent(p: Point) =
        (-1..1).flatMap { i -> (-1..1).mapNotNull { j -> grid.get(Point(p.x + i, p.y + j)) } }

    val schematicNumbers = grid.entries
        .mapNotNull { (k, v) -> if (v is Number && getAdjacent(k).any { it is Char }) v else null }
        .toSet()

    println(schematicNumbers.map { it.value }.sum())

    val schematicGearsNumbers = grid.entries
        .filter { it.value == '*' }
        .map { getAdjacent(it.key).filterIsInstance<Number>().toSet().map { it.value } }
        .filter { it.size == 2 }

    println(schematicGearsNumbers.map { it.reduce(Int::times) }.sum())
}
