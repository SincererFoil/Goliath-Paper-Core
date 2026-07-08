package ch.mcserver.goliathPaperCore.module.inventory;

public class InventoryData {
    private String uuid;
    private String inventory;
    private String armor;
    private String offhand;

    public InventoryData(String uuid, String inventory, String armor, String offhand) {
        this.uuid = uuid;
        this.inventory = inventory;
        this.armor = armor;
        this.offhand = offhand;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getOffhand() {
        return offhand;
    }

    public void setOffhand(String offhand) {
        this.offhand = offhand;
    }

    public String getArmor() {
        return armor;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }
}
