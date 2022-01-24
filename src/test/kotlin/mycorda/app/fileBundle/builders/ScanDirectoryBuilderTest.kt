package mycorda.app.fileBundle.builders

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.Fixtures
import mycorda.app.fileBundle.adapters.FilesAdapter
import mycorda.app.helpers.random
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

    @Test
    fun `should scan terrsform directory`() {
        val id = UniqueId.randomUUID()
        val builder = ScanDirectoryBuilder()

        builder
            .withId(id)
            .withName("ExampleFiles")
            .withBaseDirectory("src/test/resources/terraform")

        val bundle = builder.build()

        val testDir = ".testing/${String.random()}"
        println("extracting bundle to $testDir")
        val filesAdapter = FilesAdapter(testDir)
        filesAdapter.fromBundle(bundle)

        println(bundle.items.size)
        bundle.items.forEach { println(it.path) }



        //val loca

        //val expected = Fixtures.allExampleFiles(id)
        //assertThat(bundle, equalTo(expected))
    }
}