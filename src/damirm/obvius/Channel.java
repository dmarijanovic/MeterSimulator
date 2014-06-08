package damirm.obvius;

import java.math.BigDecimal;

public class Channel {

    public BigDecimal input;
    public BigDecimal aveRatePerHour ;
    public BigDecimal instantaneousPerHour;
    public BigDecimal minPerHour;
    public BigDecimal maxPerHour;
    
    /*
     * 113523.000,      4 
     * 420.000,         5
     * 418.605,         6
     * 418.605,         7
     * 418.605,         8    
     * 0,0,,,,0,0,,,,0,0,,,
     */

    public Channel() {
//        this.input = new BigDecimal(0); 
//        this.aveRatePerHour = new BigDecimal(1);
//        this.instantaneousPerHour = new BigDecimal(2);
//        this.minPerHour = new BigDecimal(3);
//        this.maxPerHour = new BigDecimal(4);
    }

    public String getJSON() {
        return String.format("%s,%s,%s,%s,",
                input != null ? input : "",
                aveRatePerHour != null ? aveRatePerHour : "",
                instantaneousPerHour != null ? instantaneousPerHour : "",
                minPerHour != null ? minPerHour : "",
                maxPerHour != null ? maxPerHour : "");
    }
    
}
