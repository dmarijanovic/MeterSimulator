package damirm.eagle;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import damirm.Pulse;

public abstract class EaglePulseFragment extends Pulse {
    
    public String macId;

    public EaglePulseFragment() {
    }
    
    public abstract Element getXmlFragment(Document document);

    protected String toHex(long l) {
        return "0x" + Long.toHexString(l);
    }

    protected long getEpochTimeStamp(Date currentDate) {
        Calendar epoch = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        epoch.set(2000 ,Calendar.JANUARY, 1, 0, 0, 0);
        epoch.set(Calendar.MILLISECOND, 0);
        Date date = epoch.getTime();
        return (currentDate.getTime() - date.getTime()) / 1000;
    }
    
}
