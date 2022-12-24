package practice

import Point
import Vector
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test
import point
import vector

internal class TuplePracticeTest {

    @Test
    fun `should add velocity to position after a tick`() {
        val environment = environment()
        val initialProjectile = projectile(
            position = point(1.0, 2.0, 3.0),
            velocity = vector(2.0, 3.0, 4.0),
        )

        val resultingProjectile = tick(
            environment = environment,
            projectile = initialProjectile,
        )

        assertThat(resultingProjectile)
            .prop(Projectile::position)
            .isEqualTo(point(3.0, 5.0, 7.0))
    }

    @Test
    fun `should add gravity to velocity after a tick`() {
        val environment = environment(
            gravity = vector(6.0, 7.0, 8.0)
        )
        val initialProjectile = projectile(
            velocity = vector(2.0, 3.0, 4.0),
        )

        val resultingProjectile = tick(
            environment = environment,
            projectile = initialProjectile,
        )

        assertThat(resultingProjectile)
            .prop(Projectile::velocity)
            .isEqualTo(vector(8.0, 10.0, 12.0))
    }

    @Test
    fun `should add wind to velocity after a tick`() {
        val environment = environment(
            wind = vector(6.0, 7.0, 8.0)
        )
        val initialProjectile = projectile(
            velocity = vector(2.0, 3.0, 4.0),
        )

        val resultingProjectile = tick(
            environment = environment,
            projectile = initialProjectile,
        )

        assertThat(resultingProjectile)
            .prop(Projectile::velocity)
            .isEqualTo(vector(8.0, 10.0, 12.0))
    }

    @Test
    fun `should add gravity and wind to velocity after a tick`() {
        val environment = environment(
            gravity = vector(1.0, 2.0, 3.0),
            wind = vector(6.0, 7.0, 8.0),
        )
        val initialProjectile = projectile(
            velocity = vector(2.0, 3.0, 4.0),
        )

        val resultingProjectile = tick(
            environment = environment,
            projectile = initialProjectile,
        )

        assertThat(resultingProjectile)
            .prop(Projectile::velocity)
            .isEqualTo(vector(9.0, 12.0, 15.0))
    }

    data class Projectile(
        val position: Point,
        val velocity: Vector,
    )

    data class Environment(
        val gravity: Vector,
        val wind: Vector,
    )

    private fun projectile(
        position: Point = pointZero(),
        velocity: Vector = vectorZero(),
    ) = Projectile(
        position = position,
        velocity = velocity,
    )

    private fun environment(
        gravity: Vector = vectorZero(),
        wind: Vector = vectorZero(),
    ) = Environment(
        gravity = gravity,
        wind = wind,
    )

    fun tick(environment: Environment, projectile: Projectile): Projectile {
        return projectile.copy(
            position = projectile.position + projectile.velocity,
            velocity = projectile.velocity + environment.gravity + environment.wind,
        )
    }

    private fun pointZero() = point(0.0, 0.0, 0.0)
    private fun vectorZero() = vector(0.0, 0.0, 0.0)
}