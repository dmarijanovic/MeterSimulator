package damirm.eagle;

import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CurrentSummationFragment extends EaglePulseFragment {

    private String mDeviceMacId;
    
    private String mMeterMacId;
    
    private Date mDate;
    
    private String mSummationDelivered;
    
    private long mSummationReceived;
    
    private long mMultiplier;
    
    private long mDivisor;
    
    private String mDigitsRight;
    
    private String mDigitsLeft;
    
    private String mSuppressLeadingZero;
    
    public CurrentSummationFragment() {
        mDeviceMacId = toHex(10000000);
        mMeterMacId = toHex(20000000);
        mDate = new Date();
        mMultiplier = 0;
        mDivisor = 0;
    }

    public void increment(int pulseIncrement) {
        mSummationReceived += pulseIncrement;
        mDate = new Date();
    }
    
    @Override
    public Element getXmlFragment(Document document) {
        Element currentSummationDelivered = document.createElement("CurrentSummationDelivered");
        
        Element deviceMacId = document.createElement("DeviceMacId");
        Element meterMacId = document.createElement("MeterMacId");
        Element timeStamp = document.createElement("TimeStamp");
        Element summationDelivered = document.createElement("SummationDelivered");
        Element summationReceived = document.createElement("SummationReceived");
        Element multiplier = document.createElement("Multiplier");
        Element divisor = document.createElement("Divisor");
        Element digitsRight = document.createElement("DigitsRight");
        Element digitsLeft = document.createElement("DigitsLeft");
        Element suppressLeadingZero = document.createElement("SuppressLeadingZero");
        
        deviceMacId.setTextContent(mDeviceMacId);
        meterMacId.setTextContent(mMeterMacId);
        timeStamp.setTextContent(toHex(getEpochTimeStamp(mDate)));
        summationDelivered.setTextContent(mSummationDelivered);
        summationReceived.setTextContent(toHex(mSummationReceived));
        multiplier.setTextContent(toHex(mMultiplier));
        divisor.setTextContent(toHex(mDivisor));
        digitsRight.setTextContent(mDigitsRight);
        digitsLeft.setTextContent(mDigitsLeft);
        suppressLeadingZero.setTextContent(mSuppressLeadingZero);
        
        currentSummationDelivered.appendChild(deviceMacId);
        currentSummationDelivered.appendChild(meterMacId);
        currentSummationDelivered.appendChild(timeStamp);
        currentSummationDelivered.appendChild(summationDelivered);
        currentSummationDelivered.appendChild(summationReceived);
        currentSummationDelivered.appendChild(multiplier);
        currentSummationDelivered.appendChild(divisor);
        currentSummationDelivered.appendChild(digitsRight);
        currentSummationDelivered.appendChild(digitsLeft);
        currentSummationDelivered.appendChild(suppressLeadingZero);
        
        return currentSummationDelivered;
    }

    public CurrentSummationFragment getCopy() {
        CurrentSummationFragment currentSummationFragment = new CurrentSummationFragment();
        currentSummationFragment.mDeviceMacId = mDeviceMacId;
        currentSummationFragment.mMeterMacId = mMeterMacId;
        currentSummationFragment.mDate = mDate;
        currentSummationFragment.mSummationDelivered = mSummationDelivered;
        currentSummationFragment.mSummationReceived = mSummationReceived;
        currentSummationFragment.mMultiplier = mMultiplier;
        currentSummationFragment.mDivisor = mDivisor;
        currentSummationFragment.mDigitsRight = mDigitsRight;
        currentSummationFragment.mDigitsLeft = mDigitsLeft;
        currentSummationFragment.mSuppressLeadingZero = mSuppressLeadingZero;

        return currentSummationFragment;
    }
    
}
