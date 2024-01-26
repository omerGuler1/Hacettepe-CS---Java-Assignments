import java.time.LocalDateTime;

public class SmartLampWithColor extends SmartLamp{
    public int colorCode;
    public boolean colorMode;
    public String colorCodeHex;

    /**
     * Constructs a SmartLampWithColor object with the specified name.
     *
     * @param name the name of the smart plug
     */
    public SmartLampWithColor(String name) {
        super(name);
    }

    /**
     * Creates a new Smart Lamp with Color object with the given parameters.
     * @param name the name of the Smart Lamp.
     * @param lineArray an array of Strings from input for add SmartLampWithColor command.
     * @param currentTime the current time,
     * @return SmartLampWithColor a new Smart Lamp With Color object with the given parameters, or null if an error occurred.
     */
    public static SmartDevice createSmartLampWithColor(String name, String[] lineArray, LocalDateTime currentTime){
        if (lineArray.length>3){
            if (!(lineArray[3].equals("On") || lineArray[3].equals("Off"))){
                Main.outputBuilder.append("ERROR: Erroneous command!\n");
                return null;
            }
        }

        SmartLampWithColor smartLampWithColor = new SmartLampWithColor(name);
        if (lineArray.length == 4 && lineArray[3].equals("On")){
            smartLampWithColor.switchOn(currentTime);
           // smartLampWithColor.setSwitchedOnTime(currentTime);
        } else if (lineArray.length == 6 && (lineArray[4].startsWith("0x") || lineArray[4].startsWith("0X"))){
            int colorCode = Integer.parseInt(lineArray[4].substring(2), 16);
            if (colorCode < 0 || colorCode > 16777215){
                Main.outputBuilder.append("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
                return null;
            }
            int brightness = Integer.parseInt(lineArray[5]);
            if (brightness<0 || brightness>100){
                Main.outputBuilder.append("ERROR: Brightness must be in range of 0%-100%!\n");
                return null;
            }
            if (lineArray[3].equals("On")){
                smartLampWithColor.switchOn(currentTime);
               // smartLampWithColor.setSwitchedOnTime(currentTime);
            }
            smartLampWithColor.setColorCode(colorCode);
            smartLampWithColor.setColorCodeHex(lineArray[4]);
            smartLampWithColor.setBrightness(brightness);
            smartLampWithColor.setColorMode(true);

        } else if (lineArray.length > 3){
            int kelvin = Integer.parseInt(lineArray[4]);
            int brightness = Integer.parseInt(lineArray[5]);
            if (lineArray[3].equals("On")){
                smartLampWithColor.switchOn(currentTime);
             //   smartLampWithColor.setSwitchedOnTime(currentTime);
            }
            smartLampWithColor.setKelvin(kelvin);
            smartLampWithColor.setBrightness(brightness);
            smartLampWithColor.setColorMode(false);
        }
        return smartLampWithColor;
    }
    public boolean getColorMode(){
        return colorMode;
    }
    public void setColorMode(boolean mode){
        this.colorMode = mode;
    }
    public void setColorCode(int colorCode){
        this.colorCode = colorCode;
    }
    public String getColorCodeHex(){
        return colorCodeHex;
    }
    public void setColorCodeHex(String colorCodeHex) {
        this.colorCodeHex = colorCodeHex;
    }

    @Override
    public String toString(){
        String switchTimeStr;
        if (this.getSwitchTime() != null) {
            switchTimeStr = this.getSwitchTime().format(Main.formatter);
        } else {
            switchTimeStr = "null";
        }
        if (!this.getColorMode()){
            return "Smart Color Lamp "+ this.getName()+" is "+on_or_off+ " and its color value is "+this.getKelvin()+"K with "
                    + this.getBrightness() +"% brightness, and its time to switch its status is "+ switchTimeStr+ ".";
        }else {
            return "Smart Color Lamp " + this.getName() + " is " + on_or_off + " and its color value is " + this.getColorCodeHex() + " with "
                    + this.getBrightness() + "% brightness, and its time to switch its status is " + switchTimeStr + ".";
        }
    }
}