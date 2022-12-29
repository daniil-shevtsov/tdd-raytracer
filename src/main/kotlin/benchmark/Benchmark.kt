package benchmark

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

private const val REPEAT_AMT = 10_000
private const val MatrixSize = 1000

@State(Scope.Benchmark)
@Warmup(iterations = 1)
@Threads(20)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
class NullabilityBenchmark {

    private var array2D: Array<Array<Int>> = arrayOf(arrayOf())
    private var list2D: List<List<Int>> = emptyList()

    @Setup
    fun setUp() {
        array2D = Array(MatrixSize) { i -> Array(MatrixSize) { j -> i * MatrixSize + j } }
        list2D = List(MatrixSize) { i -> List(MatrixSize) { j -> i * MatrixSize + j } }
    }

    @Benchmark
    fun array(blackhole: Blackhole) {
        repeat(MatrixSize) { count ->
            val element = array2D[count / MatrixSize][count % MatrixSize]
            blackhole.consume(element)
        }
    }

    @Benchmark
    fun list(blackhole: Blackhole) {
        repeat(MatrixSize) {count ->
            val element = list2D[count / MatrixSize][count % MatrixSize]
            blackhole.consume(element)
        }
    }
}