import Day19.FlowItem.*

class Day19 {

    sealed class FlowItem {
        data class Rule(val rule: (Rating) -> Boolean, val result: Literal) : FlowItem()

        data class Literal(val value: String) : FlowItem()
    }

    data class WorkFlow(
        val name: String,
        val items: List<FlowItem>,
        var i: Int = 0,
    )

    data class Rating(
        val x: Int,
        val m: Int,
        val a: Int,
        val s: Int,
    ) {
        val sum = x + m + a + s
    }

    fun part1(input: List<String>): Int {
        val (wf, rt) = input.split(String::isEmpty)
        val workflows = wf.map {
            WorkFlow(
                it.substringBefore("{"),
                it.substringAfter("{").substringBefore("}").split(",").map { item ->
                    item.takeIf { it.contains(">") || it.contains("<") }?.let { rule ->
                        val (num, literal) = rule.split(":")
                        val value = num.substring(2).toInt()
                        when (rule.substring(0, 2)) {
                            "x<" -> Rule({ it.x < value }, Literal(literal))
                            "x>" -> Rule({ it.x > value }, Literal(literal))
                            "m<" -> Rule({ it.m < value }, Literal(literal))
                            "m>" -> Rule({ it.m > value }, Literal(literal))
                            "a<" -> Rule({ it.a < value }, Literal(literal))
                            "a>" -> Rule({ it.a > value }, Literal(literal))
                            "s<" -> Rule({ it.s < value }, Literal(literal))
                            "s>" -> Rule({ it.s > value }, Literal(literal))
                            else -> error("")
                        }
                    } ?: Literal(item)
                }
            )
        }

        val ratings = rt.map {
            it.removeSurrounding("{", "}").split(",").associate {
                val (key, value) = it.split("=")
                key to value.toInt()
            }.let { map ->
                Rating(map["x"]!!, map["m"]!!, map["a"]!!, map["s"]!!)
            }
        }

        val result = mutableMapOf<String, String>()
        var sum = 0
        fun handleItem(result: MutableMap<String, String>, workFlow: WorkFlow, rating: Rating) {
            fun handleLiteral(literal: String) =
                when (literal) {
                    "A" -> {
                        result[workFlow.name] = "Accepted"
                        sum += rating.sum
                    }

                    "R" -> result[workFlow.name] = "Rejected"
                    else -> handleItem(
                        result,
                        workflows.first { it.name == literal },
                        rating
                    )
                }.also { workFlow.i = 0 } // Reset item iterator.

            when (val item = workFlow.items[workFlow.i++]) {
                is Rule -> {
                    if (item.rule(rating)) {
                        handleLiteral(item.result.value.also { println(item) })
                    } else {
                        handleItem(result, workFlow, rating)
                    }
                }

                is Literal -> handleLiteral(item.value)
            }
        }

        ratings.forEach {
            handleItem(result, workflows.first { wf -> wf.name == "in" }, it)
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main() {
    val input = readInput("Day19")
    with(Day19()) {
        part1(input).println()
        /*part2(input).println()*/
    }
}
