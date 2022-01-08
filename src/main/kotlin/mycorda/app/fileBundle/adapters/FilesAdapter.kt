package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.BinaryBundleItem
import mycorda.app.fileBundle.FileBundle
import mycorda.app.fileBundle.TextBundleItem
import java.io.File

class FilesAdapter(private val rootDir: String) : FileBundleAdapter<List<File>> {
    init {
        File(rootDir).mkdirs()
    }

    override fun fromBundle(bundle: FileBundle): List<File> {
        val results = ArrayList<File>()
        results.add(writeMetaData(bundle))
        bundle.items.forEach {
            when (it) {
                is TextBundleItem -> {
                    val path = "${rootDir}/${it.path}"
                    //File(path).mkdirs()
                    File(path).apply {
                        this.writeText(it.content)
                        results.add(this)
                    }
                }
                is BinaryBundleItem -> {
                    val path = "${rootDir}/${it.path}"
                    //File(path).mkdirs()
                    File(path).apply {
                        this.writeBytes(it.content)
                        results.add(this)
                    }
                }
            }
        }

        return results
    }

    private fun writeMetaData(bundle: FileBundle): File {
        val sb = StringBuilder()
        sb.append(".metadata.id=${bundle.id}\n")
        sb.append(".metadata.name=${bundle.name}\n")
        bundle.items.forEachIndexed { index, item ->
            sb.append(".metadata.filename.${index + 1}=${item.path}\n")
            sb.append(".metadata.type.${index + 1}=${item::class.simpleName}\n")
        }
        File("$rootDir/.filebundle.meta").apply {
            this.writeText(sb.toString())
            return this
        }
    }

    override fun toBundle(adapted: List<File>): FileBundle {
        TODO("Not yet implemented")
    }
}