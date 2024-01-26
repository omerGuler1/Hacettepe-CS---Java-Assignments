import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * The SmartPlug class represents a smart plug which is a type of Smart Device that can be plugged in or out,
 * and provides methods to track its running time and total energy consumption.
 */
public class SmartPlug extends SmartDevice {
    private final int voltage;
    public double ampere;
    public boolean isPluggedIn;
    public double totalEnergyConsumption;
    public LocalDateTime startTime;
    public long runningTime=0;

    /**
     * Constructs a SmartPlug object with the specified name.
     *
     * @param name the name of the smart plug
     */
    public SmartPlug(String name) {
        super(name);
        this.voltage = 220;
        this.ampere = 0;
    }

    /**
     * Creates a new SmartPlug object with the given parameters.
     *
     * @param name the name of the smart plug device
     * @param line_array an array of Strings from input for add SmartPlug command.
     * @param currentTime the current time of the system
     * @return the SmartPlug object created, or null if there was an error
     */
    public static SmartPlug createSmartPlug(String name, String[] line_array, LocalDateTime currentTime) {
        SmartPlug plug = new SmartPlug(name);
        if (line_array.length == 4 && line_array[3].equals("On")) {
            plug.switchOn(currentTime);
          //  plug.setSwitchedOnTime(currentTime);
        } else if (line_array.length == 5 && line_array[3].equals("On")) {
            double amp_value = Double.parseDouble(line_array[4]);
            if (amp_value <= 0) {
                Main.outputBuilder.append("ERROR: Ampere value must be a positive number!\n");
                return null;
            }
            plug.setAmpere(amp_value);
            plug.switchOn(currentTime);
            plug.plugIn();
            plug.setStartTime(currentTime);
        }
        return plug;
    }

    /**
     * Sets the running time(time that device consumes energy) of the smart plug device,
     * (A plug can only consume energy when it is both plugged in and switched on)
     *
     * @param active_time the duration of time that the smart plug device was active
     */
    public void setRunningTime(Duration active_time) {
        long active_time_in_minutes = active_time.toMinutes();
        runningTime += active_time_in_minutes;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    /**
     * Sets the start time of the smart plug device to the specified time.
     * (when plug is switched on and plug in)
     * @param time the start time of the smart plug device
     */
    public void setStartTime(LocalDateTime time){
        this.startTime = time;
    }
    public int getVoltage() {
        return voltage;
    }
    public double getAmpere() {
        return ampere;
    }

    public void setAmpere(double ampere) {
        this.ampere = ampere;
    }
    public void plugIn() {
        isPluggedIn = true;
    }

    public void plugOut() {
        isPluggedIn = false;
    }
    public boolean isPluggedIn() {
        return isPluggedIn;
    }

    /**
     * Returns the energy consumption of the smart plug device
     * with multiplying the values of voltage, ampere and the total time(in hours) that device both switched on and plugged in.
     *
     * @return the energy consumption of the smart plug device
     */
    public String getTotalEnergyConsumption() {
        this.totalEnergyConsumption = this.getVoltage()*this.getAmpere()*this.getRunningTime()*1/60.0;
        this.totalEnergyConsumption = Math.round(totalEnergyConsumption*100.0)/100.0;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(totalEnergyConsumption).replace(".", ",");
    }

    /**
     * Returns a string representation of the Smart Plug object.
     * @return String a string representation of the Smart Plug object.
     */
    @Override
    public String toString(){
        String switchTimeStr;
        if (this.getSwitchTime() != null) {
            switchTimeStr = this.getSwitchTime().format(Main.formatter);
        } else {
            switchTimeStr = "null";
        }

         return "Smart Plug "+this.getName()+" is "+on_or_off+" and consumed "+this.getTotalEnergyConsumption()+
            "W so far (excluding current device), and its time to switch its status is "+switchTimeStr+".";
    }
}
