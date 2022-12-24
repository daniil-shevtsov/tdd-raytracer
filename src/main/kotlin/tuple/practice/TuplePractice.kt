package tuple.practice

import tuple.Point
import tuple.Vector

fun runTuplePractice() {

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
            velocity = newProjectile.velocity.copy(
                y = 0.0
            )
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