package mycorda.app.fileBundle.adapter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.Fixtures
import mycorda.app.fileBundle.adapters.TextAdapter
import mycorda.app.types.UniqueId
import org.junit.jupiter.api.Test
import java.io.File

class TextAdapterTest {

    private val root = "src/test/resources/mycorda/app/fileBundle/adapter/TextAdapterTest"

    private val singleTextFile by lazy {
        loadAsText("singleTextFile.txt")
    }

    private val singleBinaryFile by lazy {
        loadAsText("singleBinaryFile.txt")
    }

    private val exampleFiles by lazy {
        loadAsText("exampleFiles.txt")
    }

    @Test
    fun `should convert single text file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("123456")
        val result = adapter.toText(Fixtures.helloWorldBundle(id))
        // uncomment to save new test date
        //storeAsText("singleTextFile.txt", result)

        assertThat(result, equalTo(singleTextFile))
    }

    @Test
    fun `should un-convert single text file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("123456")
        val result = adapter.fromText(singleTextFile)
        assertThat(result, equalTo(Fixtures.helloWorldBundle(id)))
    }

    @Test
    fun `should convert single binary file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.toText(Fixtures.binaryBundle(id))
        // uncomment to save new test date
        // storeAsText("singleBinaryFile.txt", result)
        assertThat(result, equalTo(singleBinaryFile))
    }

    @Test
    fun `should un-convert single binary file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.fromText(singleBinaryFile)
        assertThat(result, equalTo(Fixtures.binaryBundle(id)))
    }

    @Test
    fun `should convert multi file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.toText(Fixtures.exampleFiles(id)).trim()
        // uncomment to save new test date
        //storeAsText("exampleFiles.txt", result)
        assertThat(result, equalTo(exampleFiles))
    }

    private fun loadAsText(path: String): String {
        return File("$root/$path").readText()
    }

    private fun storeAsText(path: String, text: String) {
        File("$root/$path").writeText(text)
    }
}
