package mycorda.app.fileBundle.adapter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.adapters.FileBundleAdapter
import mycorda.app.fileBundle.adapters.TextAdapter
import java.io.File

class DirectoryAdapterTest : BaseAdapterTest<String>() {

    private val root = "src/test/resources/mycorda/app/fileBundle/adapter/TextAdapterTest"

    override val singleTextFile by lazy {
        loadAdapted("singleTextFile.txt")
    }

    override val singleBinaryFile by lazy {
        loadAdapted("singleBinaryFile.txt")
    }

    override val exampleFiles by lazy {
        loadAdapted("exampleFiles.txt")
    }


    override fun createAdapter(): FileBundleAdapter<String> = TextAdapter()


    override fun loadAdapted(path: String): String {
        return File("$root/$path").readText()
    }

    override fun storeAdapted(path: String, adapted: String) {
        File("$root/$path").writeText(adapted)
    }

    override fun assertAdapted(actual: String, expected: String) {
        assertThat(actual, equalTo(expected))
    }


}
