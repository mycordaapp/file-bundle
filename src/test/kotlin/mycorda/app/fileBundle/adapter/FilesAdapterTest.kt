package mycorda.app.fileBundle.adapter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.throws
import mycorda.app.fileBundle.Fixtures
import mycorda.app.fileBundle.adapters.FileBundleAdapter
import mycorda.app.fileBundle.adapters.FilesAdapter
import mycorda.app.fileBundle.adapters.TextAdapter
import mycorda.app.helpers.random
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.RuntimeException

class FilesAdapterTest() : BaseAdapterTest<List<File>>() {
    private val root = "src/test/resources/mycorda/app/fileBundle/adapter/FilesAdapterTest"

    override val singleTextFile by lazy {
        loadAdapted("singleTextFile")
    }

    override val mixOfLineTermination by lazy {
        loadAdapted("mixOfLineTermination")
    }

    override val singleBinaryFile by lazy {
        loadAdapted("singleBinaryFile")
    }

    override val exampleFiles by lazy {
        loadAdapted("exampleFiles")
    }

    override fun createAdapter(dataMode: DataMode): FileBundleAdapter<List<File>> {
        val testDir = ".testing/${String.random()}"
        return FilesAdapter(testDir)
    }

    override fun loadAdapted(path: String): List<File> {
        val results = ArrayList<File>()
        File("$root/$path").walk().forEach {
            if (it.isFile) results.add(it)
        }
        return results
    }

    override fun storeAdapted(path: String, adapted: List<File>) {
        //File("$root/$path").writeText(adapted)
    }

    override fun assertAdapted(actual: List<File>, expected: List<File>) {


        val a = actual.sortedBy { it.name }
        val e = expected.sortedBy { it.name }

        val x = a.last().length()
        val y = e.last().length()
        assertThat(a.map { it.name }, equalTo(e.map { it.name }))
        assertThat(a.map { it.length() }, equalTo(e.map { it.length() }))

        //assertThat(a.map { it.readBytes() }, equalTo(e.map { it.readBytes() }))
    }

    @Test
    fun `should throw exception if retrieving from text in summary mode`() {
        val summaryModeAdapter = TextAdapter(TextAdapter.Options(summaryMode = true))
        val normalAdapter = TextAdapter()

        val stored = summaryModeAdapter.fromBundle(Fixtures.helloWorldBundle())

        // cannot load as the data was stored in summary mode
        assertThat(
            { normalAdapter.toBundle(stored) },
            throws<RuntimeException>(
                has(
                    Exception::message,
                    present(equalTo("cannot read text stored in summary mode"))
                )
            )
        )

        // cannot load as the adapter is in summary  mode
        assertThat(
            { summaryModeAdapter.toBundle("dont care") },
            throws<RuntimeException>(has(Exception::message, present(equalTo("cannot read text when in summary mode"))))
        )
    }

}
