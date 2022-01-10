package mycorda.app.fileBundle


class TextBundleItemTest : BaseBundleItemTest() {
    override fun buildBundleItem(path: String): BundleItem = TextBundleItem(path,"foo")


}