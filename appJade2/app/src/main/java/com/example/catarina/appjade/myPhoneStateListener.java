package com.example.catarina.appjade;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

/**
 * Created by Catarina on 29/11/2015.
 */
public class myPhoneStateListener extends PhoneStateListener {
    public int signalStrengthValue;

    public int getSignal(SignalStrength signalStrength){
        super.onSignalStrengthsChanged(signalStrength);
        if (signalStrength.isGsm()) {
            if (signalStrength.getGsmSignalStrength() != 99)
                signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
            else
                signalStrengthValue = signalStrength.getGsmSignalStrength();
        } else {
            signalStrengthValue = signalStrength.getCdmaDbm();
        }
        return signalStrengthValue;
    }

//    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//
//        super.onSignalStrengthsChanged(signalStrength);
//        if (signalStrength.isGsm()) {
//            if (signalStrength.getGsmSignalStrength() != 99)
//                signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
//            else
//                signalStrengthValue = signalStrength.getGsmSignalStrength();
//        } else {
//            signalStrengthValue = signalStrength.getCdmaDbm();
//        }
//        //return signalStrengthValue;
//    }
}
