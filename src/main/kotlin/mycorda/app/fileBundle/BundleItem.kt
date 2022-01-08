package mycorda.app.fileBundle

import java.lang.RuntimeException
import java.util.*

sealed class BundleItem() {
    abstract val path: String
    fun pathMatches(regexp: Regex): Boolean = regexp.matches(path)

    companion object {
        fun build(path: String, content: String): BundleItem {
            return try {
                BinaryBundleItem(path, content)
            } catch (_: Exception) {
                TextBundleItem(path, content)
            }
        }

        val pattern = Regex("^[a-zA-Z0-9_\\-//.]+\$")
    }
}

data class TextBundleItem(override val path: String, val content: String) : BundleItem() {
    init {
        if (!pattern.matches(path)) throw RuntimeException("$path contains invalid characters")
    }

    companion object {
        fun fromResource(resourcePath: String, itemPath: String = resourcePath): TextBundleItem {
            val content = this::class.java.getResourceAsStream(resourcePath)!!.bufferedReader().readText()
            return TextBundleItem(itemPath, content)
        }
    }
}

data class BinaryBundleItem(override val path: String, val content: ByteArray) : BundleItem() {
    constructor(path: String, base64: String) :
            this(path, Base64.getMimeDecoder().decode(base64))

    init {
        if (!pattern.matches(path)) throw RuntimeException("$path contains invalid characters")
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is BinaryBundleItem) {
            if ((this.path == other.path) && (this.content.size == other.content.size)) {
                String(this.content) == String(other.content)
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


