package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.BinaryBundleItem
import mycorda.app.fileBundle.FileBundle
import mycorda.app.fileBundle.TextBundleItem
import java.util.*


class TextAdapter(private val options: Options = Options()) {
    data class Options(val fileSeparatorLine: String = "---")

    fun toText(bundle: FileBundle): String {
        val sb = StringBuilder()
        sb.append(".metadata.id=${bundle.id}\n")
        sb.append(".metadata.name=${bundle.name}\n")
        bundle.items.forEach {
            sb.append("${options.fileSeparatorLine}\n")
            when (it) {
                is TextBundleItem -> {
                    sb.append(it.path).append("\n")
                    sb.append(it.content).append("\n")
                }
                is BinaryBundleItem -> {
                    sb.append(it.path).append("\n")
                    sb.append(Base64.getMimeEncoder().encodeToString(it.content)).append("\n")
                }
            }
        }
        return sb.toString()
    }

    fun fromText(text: String): FileBundle {
        TODO()
    }
}