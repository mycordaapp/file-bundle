package mycorda.app.fileBundle

import mycorda.app.types.SimpleImmutableList
import mycorda.app.types.UniqueId

class BundleItemList(items: List<BundleItem>) : SimpleImmutableList<BundleItem>(items) {
}

class FileBundle(
    val id: UniqueId,
    val name: String,   // should there be limits on the character set in name?
    val items: BundleItemList
) {


}

class FileBundleBuilder {
    private var id: UniqueId = UniqueId.alphanumeric()
    private var name: String = ""
    private val items = ArrayList<BundleItem>()
    fun withId(id: UniqueId): FileBundleBuilder {
        this.id = id
        return this
    }

    fun withName(name: String): FileBundleBuilder {
        this.name = name
        return this
    }

    fun addItem(item: BundleItem): FileBundleBuilder {
        items.add(item)
        return this
    }

    fun build(): FileBundle {
        return FileBundle(id, name, BundleItemList(items))
    }


}