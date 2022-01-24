package mycorda.app.fileBundle

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

sealed class BundleItem() {
    abstract val path: String
    abstract val isExecutable: Boolean
    fun pathMatches(regexp: Regex): Boolean = regexp.matches(path)

    companion object {
        val validPathPattern = Regex("^[a-zA-Z0-9_\\-//.]+\$")
    }
}

class TextBundleItem(
    override val path: String,
    rawContent: String,
    override val isExecutable: Boolean = false
) :
    BundleItem() {
    init {
        if (path.length > 256) throw RuntimeException("path must be no more than 256 character long")
        if (!validPathPattern.matches(path)) throw RuntimeException("$path contains invalid characters")
        if (path.startsWith("/")) throw RuntimeException("$path cannot start with a slash ('/') character")
    }

    val content: String = rawContent.replace("\r\n", "\n")

    override fun hashCode(): Int {
        return path.hashCode() xor content.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is TextBundleItem) {
            (this.path == other.path) && (this.content == other.content) && (this.isExecutable == other.isExecutable)
        } else {
            false
        }
    }

    companion object {
        fun fromResource(
            resourcePath: String,
            path: String = resourcePath,
            isExecutable: Boolean = false
        ): TextBundleItem {
            val content = this::class.java.getResourceAsStream(resourcePath)!!.bufferedReader().readText()
            return TextBundleItem(path, content, isExecutable)
        }

        fun fromFile(file: File, path: String, isExecutable: Boolean = false): TextBundleItem {
            val content = file.readText()
            return TextBundleItem(path, content, isExecutable)
        }
    }
}

class BinaryBundleItem(
    override val path: String,
    val content: ByteArray,
    override val isExecutable: Boolean = false
) : BundleItem() {
    constructor(path: String, base64: String, isExecutable: Boolean = false) :
            this(path, Base64.getMimeDecoder().decode(base64), isExecutable)

    init {
        if (path.length > 256) throw RuntimeException("path must be no more than 256 character long")
        if (!validPathPattern.matches(path)) throw RuntimeException("$path contains invalid characters")
        if (path.startsWith("/")) throw RuntimeException("$path cannot start with a slash ('/') character")
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is BinaryBundleItem) {
            if ((this.path == other.path) && (this.content.size == other.content.size)) {
                (this.isExecutable == other.isExecutable) && (String(this.content) == String(other.content))
            } else {
                false
            }
        } else {
            false
        }
    }

    companion object {
        fun fromResource(
            resourcePath: String,
            path: String = resourcePath,
            isExecutable: Boolean = false
        ): BinaryBundleItem {
            val content: ByteArray =
                Files.readAllBytes(Paths.get(this::class.java.getResource(resourcePath).toURI()))

            //val content = this::class.java.getResourceAsStream(resourcePath)!!.readAllBytes()
            return BinaryBundleItem(path, content, isExecutable)
        }

        fun fromFile(file: File, path: String, isExecutable: Boolean = false): BinaryBundleItem {
            val content = file.readBytes()
            return BinaryBundleItem(path, content, isExecutable)
        }
    }
}


