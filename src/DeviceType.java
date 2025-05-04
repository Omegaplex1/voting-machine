public enum DeviceType {

    PRINTER("Printer"),
    SDCARD("SD Card"),
    LATCH("Latch"),
    BATTERY("Battery"),
    SCREEN("Screen"),
    IDCARD("ID Card Reader");

    private final String name;

    DeviceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() { return name; }
}
