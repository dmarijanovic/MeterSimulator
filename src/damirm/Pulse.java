package damirm;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Pulse {
    
    private Date date;

    private SimpleDateFormat dateFormat;
    
    public Pulse() {
        this.date = new Date();        
    }

    public Pulse(String format) {
        this();
        dateFormat = new SimpleDateFormat(format);
    }
    
    public String getDate() {
        return dateFormat.format(date);
    }

    public long getTimestemp() {
        return date.getTime();
    }

}
