package benchmark

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val REPEAT_AMT = 10_000

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
class NullabilityBenchmark {

    @Benchmark
    fun nonNullable(blackhole: Blackhole) {
        repeat(REPEAT_AMT) {
            val sum = getNonNullable(Random.nextInt()) + 5
            blackhole.consume(sum)
        }
    }

    @Benchmark
    fun nullable(blackhole: Blackhole) {
        repeat(REPEAT_AMT) {
            val sum = (getNullable(Random.nextInt()) ?: 0) + 5
            blackhole.consume(sum)
        }
    }

    private fun getNonNullable(input: Int) = input

    private fun getNullable(input: Int): Int? = if (input % 2 == 0) input else null
}