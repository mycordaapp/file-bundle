package mycorda.app.fileBundle.builders

import mycorda.app.fileBundle.BundleItem
import mycorda.app.fileBundle.BundleItemList
import mycorda.app.fileBundle.FileBundle
import mycorda.app.types.UniqueId


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

    fun addItems(items: Iterable<BundleItem>): FileBundleBuilder {
        items.forEach {
            addItem(it)
        }
        return this
    }

    fun build(): FileBundle {
        return FileBundle(id, name, BundleItemList(items))
    }
}