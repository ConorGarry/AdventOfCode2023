fun main() {
    fun Char.isSymbol() = !isDigit() && this != '.'
    fun exploreNum(coords: IntArray, num: String, m: List<String>, validNums: MutableList<String>) {
        val start = coords[1] - num.length
        val length = m[0].length
        val height = m.size
        for (i in start..start + num.length) {
            val left = i - 1
            val right = i + 1
            val top = coords[0] - 1
            val bottom = coords[0] + 1
            // Left (if not at start)
            if (left > 0) { // First check we're not at very left of matrix.
                if (i == start && m[coords[0]][left].isSymbol()) {
                    validNums.add(num)
                    break
                }
            }
            // TL, T, TR
            if (top > 0) {
                if (left > 0 && m[top][left].isSymbol()) {
                    validNums.add(num)
                    break
                }
                if (m[top][left + 1].isSymbol()) {
                    validNums.add(num)
                    break;
                }
                if (right < length && m[top][right].isSymbol()) {
                    validNums.add(num)
                    break;
                }
            }
            // BL, B, BR
            if (bottom < height) {
                if (left > 0 && m[bottom][left].isSymbol()) {
                    validNums.add(num)
                    break
                }
                if (m[bottom][left + 1].isSymbol()) {
                    validNums.add(num)
                    break;
                }
                if (right < length && m[bottom][right].isSymbol()) {
                    validNums.add(num)
                    break;
                }
            }
            // Right, if not at edge.
            if (i == (start + num.length - 1) && right < length && m[coords[0]][right].isSymbol()) {
                validNums.add(num)
                break
            }
        }

        println("valudNums: $validNums")
        validNums.map { it.toInt() }.sum().let {
            println("total: $it")
        }
    }

    fun part1(input: List<String>)/*: Int*/ {
        var validNums = mutableListOf<String>()
        for (i in input.indices) {
            val nums = mutableListOf<Int>()
            for (j in 0..<input[i].length) {
                val x = input[i][j]
                if (x.isDigit()) {
                    nums += x.digitToInt()
                } else {
                    if (nums.isNotEmpty()) {
                        exploreNum(intArrayOf(i, j), nums.joinToString(""), input, validNums)
                        nums.clear()
                    }
                }
            }
            println()
        }
    }


    val input = readInput("Day03"/*"Day03_test"*/)
    part1(input)/*.println()*/
    /*part2(input).println()*/
    // NOT 543658, 543561
}