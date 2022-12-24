package tuple.practice

import tuple.Point
import tuple.Vector
import tuple.point
import tuple.vector

fun runTuplePractice() {
    val environment = Environment(
        gravity = vector(0.0, -0.1, 0.0),
        wind = vector(0.0, 0.0, 0.0),
    )
    var projectile = Projectile(
        position = point(0.0, 0.0, 0.0),
        velocity = vector(0.5, 1.0, 0.0).normalized
    )

    while (true) {
        Thread.sleep(500L)
        projectile = tick(environment = environment, projectile = projectile)
        println(projectile.position)
        val field = visualize(
            size = 11,
            points = listOf(projectile.position.copy(y = 10.0 - projectile.position.y))
        )
        println("\r---------------")
        println(field)
    }
}

fun tick(environment: Environment, projectile: Projectile): Projectile {
    val aboutToHitGround = projectile.position.y + projectile.velocity.y <= 0.0
    val newProjectile = projectile.copy(
        position = projectile.position + projectile.velocity,
        velocity = projectile.velocity + environment.gravity + environment.wind,
    )
    return when {
        aboutToHitGround -> newProjectile.copy(
            position = newProjectile.position.copy(
                y = 0.0
            ),
            velocity = vector(0.0, 0.0, 0.0)
        )
        else -> newProjectile
    }
}

data class Projectile(
    val position: Point,
    val velocity: Vector,
)

data class Environment(
    val gravity: Vector,
    val wind: Vector,
)