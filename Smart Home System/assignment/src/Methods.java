import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Methods {
    public static void setInitialTime(int i, String[] line_array) {
        if (i == 0) {
            String time;
            try {
                time = line_array[1];
                Main.currentTime = LocalDateTime.parse(time, Main.formatter);
                Main.outputBuilder.append("SUCCESS: Time has been set to " + time + "!\n");
            } catch (DateTimeParseException e) {
                Main.outputBuilder.append("ERROR: Format of the initial date is wrong! Program is going to terminate!\n");
                fileOutput.writeToFile("output.txt", String.valueOf(Main.outputBuilder), false, false);
                System.exit(0);
            } catch (Exception e) {
                Main.outputBuilder.append("ERROR: Erroneous command!\n");
                fileOutput.writeToFile("output.txt", String.valueOf(Main.outputBuilder), false, false);
                System.exit(0);
            }
        } else {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
        }
    }

    /**
     * This method handles the "SetTime" command, which updates the current time of the system
     * and updates the time of all the Smart Devices connected to it.
     */
    public static void setTime(String[] line_array) {
        LocalDateTime newTime;
        // Check if the command has the correct number of arguments.
        if (line_array.length != 2) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        try {
            String newTimeStr = line_array[1];
            newTime = LocalDateTime.parse(newTimeStr, Main.formatter);
            // Check if the new time is before the current time.
            if (newTime.isBefore(Main.currentTime)) {
                Main.outputBuilder.append("ERROR: Time cannot be reversed!\n");
                return;
            } else if (newTime.isEqual(Main.currentTime)) {
                Main.outputBuilder.append("ERROR: There is nothing to change!\n");
                return;
            }
        } catch (DateTimeParseException e) {
            Main.outputBuilder.append("ERROR: Time format is not correct!\n");
            return;
        }

        List<SmartDevice> copyOfDevices = new ArrayList<>(Main.all_devices); // make a copy of the original list
        for (int j = 0; j < Main.all_devices.size(); j++) {
            // Handle the case where multiple devices have the same switch time.
            if (j < Main.all_devices.size()-1){
            if (Main.all_devices.get(j + 1).getSwitchTime() != null && Main.all_devices.get(j).getSwitchTime().equals(Main.all_devices.get(j + 1).getSwitchTime())) {
                int k = j + 1;
                while (k < Main.all_devices.size() && Main.all_devices.get(k).getSwitchTime().equals(Main.all_devices.get(j).getSwitchTime())) {
                    k++;
                }
                List<SmartDevice> devicesWithSameSwitchTime = new ArrayList<>(Main.all_devices.subList(j, k));
                for (int l = devicesWithSameSwitchTime.size() - 1; l >= 0; l--) {
                    SmartDevice device = devicesWithSameSwitchTime.get(l);
                    processTime.processForSet(device, newTime);
                }
            }}
            // Create a copy of the all_devices list, sort it by switch time,
            // process the Smart Device, and update the original list with the sorted copy.
            processTime.processForSet(copyOfDevices.get(j), newTime);
            SmartDevice.sortDevicesBySwitchTime(Main.all_devices);
        }
        // Update the current time with the new time.
        Main.currentTime = newTime;
    }

    /**
     * This method handles the "SkipMinutes" command, which skips a given number of minutes
     * and updates the switch times of all the devices accordingly.
     */
    public static void skipMinutes(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 2) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        String minutes_str = line_array[1];
        int minutes;
        try {
            minutes = Integer.parseInt(minutes_str);
        } catch (Exception e) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        if (minutes < 0) {
            Main.outputBuilder.append("ERROR: Time cannot be reversed!\n");
            return;
        } else if (minutes == 0) {
            Main.outputBuilder.append("ERROR: There is nothing to skip!\n");
            return;
        }
        LocalDateTime new_time = Main.currentTime.plusMinutes(minutes);

        List<SmartDevice> copyOfDevices = new ArrayList<>(Main.all_devices); // make a copy of the original list
        for (int j = 0; j < Main.all_devices.size(); j++) {
            // Handle the case where multiple devices have the same switch time.
            if(j < Main.all_devices.size()-1){
            if (Main.all_devices.get(j + 1).getSwitchTime() != null && Main.all_devices.get(j).getSwitchTime().equals(Main.all_devices.get(j + 1).getSwitchTime())) {
                int k = j + 1;
                while (k < Main.all_devices.size() && Main.all_devices.get(k).getSwitchTime().equals(Main.all_devices.get(j).getSwitchTime())) {
                    k++;
                }
                List<SmartDevice> devicesWithSameSwitchTime = new ArrayList<>(Main.all_devices.subList(j, k));
                for (int l = devicesWithSameSwitchTime.size() - 1; l >= 0; l--) {
                    SmartDevice device = devicesWithSameSwitchTime.get(l);
                    processTime.processForSkip(device, new_time);
                }

            }}

            processTime.processForSkip(copyOfDevices.get(j), new_time);
            SmartDevice.sortDevicesBySwitchTime(Main.all_devices); // sort the copy
        }

        Main.currentTime = new_time;
    }

    /**
     * This method handles the "Add" command which adds a new SmartDevice to the system.
     */
    public static void addDevice(String[] line_array) {
        String deviceType = line_array[1];
        String name = line_array[2];
        boolean foundDuplicate = false;
        // Check for duplicates
        for (SmartDevice Device : Main.all_devices) {
            if (Device.getName().equals(name)) {
                foundDuplicate = true;
                break;
            }
        }

        try {
            SmartDevice device;
            boolean b = !(line_array.length == 3 || line_array.length == 4 || line_array.length == 6);
            switch (deviceType) {
                case "SmartCamera":
                    // Check if the command has the correct number of arguments.
                    if (!(line_array.length == 4 || line_array.length == 5)) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }
                    if (foundDuplicate) {
                        Main.outputBuilder.append("ERROR: There is already a smart device with same name!\n");
                        break;
                    }
                    // Create a new SmartCamera and add it to the system
                    device = SmartCamera.createSmartCamera(name, line_array, Main.currentTime);
                    if (device != null) {
                        Main.all_devices.add(device);
                    }
                    break;
                case "SmartPlug":
                    if (!(line_array.length == 3 || line_array.length == 4 || line_array.length == 5)) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }
                    if (foundDuplicate) {
                        Main.outputBuilder.append("ERROR: There is already a smart device with same name!\n");
                        break;
                    }
                    device = SmartPlug.createSmartPlug(name, line_array, Main.currentTime);
                    if (device != null) {
                        Main.all_devices.add(device);
                    }
                    break;
                case "SmartLamp":
                    if (b) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }
                    if (foundDuplicate) {
                        Main.outputBuilder.append("ERROR: There is already a smart device with same name!\n");
                        break;
                    }
                    device = SmartLamp.createSmartLamp(name, line_array, Main.currentTime);
                    if (device != null) {
                        Main.all_devices.add(device);
                    }
                    break;
                case "SmartColorLamp":
                    if (b) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }
                    if (foundDuplicate) {
                        Main.outputBuilder.append("ERROR: There is already a smart device with same name!\n");
                        break;
                    }
                    device = SmartLampWithColor.createSmartLampWithColor(name, line_array, Main.currentTime);
                    if (device != null) {
                        Main.all_devices.add(device);
                    }
                    break;
            }
        } catch (Exception e) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
        }
    }

    /**
     * This case handles the "Remove" command, which removes a smart device from the list of all devices.
     */
    public static void removeDevice(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 2) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        // Get the device name from the command.
        String device2 = line_array[1];
        int index1 = -1;
        // Find the device in the list of all devices.
        for (int j = 0; j < Main.all_devices.size(); j++) {
            SmartDevice device_ = Main.all_devices.get(j);
            if (device_.getName().equals(device2)) {
                index1 = j;
                break;
            }
        }
        // If the device is not found, output an error message.
        if (index1 == -1) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
            return;
        }
        // Output a success message and remove the device from the list of all devices.
        Main.outputBuilder.append("SUCCESS: Information about removed smart device is as follows:\n");
        if (Main.all_devices.get(index1).isOn()) {
            Main.all_devices.get(index1).switchOff(Main.all_devices.get(index1), Main.currentTime);
        }
        Main.outputBuilder.append(Main.all_devices.get(index1) + "\n");
        Main.all_devices.remove(index1);
    }

    /**
     *  Sets the switch time of a specified device to a specified time and sorts all devices by switch time.
     */
    public static void setSwitchTime(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 3) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        String time2 = line_array[2];
        LocalDateTime switchTime;
        String device_name0 = line_array[1];
        int index0 = -1;
        for (int j = 0; j < Main.all_devices.size(); j++) {
            SmartDevice device_ = Main.all_devices.get(j);
            if (device_.getName().equals(device_name0)) {
                index0 = j;
                break;
            }
        }
        if (index0 == -1) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
            return;
        }
        SmartDevice device = Main.all_devices.get(index0);
        try {
            switchTime = LocalDateTime.parse(time2, Main.formatter);
            if (switchTime.isBefore(Main.currentTime)){
                Main.outputBuilder.append("ERROR: Switch time cannot be in the past!\n");
                return;

            } else if (switchTime.isEqual(Main.currentTime)){
                device.setSwitchTime(switchTime);
                SmartDevice.sortDevicesBySwitchTime(Main.all_devices);
                if (device.isOn){
                    device.switchOff(device, Main.currentTime);
                }else{
                    device.switchOn(Main.currentTime);
                }
                device.setSwitchTime(null);

            }else {
                device.setSwitchTime(switchTime);
                SmartDevice.sortDevicesBySwitchTime(Main.all_devices);
            }
        } catch (DateTimeParseException e) {
            Main.outputBuilder.append("ERROR: Time format is not correct!\n");
            return;
        } catch (Exception e){
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
    }

    /**
     This method is used to switch a device on or off based on the input option.
     If the option is "On", the device will be switched on, and if the option is "Off", the device will be switched off.
     */
    public static void switchOnOff(String[] line_array) {
        String deviceName = line_array[1];
        String option = line_array[2];
        SmartDevice device = null;
        for (SmartDevice d : Main.all_devices) {
            if (d.getName().equals(deviceName)) {
                device = d;
                break;
            }
        }
        if (device == null) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
            return;
        }
        switch (option) {
            case "On":

                if (device instanceof SmartPlug) {
                    SmartPlug device0 = (SmartPlug) device;
                    if (device0.isPluggedIn()) {
                        device0.setStartTime(Main.currentTime);
                    }
                }
                if (device.isOn()) {
                    Main.outputBuilder.append("ERROR: This device is already switched on!\n");
                    break;
                }
                device.switchOn(Main.currentTime);
                break;

            case "Off":

                if (!device.isOn()) {
                    Main.outputBuilder.append("ERROR: This device is already switched off!\n");
                    break;
                }

                device.switchOff(device, Main.currentTime);
                break;
        }
    }

    /**
     * This case is used to change the name of a SmartDevice.
     * The case takes the old name and the new name as arguments, and replaces the old name with the new name.
     */
    public static void changeName(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 3) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        Main.a = 0;

        String oldName = line_array[1];
        String newName = line_array[2];
        if (oldName.equals(newName)) {
            Main.outputBuilder.append("ERROR: Both of the names are the same, nothing changed!\n");
            return;
        }
        // Check if the old name and the new name are the same.
        boolean nameFound = false;
        for (SmartDevice device1 : Main.all_devices) {
            if (device1.getName().equals(newName)) {
                Main.outputBuilder.append("ERROR: There is already a smart device with same name!\n");
                nameFound = true;
                break;
            }
        }
        if (nameFound) return;
        // Change the name of the device with the old name to the new name.
        for (SmartDevice device1 : Main.all_devices) {
            if (device1.getName().equals(oldName)) {
                Main.a++;
                device1.setName(newName);
                break;
            }
        }
        if (Main.a == 0) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
        }
    }

    /**
     This case is used to plug in a SmartPlug device.
     if the device is on, it sets the startTime because the device is both on and in plugin after plugIn command
     */
    public static void plugIn(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 3) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        Main.a = 0;
        int ampValue;
        String Name = line_array[1];
        try {
            ampValue = Integer.parseInt(line_array[2]);
        } catch (Exception e) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        // Check if the ampere value is positive.
        if (ampValue <= 0) {
            Main.outputBuilder.append("ERROR: Ampere value must be a positive number!\n");
            return;
        }
        // Find the device with the given name and plug it in.
        for (SmartDevice device1 : Main.all_devices) {
            if (device1.getName().equals(Name)) {
                Main.a++;
                if (device1 instanceof SmartPlug) {
                    SmartPlug smartPlug = (SmartPlug) device1;
                    if (smartPlug.isPluggedIn()) {
                        Main.outputBuilder.append("ERROR: There is already an item plugged in to that plug!\n");
                        break;
                    }
                    smartPlug.setAmpere(ampValue);
                    smartPlug.plugIn();
                    if (smartPlug.isOn()) {
                        smartPlug.setStartTime(Main.currentTime);
                    }
                } else {
                    Main.outputBuilder.append("ERROR: This device is not a smart plug!\n");
                    break;
                }
            }
        }
        if (Main.a == 0) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
        }
    }

    /**
     * This case is used to plug out a SmartPlug device.
     * if the device is on, it calculates duration between current time and the time that device was both plugged in and switched on
     */
    public static void plugOut(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 2) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        Main.a = 0;
        String Name0 = line_array[1];
        SmartPlug device6 = null;
        for (SmartDevice device1 : Main.all_devices) {
            if (device1.getName().equals(Name0)) {
                Main.a++;
                if (device1 instanceof SmartPlug) {
                    device6 = (SmartPlug) device1;

                    if (device6.isPluggedIn()) {
                        device6.plugOut();
                        if (device6.isOn()) {
                            Duration duration = Duration.between(device6.getStartTime(), Main.currentTime);
                            device6.setRunningTime(duration);
                            device6.setStartTime(null);
                        }
                    } else {
                        Main.outputBuilder.append("ERROR: This plug has no item to plug out from that plug!\n");
                        break;
                    }
                } else {
                    Main.outputBuilder.append("ERROR: This device is not a smart plug!\n");
                    break;
                }
            }
        }
        if (Main.a == 0) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
        }
    }

    /**
     * It loops through all the devices in the system to find the smart lamp with the given name.
     * If the device is not a smart lamp, an error message is outputted.
     * If the device is a smart lamp, the code tries to parse the kelvin value from the second argument.
     * If it is not a valid number, an error message is outputted.
     * If the kelvin value is outside the range of 2000K-6500K, an error message is outputted.
     * Otherwise, the kelvin value is set for the smart lamp.
     */
    public static void setKelvin(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 3) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        String name3 = line_array[1];
        SmartLamp lamp1;
        int kelvin;
        Main.a = 0;

        for (SmartDevice device3 : Main.all_devices) {
            if (device3.getName().equals(name3)) {
                Main.a++;
                if (!(device3 instanceof SmartLamp)) {
                    Main.outputBuilder.append("ERROR: This device is not a smart lamp!\n");
                    break;
                } else {

                    try {
                        kelvin = Integer.parseInt(line_array[2]);
                    } catch (NumberFormatException e) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }

                    lamp1 = (SmartLamp) device3;
                    if (kelvin > 6500 || kelvin < 2000) {
                        Main.outputBuilder.append("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                        break;
                    }
                    lamp1.setKelvin(kelvin);
                }
            }
        }
        if (Main.a == 0) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
        }
    }

    /**
     * it looks for the SmartLamp device with the specified name.
     * If found, it sets the brightness to the specified value.
     * If the device is not a SmartLamp or the specified brightness value is not within the acceptable range,
     * an error message is outputted. If no device with the specified name is found, an error message is also outputted.
     */
    public static void setBrightness(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 3) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        Main.a = 0;
        String name2 = line_array[1];
        int brightness;

        for (SmartDevice device3 : Main.all_devices) {
            if (device3.getName().equals(name2)) {
                Main.a++;
                if (!(device3 instanceof SmartLamp)) {
                    Main.outputBuilder.append("ERROR: This device is not a smart lamp!\n");
                    break;
                } else {

                    try {
                        brightness = Integer.parseInt(line_array[2]);
                    } catch (NumberFormatException e) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }

                    SmartLamp lamp = (SmartLamp) device3;
                    if (brightness > 100 || brightness < 0) {
                        Main.outputBuilder.append(("ERROR: Brightness must be in range of 0%-100%!\n"));
                        break;
                    }
                    lamp.setBrightness(brightness);
                }
            }
        }
        if (Main.a == 0) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
        }
    }

    /**
     * This code sets the color of a smart lamp with a given color code value,
     * represented in hexadecimal format. It checks if the device is a smart color lamp.
     * It then sets the color code, color code hex, and color mode of the lamp.
     * If there is no device with the given name, it returns an error message.
     */
    public static void setColorCode(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 3) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        Main.a = 0;
        String name1 = line_array[1];
        int colorCode;

        for (SmartDevice device3 : Main.all_devices) {
            if (device3.getName().equals(name1)) {
                Main.a++;
                if (!(device3 instanceof SmartLampWithColor)) {
                    Main.outputBuilder.append("ERROR: This device is not a smart color lamp!\n");
                } else {

                    try {
                        colorCode = Integer.parseInt(line_array[2].substring(2), 16);
                    } catch (NumberFormatException e) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }

                    if (colorCode < 0 || colorCode > 16777215) {
                        Main.outputBuilder.append("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
                        break;
                    }
                    SmartLampWithColor lamp = (SmartLampWithColor) device3;
                    lamp.setColorCode(colorCode);
                    lamp.setColorCodeHex(line_array[2]);
                    lamp.setColorMode(true);
                }
            }
        }
        if (Main.a == 0) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
        }
    }

    /**
     * The code first checks if the device is a smart lamp and then sets the brightness and kelvin values
     * if they are within a certain range. If the smart lamp also has color capabilities,it sets the color mode to false.
     */
    public static void setWhite(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 4) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        String name4 = line_array[1];
        int brightness0;
        int kelvin0;
        Main.a = 0;

        for (SmartDevice device3 : Main.all_devices) {
            if (device3.getName().equals(name4)) {
                Main.a++;
                if (!(device3 instanceof SmartLamp)) {
                    Main.outputBuilder.append("ERROR: This device is not a smart lamp!\n");
                    break;
                } else {

                    try {
                        brightness0 = Integer.parseInt(line_array[3]);
                        kelvin0 = Integer.parseInt(line_array[2]);
                    } catch (NumberFormatException e) {
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }

                    if (kelvin0 < 2000 || kelvin0 > 6500) {
                        Main.outputBuilder.append("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                        break;
                    }
                    if (brightness0 > 100 || brightness0 < 0) {
                        Main.outputBuilder.append("ERROR: Brightness must be in range of 0%-100%!\n");
                        break;
                    }
                    SmartLamp lamp = (SmartLamp) device3;
                    lamp.setBrightness(brightness0);
                    lamp.setKelvin(kelvin0);
                    if (lamp instanceof SmartLampWithColor) {
                        ((SmartLampWithColor) lamp).setColorMode(false);
                    }
                }
            }
        }
        if (Main.a == 0) {
            Main.outputBuilder.append("ERROR: There is not such a device!\n");
        }
    }

    /**
     * This code handles the "SetColor" command for a smart color lamp.
     * It sets the color code, brightness, and color mode for the lamp.
     * If there is an error, an appropriate error message is appended to the outputBuilder.
     */
    public static void setColor(String[] line_array) {
        // Check if the command has the correct number of arguments.
        if (line_array.length != 4) {
            Main.outputBuilder.append("ERROR: Erroneous command!\n");
            return;
        }
        String name0 = line_array[1];
        int colorCode1;
        int brightness1;
        Main.a = 0;

        for (SmartDevice device3 : Main.all_devices) {
            if (device3.getName().equals(name0)){
                Main.a++;
                if (!(device3 instanceof SmartLampWithColor)) {
                    Main.outputBuilder.append("ERROR: This device is not a smart color lamp!\n");
                    break;
                } else {
                    try {
                        colorCode1 = Integer.parseInt(line_array[2].substring(2), 16);
                        brightness1 = Integer.parseInt(line_array[3]);
                    }catch (NumberFormatException e){
                        Main.outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }

                    if (colorCode1 < 0 || colorCode1 > 16777215){
                        Main.outputBuilder.append("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
                        break;
                    }

                    if (brightness1<0 || brightness1>100){
                        Main.outputBuilder.append("ERROR: Brightness must be in range of 0%-100%!\n");
                        break;
                    }
                    SmartLampWithColor lamp = (SmartLampWithColor) device3;
                    lamp.setColorCode(colorCode1);
                    lamp.setColorCodeHex(line_array[2]);
                    lamp.setBrightness(brightness1);
                    lamp.setColorMode(true);
                }
            }
        }
    }
}