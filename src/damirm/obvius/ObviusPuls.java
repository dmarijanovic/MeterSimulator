package damirm.obvius;

import java.math.BigDecimal;

import damirm.Pulse;

public class ObviusPuls extends Pulse {
    
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    
    public Channel[] chennels;

    public ObviusPuls() {
        super(dateFormat);
        this.chennels = new Channel[4];
        for (int i = 0; i < chennels.length; i++) {
            chennels[i] = new Channel();
        }
    }

    public void setChannel(BigDecimal input, int channel) {
        chennels[channel].input = input;
    }

    public String getCSV() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDate());
        sb.append(",0,0,0");

        for (int i = 0; i < chennels.length; i++) {
            Channel channel = chennels[i];
            sb.append(",");                
            sb.append(channel.input != null ? channel.input : "");
            sb.append(",");
            sb.append(channel.aveRatePerHour != null ? channel.aveRatePerHour : "");
            sb.append(",");
            sb.append(channel.instantaneousPerHour != null ? channel.instantaneousPerHour : "");
            sb.append(",");
            sb.append(channel.minPerHour != null ? channel.minPerHour : "");
            sb.append(",");
            sb.append(channel.maxPerHour != null ? channel.maxPerHour : "");
        }

        return sb.toString();
    }

}
