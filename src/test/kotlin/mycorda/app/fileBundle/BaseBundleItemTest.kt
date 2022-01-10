package mycorda.app.fileBundle

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

abstract class BaseBundleItemTest {

    protected abstract fun buildBundleItem(path :String) : BundleItem

    @Test
    fun `should throw exception if path invalid`() {
        assertThrows<Exception> { buildBundleItem("@£@£$%") }
        assertThrows<Exception> { buildBundleItem("/cannot/start/with/a/slash") }
        assertThrows<Exception> { buildBundleItem("no spaces") }
        assertThrows<Exception> { buildBundleItem("no\\backslash") }
        assertThrows<Exception> { buildBundleItem("asciiTextØnly") }
        assertThrows<Exception> { buildBundleItem("a".repeat(257)) }
        assertThrows<Exception> { buildBundleItem("", ) }
    }

    @Test
    fun `should not throw exception if path valid`() {
        assertDoesNotThrow { buildBundleItem("myitem") }
        assertDoesNotThrow { buildBundleItem("nested/path/to/myitem") }
        assertDoesNotThrow { buildBundleItem("has_underscore") }
        assertDoesNotThrow { buildBundleItem("has-hypen") }
        assertDoesNotThrow { buildBundleItem("has-extension.txt") }
        assertDoesNotThrow { buildBundleItem("has-numbers-0123456789") }
        assertDoesNotThrow { buildBundleItem("UPPERCASE") }
        assertDoesNotThrow { buildBundleItem("a".repeat(256)) }
        assertDoesNotThrow { buildBundleItem("a") }
    }


}