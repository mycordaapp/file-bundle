package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.*
import mycorda.app.types.UniqueId
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TextAdapter(private val options: Options = Options()) {
    data class Options(
        val fileSeparatorLine: String = "---",
        val summaryMode: Boolean = false
    )

    enum class StateMachine { readMetaData, startBundleItem, bundleContent }

    fun toText(bundle: FileBundle): String {
        val sb = StringBuilder()
        sb.append(".metadata.id=${bundle.id}\n")
        sb.append(".metadata.name=${bundle.name}\n")
        if (options.summaryMode) sb.append(".metadata.summaryMode=true\n")
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
        if (options.summaryMode) {
            throw RuntimeException("cannot read a file in summary mode")
        }

        // this is a mini state machine
        var state = StateMachine.readMetaData
        val metaData = HashMap<String, String>()
        var bundleName = ""
        var textContent = StringBuilder()
        val items = ArrayList<BundleItem>()
        text.lines().forEach {
            when (state) {
                StateMachine.readMetaData -> {
                    if (it == options.fileSeparatorLine) {
                        if (metaData.containsKey(".metadata.summaryMode")) {
                            throw RuntimeException("cannot read a file in summary mode")
                        }
                        state = StateMachine.startBundleItem
                    } else {
                        val meta = extractMetaDataLine(it)
                        metaData[meta.first] = meta.second
                    }
                }
                StateMachine.startBundleItem -> {
                    bundleName = it
                    state = StateMachine.bundleContent
                }
                StateMachine.bundleContent -> {
                    if (it == options.fileSeparatorLine) {
                        state = StateMachine.startBundleItem
                        items.add(BundleItem.build(bundleName, textContent.toString()))
                        bundleName = ""
                        textContent.clear()
                    } else {
                        if (textContent.isNotEmpty()) textContent.append("\n")
                        textContent.append(it)
                    }
                }
            }
        }

        if (state == StateMachine.bundleContent) {
            items.add(BundleItem.build(bundleName, textContent.toString()))
        }

        return FileBundleBuilder()
            .withId(UniqueId.fromString(metaData[".metadata.id"]!!))
            .withName(metaData[".metadata.name"]!!)
            .addItem(items.first())
            .build()
    }

    private fun extractMetaDataLine(line: String): Pair<String, String> {
        val parts = line.split("=")
        if (parts.size != 2) throw RuntimeException("Problem parsing mete-data in $line")
        return Pair(parts[0], parts[1])
    }
}