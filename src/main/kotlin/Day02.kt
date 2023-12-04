import java.lang.Integer.max

const val RED = 12
const val GREEN = 13
const val BLUE = 14

data class GameSet(var red: Int = 0, var green: Int = 0, var blue: Int = 0)

val games = readInput("Day02"/*"Day02_test"*/)
    .map { input ->
        input.split(":").let { (game, sets) ->
            game.substring(game.indexOf(" ")).trim().toInt() to
                    sets.split("; ").map {
                        it.split(", ").associate { c ->
                            c.split(" ").asReversed()
                                .let { (color, count) -> color to count.toInt() }
                        }
                    }.map {
                        GameSet(it["red"] ?: 0, it["green"] ?: 0, it["blue"] ?: 0)
                    }
        }
    }

fun main() {
    fun solutions(): IntArray =
        games.fold(intArrayOf(0, 0)) { totals, (id, sets) ->
            var valid = true
            val minSet = GameSet()
            sets.forEach { set ->
                if (set.red > RED || set.green > GREEN || set.blue > BLUE) valid = false
                minSet.red = max(minSet.red, set.red)
                minSet.green = max(minSet.green, set.green)
                minSet.blue = max(minSet.blue, set.blue)
            }
            intArrayOf(
                totals[0] + if (valid) id else 0,
                totals[1] + minSet.let { (r, g, b) -> r * g * b }
            )
        }

    val (part1, part2) = solutions()
    part1.println()
    part2.println()
}
