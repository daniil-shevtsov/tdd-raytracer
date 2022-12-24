import org.junit.jupiter.api.Test
import kotlin.test.DefaultAsserter.assertEquals

class HelloTest {

    @Test
    fun `lol kek`() {
        assertEquals(null, expected = "Hello", actual = createHello())
    }

}