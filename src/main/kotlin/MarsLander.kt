import java.util.*
import kotlin.math.*

fun main() {

    val program = MarsLander()
    program.run()
}

class MarsLander {

    private var outputAngle = 0
    private var outputThrust = 4
    private var rocket = Rocket(0,0,0,0,0,0,0)
    private var landingZone = LandingZone(0,0,0)
    private var isOverLandingZone = false
    private var direction = 0
    private var step = 0
    private var initialSpeed = 0

    fun run() {
        val input = Scanner(System.`in`)

        val landingPointsNumber = input.nextInt() // the number of points used to draw the surface of Mars.
        var land= Pair(0,0)
        var prevLand: Pair<Int, Int>
        for (i in 0 until landingPointsNumber) {
            prevLand = land
            land = Pair(input.nextInt(), input.nextInt())
            if(prevLand.second == land.second) landingZone = LandingZone(prevLand.first, prevLand.first + abs(land.first - prevLand.first)/2, land.first)
        }

        // game loop
        while (true) {
            rocket = Rocket(input.nextInt(), input.nextInt(),input.nextInt(),input.nextInt(),input.nextInt(),input.nextInt(),input.nextInt())
            isOverLandingZone = (rocket.x >= landingZone.left && rocket.x <= landingZone.right)
            direction = sign((landingZone.middle - rocket.x).toDouble()).toInt()

            if(step == 0) initialSpeed  = if(direction == sign(rocket.hs.toDouble()).toInt()) abs(rocket.hs) else 100

            //Landing zone reached start to slow down
            if(isOverLandingZone){
                outputAngle = getAngle(0, 33)
                outputThrust = if(abs(rocket.vs) < 40 && rocket.hs == 0)  3 else 4 //Reduce thrust if vertical speed is too high to land
            }else{
                outputThrust = if(rocket.vs >= 0)  3 else 4 //Reduce thrust if going up
                outputAngle = if(abs(rocket.x - landingZone.middle) > 2000) getAngle(initialSpeed, 60) else getAngle(20, 45) //reduce speed and angle when getting close
            }
            println("$outputAngle $outputThrust")
            step++
        }

    }
    private fun getAngle(speed:Int, angle:Int): Int {
        val acceleration = (direction * speed - rocket.hs).coerceIn(-outputThrust, outputThrust)
        val targetAngle = Math.toDegrees(asin(acceleration.toDouble() / outputThrust)).toInt().coerceIn(-angle, angle)
        return -targetAngle
    }
    data class Rocket(val x:Int, val y:Int, val hs: Int, val vs: Int, val fuel: Int, val angle: Int, val thrust: Int)
    data class LandingZone(val left:Int, val middle:Int, val right: Int)
}
