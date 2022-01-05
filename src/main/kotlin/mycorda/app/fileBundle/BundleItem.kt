package mycorda.app.fileBundle

import java.io.BufferedReader

sealed class BundleItem(path: String) {
    private val path = path
    fun pathMatches(regexp: Regex): Boolean = regexp.matches(path)
}

data class TextBundleItem(val path: String, val content: String) : BundleItem(path) {
    companion object {
        fun fromResource(resourcePath: String, itemPath: String = resourcePath): TextBundleItem {
            val content = this::class.java.getResourceAsStream(resourcePath)!!.bufferedReader().readText()
            return TextBundleItem(itemPath, content)
        }
    }
}
data class BinaryBundleItem(val path: String, val content: ByteArray) : BundleItem(path) {
    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is BinaryBundleItem) {
            if (other.path == this.path && this.content.size == other.content.size) {
                this.content == other.content // todo - check binary content
                //true
            } else {
                false
            }
        } else {
            false
        }
    }

    companion object {
        fun fromResource(resourcePath: String, itemPath: String = resourcePath): BinaryBundleItem {
            val content = this::class.java.getResourceAsStream(resourcePath)!!.readAllBytes()
            return BinaryBundleItem(itemPath, content)
        }
    }

}


