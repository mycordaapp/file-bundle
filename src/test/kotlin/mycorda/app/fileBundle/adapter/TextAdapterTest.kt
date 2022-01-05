package mycorda.app.fileBundle.adapter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.adapters.TextAdapter
import mycorda.app.types.UniqueId
import org.junit.jupiter.api.Test

class TextAdapterTest {

    @Test
    fun `should convert single text file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("123456")
        val result = adapter.toText(Fixtures.helloWorldBundle(id))

        val expected = """
            .metadata.id=123456
            .metadata.name=HelloWorldBundle
            ---
            greeting.txt
            Hello, world
           
        """.trimIndent()
        assertThat(result, equalTo(expected))
    }

    @Test
    fun `should convert single binary file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.toText(Fixtures.binaryBundle(id))

        val expected = """
           .metadata.id=abcdef
           .metadata.name=BinaryBundle
           ---
           greeting.bin
           SGVsbG8sIHdvcmxk
           
        """.trimIndent()
        assertThat(result, equalTo(expected))
    }

    @Test
    fun `should convert multi file bundle`() {
        val adapter = TextAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.toText(Fixtures.exampleFiles(id))

        println(result)

        val expected = """
            .metadata.id=abcdef
            .metadata.name=ExampleFiles
            ---
            r3.svg
            PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48c3ZnIHhtbG5zPSJodHRwOi8v
            d3d3LnczLm9yZy8yMDAwL3N2ZyIgdmlld0JveD0iMCAwIDM1OS4yIDI3My43Ij48cGF0aCBkPSJN
            MzU5LjIgMTU4LjdjMCAxMi42LTEwLjMgMjIuOS0yMi45IDIyLjlzLTIyLjktMTAuMy0yMi45LTIy
            LjljMC0xMi42IDEwLjMtMjIuOSAyMi45LTIyLjkgMTIuNiAwIDIyLjkgMTAuMiAyMi45IDIyLjki
            IGZpbGw9IiNlZjM4MmUiLz48cGF0aCBkPSJNMjIyLjkgOTFMMjU0IDQ1LjhWMGgtOTlsLTMxLjUg
            NDUuOGg3NC43bC0zOC40IDU1LjkgMjIuOSAzOS43YzYuOC0zLjkgMTQuNy02LjIgMjMuMi02LjIg
            MjUuNiAwIDQ2LjQgMjAuNyA0Ni40IDQ2LjRTMjMxLjUgMjI4IDIwNS45IDIyOHMtNDYuNC0yMC44
            LTQ2LjQtNDYuNGgtNDUuOGMwIDUwLjkgNDEuMyA5Mi4yIDkyLjEgOTIuMiA1MC45IDAgOTIuMi00
            MS4zIDkyLjItOTIuMiAwLTQ1LjEtMzIuNC04Mi42LTc1LjEtOTAuNk05MiAwQzc1LjIgMCA1OS41
            IDQuNSA0NS45IDEyLjRWMEgwdjE4MS42aDQ1LjlWOTJjMC0yNS41IDIwLjYtNDYuMSA0Ni4xLTQ2
            LjFsMTUuNC0uMUwxMzguOSAwSDkyeiIvPjwvc3ZnPg==
        """.trimIndent()
        //assertThat(result.trim(), equalTo(expected.trim()))
    }
}