package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.*
import mycorda.app.fileBundle.builders.FileBundleBuilder
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

    enum class StateMachine { readMetaData, itemType, itemName, itemExecutable, itemContent }

    override fun fromBundle(bundle: FileBundle): String {
        val sb = StringBuilder()
        sb.append(".metadata.id=${bundle.id}\n")
        sb.append(".metadata.name=${bundle.name}\n")
        if (options.summaryMode) sb.append(".metadata.summaryMode=true\n")
        bundle.items.forEachIndexed { index, item ->
            sb.append("${options.fileSeparatorLine}\n")
            when (item) {
                is TextBundleItem -> {
                    sb.append("TextBundleItem\n")
                    sb.append(item.path).append("\n")
                    if (item.isExecutable) sb.append("executable\n") else sb.append("notExecutable\n")
                    if (options.summaryMode) {
                        sb.append("length: ${item.content.length}\n")
                    } else {
                        sb.append(item.content)
                        if (index != bundle.items.size - 1) sb.append("\n")
                    }
                }
                is BinaryBundleItem -> {
                    sb.append("BinaryBundleItem\n")
                    sb.append(item.path).append("\n")
                    if (item.isExecutable) sb.append("executable\n") else sb.append("notExecutable\n")
                    if (options.summaryMode) {
                        sb.append("bytes: ${item.content.size}\n")
                    } else {
                        sb.append(base64Encoder.encodeToString(item.content))
                        if (index != bundle.items.size - 1) sb.append("\n")
                    }
                }
            }
        }
        return sb.toString()
    }

    override fun toBundle(adapted: String): FileBundle {
        if (options.summaryMode) {
            throw RuntimeException("cannot read text when in summary mode")
        }

        // this is a mini state machine
        var state = StateMachine.readMetaData
        val metaData = HashMap<String, String>()
        var bundleType = ""
        var bundleName = ""
        var isExecutable = false
        var textContent = StringBuilder()
        val items = ArrayList<BundleItem>()
        adapted.lines().forEach {
            when (state) {
                StateMachine.readMetaData -> {
                    if (it == options.fileSeparatorLine) {
                        if (metaData.containsKey(".metadata.summaryMode")) {
                            throw RuntimeException("cannot read text stored in summary mode")
                        }
                        state = StateMachine.itemType
                    } else {
                        val meta = extractMetaDataLine(it)
                        metaData[meta.first] = meta.second
                    }
                }
                StateMachine.itemType -> {
                    bundleType = it
                    state = StateMachine.itemName
                }
                StateMachine.itemName -> {
                    bundleName = it
                    state = StateMachine.itemExecutable
                }
                StateMachine.itemExecutable -> {
                    if (!setOf("executable", "notExecutable").contains(it)) throw RuntimeException("$it unexpected")
                    isExecutable = "executable" == it
                    state = StateMachine.itemContent
                }
                StateMachine.itemContent -> {
                    if (it == options.fileSeparatorLine) {
                        state = StateMachine.itemType
                        items.add(buildBundleItem(bundleType, bundleName, textContent.toString(), isExecutable))
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

        if (state == StateMachine.itemContent) {
            items.add(buildBundleItem(bundleType, bundleName, textContent.toString(),isExecutable))
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

    private fun buildBundleItem(
        bundleType: String,
        bundleName: String,
        bundleContent: String,
        isExecutable: Boolean
    ): BundleItem {
        return when (bundleType) {
            "TextBundleItem" -> TextBundleItem(bundleName, bundleContent, isExecutable)
            "BinaryBundleItem" -> BinaryBundleItem(bundleName, bundleContent, isExecutable)
            else -> throw RuntimeException("internal error - unexpected `$bundleType`")
        }
    }
}