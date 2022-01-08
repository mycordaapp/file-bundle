package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.*
import mycorda.app.types.UniqueId
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TextAdapter(private val options: Options = Options()) : FileBundleAdapter<String> {
    // force unix convention for consistent text assertions
    private val base64Encoder = Base64.getMimeEncoder(76, "\n".toByteArray())

    data class Options(
        val fileSeparatorLine: String = "---",
        val summaryMode: Boolean = false
    )

    enum class StateMachine { readMetaData, bundleType, bundleName, bundleContent }

    override fun fromBundle(bundle: FileBundle): String {
        val sb = StringBuilder()
        sb.append(".metadata.id=${bundle.id}\n")
        sb.append(".metadata.name=${bundle.name}\n")
        if (options.summaryMode) sb.append(".metadata.summaryMode=true\n")
        bundle.items.forEach {
            sb.append("${options.fileSeparatorLine}\n")
            when (it) {
                is TextBundleItem -> {
                    sb.append("TextBundleItem\n")
                    sb.append(it.path).append("\n")
                    sb.append(it.content).append("\n")
                }
                is BinaryBundleItem -> {
                    sb.append("BinaryBundleItem\n")
                    sb.append(it.path).append("\n")
                    sb.append(base64Encoder.encodeToString(it.content)).append("\n")
                }
            }
        }
        return sb.toString()
    }

    override fun toBundle(adapted: String): FileBundle {
        if (options.summaryMode) {
            throw RuntimeException("cannot read a file in summary mode")
        }

        // this is a mini state machine
        var state = StateMachine.readMetaData
        val metaData = HashMap<String, String>()
        var bundleType = ""
        var bundleName = ""
        var textContent = StringBuilder()
        val items = ArrayList<BundleItem>()
        adapted.lines().forEach {
            when (state) {
                StateMachine.readMetaData -> {
                    if (it == options.fileSeparatorLine) {
                        if (metaData.containsKey(".metadata.summaryMode")) {
                            throw RuntimeException("cannot read a file in summary mode")
                        }
                        state = StateMachine.bundleType
                    } else {
                        val meta = extractMetaDataLine(it)
                        metaData[meta.first] = meta.second
                    }
                }
                StateMachine.bundleType -> {
                    bundleType = it
                    state = StateMachine.bundleName
                }
                StateMachine.bundleName -> {
                    bundleName = it
                    state = StateMachine.bundleContent
                }
                StateMachine.bundleContent -> {
                    if (it == options.fileSeparatorLine) {
                        state = StateMachine.bundleType
                        items.add(buildBundleItem(bundleType, bundleName, textContent.toString()))
                        bundleName = ""
                        bundleType = ""
                        textContent.clear()
                    } else {
                        if (textContent.isNotEmpty()) textContent.append("\n")
                        textContent.append(it)
                    }
                }
            }
        }

        if (state == StateMachine.bundleContent) {
            items.add(buildBundleItem(bundleType, bundleName, textContent.toString()))
        }

        return FileBundleBuilder()
            .withId(UniqueId.fromString(metaData[".metadata.id"]!!))
            .withName(metaData[".metadata.name"]!!)
            .addItems(items)
            .build()
    }

    private fun extractMetaDataLine(line: String): Pair<String, String> {
        val parts = line.split("=")
        if (parts.size != 2) throw RuntimeException("Problem parsing mete-data in $line")
        return Pair(parts[0], parts[1])
    }

    private fun buildBundleItem(bundleType: String, bundleName: String, bundleContent: String): BundleItem {
        return when (bundleType) {
            "TextBundleItem" -> TextBundleItem(bundleName, bundleContent)
            "BinaryBundleItem" -> BinaryBundleItem(bundleName, bundleContent)
            else -> throw RuntimeException("internal error - unexpected `$bundleType`")
        }
    }
}