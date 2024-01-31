package io.undo

import java.util.concurrent.LinkedBlockingQueue
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter
import kotlin.concurrent.thread
import kotlin.random.Random

val fileHandler = FileHandler("fruit.log")
val random = Random(23)

fun getLogger(name: String): Logger {
    val logger = Logger.getLogger(name)
    logger.addHandler(fileHandler)
    return logger
}

fun main() {
    println("Hello!")
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tT.%1\$tL] %4\$-7s %3\$s - %5\$s %n")
    fileHandler.formatter = SimpleFormatter()

    val input = LinkedBlockingQueue<Vegetable>()
    val output = LinkedBlockingQueue<Boolean>()

    thread(start = true, name = "Producer") {
        val logger = getLogger("Producer")
        for (i in 0..1000) {
            val r = random.nextInt(0, 1000)
            var veg: Vegetable
            if (r == 37) {
                veg = Vegetable()
            } else if ((r % 17) == 0) {
                veg = Turnip()
            } else if ((r % 2) == 0) {
                veg = Apple()
            } else {
                veg = Pear()
            }
            input.put(veg)
            Thread.sleep(2)
            if (output.take()) {
                logger.info("vegetable was a turnip")
            }
        }
    }

    thread(start = true, isDaemon = true, name = "Consumer") {
        val logger = getLogger("Consumer")
        var fruit = 0
        var turnips = 0
        var unknown = 0
        while (true) {
            val veg = input.take()
            var isTurnip = false
            if (veg is Turnip) {
                turnips++
                isTurnip = true
                logger.info("found $turnips turnips")
            } else if (veg is Fruit) {
                fruit++
            } else {
                unknown++
                logger.info("found $unknown unknown vegetables")
            }
            output.put(isTurnip)
        }
    }
}

open class Vegetable

open class Fruit : Vegetable()

class Apple : Fruit() {
    companion object {
        val logger = getLogger("Apple")
    }
}

class Pear : Fruit() {
    companion object {
        val logger = getLogger("Pear")
    }
}

class Turnip : Vegetable() {
    companion object {
        val logger = getLogger("Turnip")
    }
}
