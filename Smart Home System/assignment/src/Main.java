import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static StringBuilder outputBuilder = new StringBuilder();
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    public static LocalDateTime currentTime = null;
    public static List<SmartDevice> all_devices = new ArrayList<>();
    public static int a; // we will use this to understand there is a device or not in the desired name
    public static String output_file;


    public static void main(String[] args) {
        String input_file = args[0];
        output_file = args[1];
        String[] fileContents;
        String[] newArray = fileInput.readFile(input_file, true, true);
        assert newArray != null;
        boolean cmd;
        // if last command is not "ZReport", make it "ZReport"
        if (!(newArray[newArray.length - 1].equals("ZReport"))){
            fileContents = new String[newArray.length + 1];
            System.arraycopy(newArray, 0, fileContents, 0, newArray.length);
            fileContents[fileContents.length - 1] = "ZReport";
            cmd = true;
        } else {
            fileContents = new String[newArray.length];
            System.arraycopy(newArray, 0, fileContents, 0, newArray.length);
            cmd = false;
        }

        // loop through each line from txt, if a line from input.txt does not empty
        for (int i = 0; i < fileContents.length; i++) {
            String[] line_array = fileContents[i].split("\t");
            String command = line_array[0];
            if (cmd && i == fileContents.length-1){
                outputBuilder.append(fileContents[i] + ":\n");
            } else {
                outputBuilder.append("COMMAND: " + fileContents[i] + "\n");
            }


            if (line_array.length != 2 && (!line_array[0].equals("setInitialTime") && (i==0))) {
                outputBuilder.append("ERROR: First command must be set initial time! Program is going to terminate!\n");
                fileOutput.writeToFile("output.txt", String.valueOf(outputBuilder), false, false);
                System.exit(0);
            }

            // execute command based on input
            switch (command) {

                // Sets the initial time based on the first command in the input file.
                case "SetInitialTime":
                    Methods.setInitialTime(i, line_array);
                    break;


                case "SetTime":
                    Methods.setTime(line_array);
                    break;

                case "SkipMinutes":
                    Methods.skipMinutes(line_array);
                    break;

                case "Add":
                    Methods.addDevice(line_array);
                    break;

                case "Remove":
                    Methods.removeDevice(line_array);
                    break;

                case "SetSwitchTime":
                    Methods.setSwitchTime(line_array);
                    break;

                case "Switch":
                    Methods.switchOnOff(line_array);
                    break;

                case "ChangeName":
                    Methods.changeName(line_array);
                    break;

                case "PlugIn":
                    Methods.plugIn(line_array);
                    break;

                case "PlugOut":
                    Methods.plugOut(line_array);
                    break;

                case "SetKelvin":
                    Methods.setKelvin(line_array);
                    break;

                case "SetBrightness":
                    Methods.setBrightness(line_array);
                    break;

                case "SetColorCode":
                    Methods.setColorCode(line_array);
                    break;

                case "SetWhite":
                    Methods.setWhite(line_array);
                    break;

                case "SetColor":
                    Methods.setColor(line_array);
                    break;

                /*
                     This case proceeds to get the switch time of the first device in the list of all devices,
                     and update the switch times of all devices accordingly using the processTime method.
                     It then sorts the devices by switch time,
                     If there are no devices(has a switch time) in the list, it returns an error message.
                */
                case "Nop":
                    // Check if the command has the correct number of arguments.
                    if (line_array.length != 1) {
                        outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }
                    try{
                        LocalDateTime newTime0 = all_devices.get(0).getSwitchTime();
                        if (newTime0 == null){
                            outputBuilder.append("ERROR: There is nothing to switch!\n");
                            break;
                        }
                        processTime.processForSet(all_devices.get(0), newTime0);
                        SmartDevice.sortDevicesBySwitchTime(all_devices);
                        currentTime = newTime0;
                    } catch (IndexOutOfBoundsException e){
                        outputBuilder.append("ERROR: There is nothing to switch!\n");
                    }
                    break;

                /*
                    This case prints the devices' information respectively from all devices list
                */
                case "ZReport":
                    // Check if the command has the correct number of arguments.
                    if (line_array.length != 1) {
                        outputBuilder.append("ERROR: Erroneous command!\n");
                        break;
                    }
                    String formattedTime = currentTime.format(formatter);
                    outputBuilder.append("Time is:\t" + formattedTime + "\n");
                    for (SmartDevice device_ : all_devices){
                        outputBuilder.append(device_ + "\n");
                    }
                    break;

                default:
                    outputBuilder.append("ERROR: Erroneous command!\n");
                    break;
            }
        }

        fileOutput.writeToFile(output_file, String.valueOf(outputBuilder), false, false);
    }
}
