public class MenuItem {
    int index;
    String itemName;

    MenuItem(int index, String itemName) {
        this.index = index;
        this.itemName = itemName;
    }

    int getIndex() {
        return index;
    }

    String getItemName() {
        return itemName;
    }
}
