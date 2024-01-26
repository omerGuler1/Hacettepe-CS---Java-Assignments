import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 The SmartDevice class is an abstract class that represents a smart device.
 It contains attributes and methods that are common to all smart devices.
 */
public abstract class SmartDevice {
    public String name;

    /**
     * Whether the device is on or off
     */
    public boolean isOn;

    /**
     * The state of the device - on or off (if isOn true then this variable is on)
     */
    public String on_or_off = "off";

    /**
     * The time when the device will be switched
     */
    public LocalDateTime switchTime;

    /**
     * The time duration for which the device was active
     */
    public long activeTime;

    /**
     * The time when the device was switched on,
     * it is necessary to calculate active time
     */
    private LocalDateTime switchedOnTime;

    /**
     * Constructor for the SmartDevice class
     * @param name The name of the device
     */
    public SmartDevice(String name) {
        this.name = name;
        isOn = false;
        switchTime = null;
        this.activeTime = 0;
    }

    public LocalDateTime getSwitchedOnTime() {
        return switchedOnTime;
    }

    public void setSwitchedOnTime(LocalDateTime switchedOnTime) {
        this.switchedOnTime = switchedOnTime;
    }

    /**
     * Switches on the device and sets the SwitchedOnTime in order to access when active time is being calculated
     * @param currentTime The current time when the device is switched on
     */
    public void switchOn(LocalDateTime currentTime) {
        isOn = true;
        on_or_off = "on";
        setSwitchTime(null);
        setSwitchedOnTime(currentTime);
    }

    /**
     * Switches off the device
     * Calculates the time interval between current time(switched off time) and the time that the device was switched on
     * @param device The device to be switched off
     * @param currentTime The current time when the device is switched off
     */
    public void switchOff(SmartDevice device, LocalDateTime currentTime) {
        if (!isOn) {
            Main.outputBuilder.append("ERROR: This device is already switched off!\n");
            return;
        }

        isOn = false;
        on_or_off = "off";
        if (device instanceof SmartPlug){
            SmartPlug plug =  (SmartPlug) device;
            if (plug.isPluggedIn()){
                Duration duration = Duration.between(plug.getStartTime(), currentTime);
                plug.setRunningTime(duration);
                plug.setStartTime(null);
                setSwitchTime(null);
                sortDevicesBySwitchTime(Main.all_devices);
            }
        } else{
            Duration activeDuration = Duration.between(switchedOnTime, currentTime);
            setActiveTime(activeDuration);
            setSwitchTime(null);
            sortDevicesBySwitchTime(Main.all_devices);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOn(){
        return isOn;
    }

    /**
     * Gets the time duration for which the device was active(is on)
     * @return The time duration for which the device was active
     */
    public long getActiveTime(){
        return activeTime;
    }

    public void setActiveTime(Duration active_time) {
        long active_time_in_minutes = active_time.toMinutes();
        activeTime += active_time_in_minutes;
    }

    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(LocalDateTime time) {
        switchTime = time;
    }

    /**
     * Sorts a list of SmartDevice objects by their switch time in ascending order.
     * If two devices have the same switch time, they are sorted based on their index in the list.
     * @param devices the list of SmartDevice objects to be sorted
     */
    public static void sortDevicesBySwitchTime(List<SmartDevice> devices) {
        int n = devices.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                SmartDevice d1 = devices.get(j);
                SmartDevice d2 = devices.get(j + 1);
                if (d1.getSwitchTime() == null && d2.getSwitchTime() != null) {
                    SmartDevice temp = d1;
                    devices.set(j, d2);
                    devices.set(j + 1, temp);
                } else if (d1.getSwitchTime() == null || d2.getSwitchTime() == null){
                    continue;
                } else {
                    int cmp = d1.getSwitchTime().compareTo(d2.getSwitchTime());
                    if (cmp == 0) { // switch times are equal
                        cmp = Integer.compare(devices.indexOf(d1), devices.indexOf(d2));
                    }
                    if (cmp > 0) {
                        SmartDevice temp = d1;
                        devices.set(j, d2);
                        devices.set(j + 1, temp);
                    }
                }
            }
        }
    }
}
