import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class SmartCamera extends SmartDevice {
    private final double mbPerMin;
    public double storage_usage;

    /**
     * Constructs a SmartCamera object with the specified name and sets the initial values for mbPerMin and storage_usage.
     *
     * @param name the name of the Smart Camera
     */
    public SmartCamera(String name, double mbPerMin) {
        super(name);
        this.mbPerMin = mbPerMin;
        this.storage_usage = 0.0;
    }

    /**
     * Creates a new SmartCamera object with the given parameters.
     * @param name the name of the Smart Lamp.
     * @param lineArray an array of Strings from input for add SmartCamera command.
     * @param currentTime the current time,
     * @return SmartLampWithColor a new Smart Lamp With Color object with the given parameters, or null if an error occurred.
     */
    public static SmartCamera createSmartCamera(String name, String[] lineArray, LocalDateTime currentTime) {
        double mbPerMin = Double.parseDouble(lineArray[3]);
        if (mbPerMin <= 0) {
            Main.outputBuilder.append("ERROR: Megabyte value must be a positive number!\n");
            return null;
        }

        SmartCamera camera = new SmartCamera(name, mbPerMin);
        if (lineArray.length == 5){
            if (!(lineArray[4].equals("On") || lineArray[4].equals("Off"))){
                Main.outputBuilder.append("ERROR: Erroneous command!\n");
                return null;
            }
            if (lineArray[4].equals("On")) {
                camera.switchOn(currentTime);
            }
        }
        return camera;
    }

    /**
     * Returns the storage usage of the smart camera with multiplying mbPerMin and the time which used
     * @return storage usage of the smart camera
     */
    public String getStorage_usage(){
        this.storage_usage = this.mbPerMin*this.getActiveTime();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(storage_usage).replace(".", ",");
    }

    @Override
    public String toString() {
        String switchTimeStr;
        if (this.getSwitchTime() != null) {
            switchTimeStr = this.getSwitchTime().format(Main.formatter);
        } else {
            switchTimeStr = "null";
        }
        return "Smart Camera " + this.getName() + " is " + on_or_off + " and used " + this.getStorage_usage() +
                " MB of storage so far (excluding current status), and its time to switch its status is " + switchTimeStr + ".";
    }
}
