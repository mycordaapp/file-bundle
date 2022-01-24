package mycorda.app.fileBundle.adapters

import mycorda.app.fileBundle.*
import mycorda.app.fileBundle.builders.FileBundleBuilder
import mycorda.app.types.UniqueId
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFileAttributes
import java.nio.file.attribute.PosixFilePermission


class FilesAdapter(private val rootDir: String) : FileBundleAdapter<List<File>> {
    init {
        File(rootDir).mkdirs()
    }

    override fun fromBundle(bundle: FileBundle): List<File> {
        val results = ArrayList<File>()
        results.add(writeMetaData(bundle))
        bundle.items.forEach {
            makeNestedDirsIfNecessary(it)
            when (it) {
                is TextBundleItem -> {
                    val path = "${rootDir}/${it.path}"
                    File(path).apply {
                        this.writeText(it.content)
                        results.add(this)
                    }
                    if (it.isExecutable){
                        makeExecutable(path)
                    }
                }
                is BinaryBundleItem -> {
                    val path = "${rootDir}/${it.path}"
                    File(path).apply {
                        this.writeBytes(it.content)
                        results.add(this)
                    }
                    if (it.isExecutable){
                        makeExecutable(path)
                    }
                }
            }
        }

        return results
    }

    private fun makeExecutable(path: String) {
        val path = Paths.get(path)
        val perms: MutableSet<PosixFilePermission> =
            Files.readAttributes(path, PosixFileAttributes::class.java).permissions()
        perms.add(PosixFilePermission.OWNER_EXECUTE)
        perms.add(PosixFilePermission.GROUP_EXECUTE)
        perms.add(PosixFilePermission.OTHERS_EXECUTE)
        Files.setPosixFilePermissions(path, perms)
    }

    private fun makeNestedDirsIfNecessary(it: BundleItem) {
        if (it.path.contains("/")) {
            val nested = it.path
                .replaceAfterLast("/", "", "")
                .removeSuffix("/")
            File("${rootDir}/$nested").mkdirs()
        }
    }

    private fun writeMetaData(bundle: FileBundle): File {
        val sb = StringBuilder()
        sb.append(".metadata.id=${bundle.id}\n")
        sb.append(".metadata.name=${bundle.name}\n")
        bundle.items.forEachIndexed { index, item ->
            sb.append(".metadata.filename.${index + 1}=${item.path}\n")
            sb.append(".metadata.type.${index + 1}=${item::class.simpleName}\n")
            sb.append(".metadata.isExecutable.${index + 1}=${item.isExecutable}\n")
        }
        File("$rootDir/.filebundle.meta").apply {
            this.writeText(sb.toString())
            return this
        }
    }

    override fun toBundle(adapted: List<File>): FileBundle {
        val rawMeta = extractRawMetaData(adapted)
        val bundleItems = ArrayList<BundleItem>()

        val fileCount = rawMeta
            .filterKeys { it.startsWith(".metadata.filename") }
            .count()

        (1..fileCount).forEach {
            val path = rawMeta[".metadata.filename.${it}"]!!
            val type = rawMeta[".metadata.type.${it}"]!!
            val isExecutable = rawMeta[".metadata.isExecutable.${it}"]!!.toBoolean()

            val file = findFileByPath(path, adapted)
            when (type) {
                "TextBundleItem" -> {
                    bundleItems.add(TextBundleItem(path, file.readText(), isExecutable))
                }
                "BinaryBundleItem" -> {
                    bundleItems.add(BinaryBundleItem(path, file.readBytes(), isExecutable))
                }
            }
        }

        return FileBundleBuilder()
            .withId(UniqueId.fromString(rawMeta[".metadata.id"]!!))
            .withName(rawMeta[".metadata.name"]!!)
            .addItems(bundleItems)
            .build()
    }

    private fun findFileByPath(path: String, adapted: List<File>): File {
        return adapted.single { it.canonicalPath.endsWith("/" + path) }
    }

    private fun extractRawMetaData(adapted: List<File>): Map<String, String> {
        val metaData = HashMap<String, String>()
        findFileByPath(".filebundle.meta", adapted)
            .readText()
            .lines()
            .filter { it.isNotEmpty() }
            .forEach {
                val meta = extractMetaDataLine(it)
                metaData[meta.first] = meta.second
            }
        return metaData
    }

    private fun extractMetaDataLine(line: String): Pair<String, String> {
        val parts = line.split("=")
        if (parts.size != 2) throw RuntimeException("Problem parsing mete-data in $line")
        return Pair(parts[0], parts[1])
    }
}