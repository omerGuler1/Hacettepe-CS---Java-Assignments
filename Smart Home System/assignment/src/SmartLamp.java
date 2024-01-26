import java.time.LocalDateTime;
/**
 *This class represents a Smart Lamp which is a type of Smart Device with the ability to set its kelvin value and brightness.
 */
public class SmartLamp extends SmartDevice{
    public int Kelvin;
    public int Brightness;

    /**
     * * Constructs a SmartPlug object with the specified name and sets the initial values for kelvin and brightness.
     * @param name the name of the smart lamp.
     */
    public SmartLamp(String name){
        super(name);
        this.Kelvin = 4000;
        this.Brightness = 100;
    }

    /**
     * Creates a new Smart Lamp object with the given parameters.
     * @param name the name of the Smart Lamp.
     * @param lineArray an array of Strings from input for add SmartLamp command.
     * @param currentTime the current time,
     * @return SmartLamp a new Smart Lamp object with the given parameters, or null if an error occurred.
     */
    public static SmartDevice createSmartLamp(String name, String[] lineArray, LocalDateTime currentTime) {
        if (lineArray.length>3){
            if (!(lineArray[3].equals("On") || lineArray[3].equals("Off"))){
                Main.outputBuilder.append("ERROR: Erroneous command!\n");
                return null;
            }
        }
        SmartLamp smartLamp = new SmartLamp(name);
        if (lineArray.length == 4 && lineArray[3].equals("On")){
            smartLamp.switchOn(currentTime);
        } else if (lineArray.length == 6){
            int kelvin = Integer.parseInt(lineArray[4]);
            if (kelvin<2000 || kelvin>6500){
                Main.outputBuilder.append("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                return null;
            }
            int brightness = Integer.parseInt(lineArray[5]);
            if (brightness>100 || brightness<0){
                Main.outputBuilder.append("ERROR: Brightness must be in range of 0%-100%!\n");
                return null;
            }
            if (lineArray[3].equals("On")){
                smartLamp.switchOn(currentTime);
                //
            }
            smartLamp.setKelvin(kelvin);
            smartLamp.setBrightness(brightness);
        }
        return smartLamp;
    }

    /**
     * Gets the kelvin value of the smart lamp.
     * @return int the kelvin value of the smart lamp.
     */
    public int getKelvin() {
        return Kelvin;
    }

    /**
     * Sets the kelvin value of the smart lamp.
     * @param kelvin the kelvin value to be set.
     */
    public void setKelvin(int kelvin) {
        Kelvin = kelvin;
    }

    /**
     * Gets the brightness value of the smart lamp.
     * @return int the brightness value of the smart lamp.
     */
    public int getBrightness() {
        return Brightness;
    }

    /**
     * Sets the brightness value of the smart lamp.
     * @param brightness the brightness value to be set.
     */
    public void setBrightness(int brightness) {
        Brightness = brightness;
    }

    /**
     * Returns a string representation of the Smart Lamp object.
     * @return String a string representation of the Smart Lamp object.
     */
    @Override
    public String toString() {
        String switchTimeStr;
        if (this.getSwitchTime() != null) {
            switchTimeStr = this.getSwitchTime().format(Main.formatter);
        } else {
            switchTimeStr = "null";
        }
        return "Smart Lamp "+this.getName()+" is "+on_or_off+" and its kelvin value is "+this.getKelvin()+"K with "+this.getBrightness()+
                "% brightness, and its time to switch its status is "+switchTimeStr+".";
    }
}