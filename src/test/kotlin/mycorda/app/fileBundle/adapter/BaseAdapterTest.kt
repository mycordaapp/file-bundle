package mycorda.app.fileBundle.adapter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.fileBundle.FileBundle
import mycorda.app.fileBundle.Fixtures
import mycorda.app.fileBundle.adapters.FileBundleAdapter
import mycorda.app.types.UniqueId
import org.junit.jupiter.api.Test

abstract class BaseAdapterTest<T> {
    protected abstract fun loadAdapted(path: String): T
    protected abstract fun storeAdapted(path: String, adapted: T)
    protected abstract fun createAdapter(): FileBundleAdapter<T>

    protected fun assertBundle(actual: FileBundle, expected: FileBundle) {
        assertThat(actual, equalTo(expected))
    }

    protected abstract fun assertAdapted(actual: T, expected: T)

    protected abstract val singleTextFile: T

    protected abstract val singleBinaryFile: T

    protected abstract val exampleFiles: T


    @Test
    fun `should convert single text file bundle`() {
        val adapter = createAdapter()
        val id = UniqueId.fromString("123456")
        val result = adapter.fromBundle(Fixtures.helloWorldBundle(id))
        // uncomment to save new test date
        //storeAdapted("singleTextFile.txt", result as T)
        assertAdapted(result, singleTextFile)
    }

    @Test
    fun `should un-convert single text file bundle`() {
        val adapter = createAdapter()
        val id = UniqueId.fromString("123456")
        val result = adapter.toBundle(singleTextFile)
        assertBundle(result, Fixtures.helloWorldBundle(id))
    }

    @Test
    fun `should convert single binary file bundle`() {
        val adapter = createAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.fromBundle(Fixtures.binaryBundle(id))
        // uncomment to save new test date
        // storeAdapted("singleBinaryFile.txt", result)
        assertAdapted(result, singleBinaryFile)
    }

    @Test
    fun `should un-convert single binary file bundle`() {
        val adapter = createAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.toBundle(singleBinaryFile)
        assertBundle(result, Fixtures.binaryBundle(id))
    }

    @Test
    fun `should convert multi file bundle`() {
        val adapter = createAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.fromBundle(Fixtures.exampleFiles(id))
        // uncomment to save new test date
        //storeAdapted("exampleFiles.txt", result)
        assertAdapted(result, exampleFiles)
    }

    @Test
    fun `should un-convert multi file bundle`() {
        val adapter = createAdapter()
        val id = UniqueId.fromString("abcdef")
        val result = adapter.toBundle(exampleFiles)
        assertBundle(result, Fixtures.exampleFiles(id))
    }

}