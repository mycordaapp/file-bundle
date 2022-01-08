package mycorda.app.fileBundle.adapter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.Fixtures
import mycorda.app.fileBundle.adapters.FileBundleAdapter
import mycorda.app.fileBundle.adapters.FilesAdapter
import mycorda.app.helpers.random
import org.junit.jupiter.api.Disabled
import java.io.File

@Disabled
class FilesAdapterTest : BaseAdapterTest<List<File>>() {
    private val root = "src/test/resources/mycorda/app/fileBundle/adapter/FilesAdapterTest"
    private val testDir = ".testing/${String.random()}"

    override val singleTextFile by lazy {
        loadAdapted("singleTextFile")
    }

    override val singleBinaryFile by lazy {
        loadAdapted("singleBinaryFile.txt")
    }

    override val exampleFiles by lazy {
        loadAdapted("exampleFiles.txt")
    }

    override fun createAdapter(): FileBundleAdapter<List<File>> = FilesAdapter(testDir)

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
        assertThat(a.map { it.name }, equalTo(e.map { it.name }))
        assertThat(a.map { it.length() }, equalTo(e.map { it.length() }))
        //assertThat(a.map { it.readBytes() }, equalTo(e.map { it.readBytes() }))
    }



}
