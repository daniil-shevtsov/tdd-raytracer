package tuple.practice

import tuple.Point
import tuple.Vector
import tuple.point
import tuple.vector

fun runTuplePractice() {
    generateTuplePracticeStates(
        initial = TuplePracticeState(
            environment = Environment(
                gravity = vector(0.0, -0.2, 0.0),
                wind = vector(0.0, 0.0, 0.0),
            ),
            projectile = Projectile(
                position = point(-10.0, 0.0, 0.0),
                velocity = vector(0.5, 1.0, 0.0).normalized
            )
        ),
        numberOfTicks = 15,
    ).let { states ->
        val positions = states.map { it.projectile.position }
        generateVisualizations(
            pointStates = states.map { state -> listOf(state.projectile.position) },
        )
    }.forEach { visualization ->
        Thread.sleep(300L)
        println(visualization)
    }
}

fun generateTuplePracticeStates(initial: TuplePracticeState, numberOfTicks: Int): List<TuplePracticeState> {
    return (0 until numberOfTicks)
        .fold(initial = listOf(initial)) { previousStates, _ ->
            val previousState = previousStates.last()

            previousStates + TuplePracticeState(
                projectile = tick(environment = previousState.environment, projectile = previousState.projectile),
                environment = previousState.environment,
            )
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

data class TuplePracticeState(
    val projectile: Projectile,
    val environment: Environment,
)