package mycorda.app.fileBundle

import mycorda.app.types.UniqueId
import org.junit.jupiter.api.Test

class FileBundleTest {

    @Test
    fun `should create a single file bundle`() {
        val builder = FileBundleBuilder()
        val id = UniqueId.alphanumeric()

        val bundle = builder.withId(id)
            .withName("demo bundle")
            .addItem(TextBundleItem("settings.properties", "foo=bar"))
            .build()


    }
}