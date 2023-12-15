class Day15 {

    fun String.holidayAlgorithm(): Int =
        fold(0) { acc, c ->
            (acc + c.code) * 17 % 256
        }

    fun part1(input: List<String>): Int =
        input.first().split(",")
            .sumOf { it.holidayAlgorithm() }

    fun part2(input: List<String>): Int {
        val boxes = List(256) { mutableSetOf<String>() }
        val focalLengths = mutableMapOf<String, Int>()
        input.first().split(",").forEach { s ->
            if (s.contains("-")) {
                val label = s.subSequence(0, s.length - 1).toString()
                val index = label.holidayAlgorithm()
                boxes[index].remove(label)
            } else {
                val (label, length) = s.split("=")
                    .let { (lb, len) -> lb to len.toInt() }
                boxes[label.holidayAlgorithm()].add(label)
                focalLengths[label] = length
            }
        }
        return boxes.foldIndexed(0) { box, boxTotal, slots ->
            boxTotal + slots.foldIndexed(0) { slot, slotTotal, labels ->
                slotTotal + (box + 1) * (slot + 1) * focalLengths[labels]!!
            }
        }
    }
}

fun main() {
    val input = readInput("Day15")
    with(Day15()) {
        part1(input).println()
        part2(input).println()
    }
}
