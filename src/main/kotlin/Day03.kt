class Day03 {
    private fun Char.isSymbol() = !isDigit() && this != '.'
    fun part1(input: List<String>): Int {
        val map = buildMap {
            input.forEachIndexed { rowIndex, line ->
                var colIndex = 0
                while (colIndex < line.length) {
                    val char = line[colIndex]
                    if (char.isDigit()) {
                        var endCol = colIndex
                        while (endCol + 1 < line.length && line[endCol + 1].isDigit()) {
                            endCol++
                        }
                        val number = line.substring(colIndex, endCol + 1).toInt()
                        put(rowIndex to colIndex, number)
                        colIndex = endCol + 1
                    } else {
                        colIndex++
                    }
                }
            }
        }

        val offsets = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)

        return map.entries.sumOf { (coord, num) ->
            val (r, c) = coord
            num.toString().indices.any {
                offsets.any { (rowOffset, colOffset) ->
                    input.getOrNull(r + rowOffset)
                        ?.getOrNull(c + it + colOffset)?.isSymbol() == true
                }
            }.let { isSymbolAdjacent ->
                if (isSymbolAdjacent) num else 0
            }
        }
    }
}

fun main() {
    val input = readInput("Day03")
//    val input = readInput("Day03_test")
    Day03().part1(input).println()
    /*part2(input).println()*/
}
