package mycorda.app.fileBundle

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test


class TextBundleItemTest : BaseBundleItemTest() {
    override fun buildBundleItem(path: String, isExecutable: Boolean): BundleItem {
        return TextBundleItem(path, "foo", isExecutable)
    }

    @Test
    fun `should replace windows style line termination`() {
        val rhyme = "\tMary\nhad\r\nlittle\nlamb.\r\n"
        val item = TextBundleItem("rhyme.txt", rhyme)
        assertThat(item.content, equalTo("\tMary\nhad\nlittle\nlamb.\n"))
    }
}