import java.time.Duration;
import java.time.LocalDateTime;

public class processTime {

    /**
     * Processes the switch time of the SmartDevice for the "skip" command.
     * If the switch time is not null and the newTime is after the switch time or equal to the switch time,
     * and the currentTime is before the switch time, the device will be switched off, and its active time and
     * running time (if it is a SmartPlug) will be calculated and set. Otherwise, nothing happens.
     * @param device the SmartDevice to be processed
     * @param newTime the time which added to current time
     */
    public static void processForSkip(SmartDevice device,  LocalDateTime newTime) {
        LocalDateTime switchTime = device.getSwitchTime();
        if (switchTime != null) {
            if ((newTime.isAfter(switchTime) && Main.currentTime.isBefore(switchTime)) || newTime.isEqual(switchTime)) {
                Duration duration;
                if (device.isOn()) {
                    device.switchOff(device, switchTime);
                } else {
                    device.switchOn(switchTime);
                    if (device instanceof SmartPlug) {
                        SmartPlug device1 = (SmartPlug) device;
                        if (device1.isPluggedIn()) {
                            device1.setStartTime(switchTime);
                        }
                    }
                }
            }
        }
    }

    /**
     * Processes the switch time of the SmartDevice for the "set" command.
     * If the switch time is not null and the newTime is after the switch time or equal to the switch time,
     * and the currentTime is before the switch time, the device will be switched off, and its active time and
     * running time (if it is a SmartPlug) will be calculated and set. Otherwise, nothing happens.
     * @param device the SmartDevice to be processed
     * @param newTime the time which added to current time
     */
    public static void processForSet(SmartDevice device, LocalDateTime newTime) {
        LocalDateTime switchTime = device.getSwitchTime();
        if (switchTime != null) {
            if ((newTime.isAfter(switchTime) && Main.currentTime.isBefore(switchTime)) || newTime.isEqual(switchTime)) {
                if (device.isOn()) {
                    device.switchOff(device, switchTime);
                } else {
                    device.switchOn(switchTime);
                    if (device instanceof SmartPlug) {
                        SmartPlug device1 = (SmartPlug) device;
                        if (device1.isPluggedIn()) {
                            device1.setStartTime(switchTime);
                        }
                    }
                }
            }
        }
    }
}
