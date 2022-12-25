package file

import java.io.File

fun createPmm(name: String, content: String) {
    File(name).printWriter().use { out ->
        out.println(content)
    }
}