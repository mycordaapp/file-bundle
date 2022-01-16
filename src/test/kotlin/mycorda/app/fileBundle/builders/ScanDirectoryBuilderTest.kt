package mycorda.app.fileBundle.builders

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.Fixtures
import mycorda.app.types.UniqueId
import org.junit.jupiter.api.Test

class ScanDirectoryBuilderTest {

    @Test
    fun `should scan directory`() {
        val id = UniqueId.randomUUID()
        val builder = ScanDirectoryBuilder()

        builder
            .withId(id)
            .withName("ExampleFiles")
            .withBaseDirectory("src/test/resources/examples")

        val bundle = builder.build()
        val expected = Fixtures.allExampleFiles(id)
        assertThat(bundle, equalTo(expected))
    }
}