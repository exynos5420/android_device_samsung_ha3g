/*
 * Copyright (c) 2014, The CyanogenMod Project. All rights reserved.
 * Copyright (c) 2017, The LineageOS Project. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import static com.android.internal.telephony.RILConstants.*;

import android.content.Context;
import android.telephony.Rlog;
import android.os.AsyncResult;
import android.os.Message;
import android.os.Parcel;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * RIL customization for Galaxy Note 3
 *
 * {@hide}
 */
public class ha3gRIL extends RIL {
    static final boolean RILJ_LOGD = true;
    static final boolean RILJ_LOGV = true;

    public ha3gRIL(Context context, int preferredNetworkType, int cdmaSubscription) {
        this(context, preferredNetworkType, cdmaSubscription, null);
    }

    public ha3gRIL(Context context, int preferredNetworkType,
                   int cdmaSubscription, Integer instanceId) {
        super(context, preferredNetworkType, cdmaSubscription, instanceId);
    }

    @Override
    public void
    acceptCall(Message result) {
        acceptCall(0, result);
    }

    @Override
    public void
    acceptCall(int index, Message result) {
        RILRequest rr =
            RILRequest.obtain(RIL_REQUEST_ANSWER, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mParcel.writeInt(1);
        rr.mParcel.writeInt(index);

        send(rr);
    }

    /**
     *  Translates EF_SMS status bits to a status value compatible with
     *  SMS AT commands.  See TS 27.005 3.1.
     */
    @Override
    private int
    translateStatus(int status) {
        switch (status & 7) {
            case 3:
                return 0;
            case 5:
                return 3;
            case 7:
                return 2;
            default:
                return 1;
        }
    }

    @Override
    public void
    dial(String address, int clirMode, UUSInfo uusInfo, Message result) {
        RILRequest rr = RILRequest.obtain(10, result);
        rr.mParcel.writeString(address);
        rr.mParcel.writeInt(clirMode);
        rr.mParcel.writeInt(0);
        rr.mParcel.writeInt(1);
        rr.mParcel.writeString("");
        if (uusInfo == null) {
            rr.mParcel.writeInt(0);
        } else {
            rr.mParcel.writeInt(1);
            rr.mParcel.writeInt(uusInfo.getType());
            rr.mParcel.writeInt(uusInfo.getDcs());
            rr.mParcel.writeByteArray(uusInfo.getUserData());
        }
        if (RILJ_LOGV) riljLog(rr.serialString() + "> " +
                               requestToString(rr.mRequest) + " " + callDetails);
        send(rr);
    }

    @Override
    private Object
    responseIccCardStatus(Parcel p) {
        IccCardStatus cardStatus = new IccCardStatus();
        cardStatus.setCardState(p.readInt());
        cardStatus.setUniversalPinState(p.readInt());
        cardStatus.mGsmUmtsSubscriptionAppIndex = p.readInt();
        cardStatus.mCdmaSubscriptionAppIndex = p.readInt();
        cardStatus.mImsSubscriptionAppIndex = p.readInt();
        int numApplications = p.readInt();
        if (numApplications > 8) {
            numApplications = 8;
        }
        cardStatus.mApplications = new IccCardApplicationStatus[numApplications];
        for (int i = 0; i < numApplications; i++) {
            IccCardApplicationStatus appStatus = new IccCardApplicationStatus();
            appStatus.app_type = appStatus.AppTypeFromRILInt(p.readInt());
            appStatus.app_state = appStatus.AppStateFromRILInt(p.readInt());
            appStatus.perso_substate = appStatus.PersoSubstateFromRILInt(p.readInt());
            appStatus.aid = p.readString();
            appStatus.app_label = p.readString();
            appStatus.pin1_replaced = p.readInt();
            appStatus.pin1 = appStatus.PinStateFromRILInt(p.readInt());
            appStatus.pin2 = appStatus.PinStateFromRILInt(p.readInt());
            p.readInt(); // pin1_num_retries
            p.readInt(); // puk1_num_retries
            p.readInt(); // pin2_num_retries
            p.readInt(); // puk2_num_retries
            p.readInt(); // perso_unblock_retries

            cardStatus.mApplications[i] = appStatus;
        }
        return cardStatus;
    }

    @Override
    protected Object
    responseCallList(Parcel p) {
        int num = p.readInt();
        ArrayList<DriverCall> response = new ArrayList(num);
        for (int i = 0; i < num; i++) {
            boolean z;
            DriverCall dc = new DriverCall();
            dc.state = DriverCall.stateFromCLCC(p.readInt());
            dc.index = p.readInt();
            dc.id = (dc.index >> 8) & 255;
            dc.index &= 255;
            dc.TOA = p.readInt();
            dc.isMpty = p.readInt() != 0;
            dc.isMT = p.readInt() != 0;
            dc.als = p.readInt();
            if (p.readInt() == 0) {
                z = false;
            } else {
                z = true;
            }
            dc.isVoice = z;
            int type = p.readInt();
            int domain = p.readInt();
            String extras = p.readString();
            dc.callDetails = new CallDetails(type, domain, null);
            dc.callDetails.setExtrasFromCsv(extras);
            if (RILJ_LOGD) Rlog.d(RILJ_LOG_TAG, "dc.index " + dc.index + " dc.id " + dc.id +
                                  " dc.callDetails " + dc.callDetails);
            dc.isVoicePrivacy = p.readInt() != 0;
            dc.number = p.readString();
            dc.numberPresentation = DriverCall.presentationFromCLIP(p.readInt());
            dc.name = p.readString();
            if (RILJ_LOGD) Rlog.d(RILJ_LOG_TAG, "responseCallList dc.name" + dc.name);
            dc.namePresentation = DriverCall.presentationFromCLIP(p.readInt());
            if (p.readInt() == 1) {
                dc.uusInfo = new UUSInfo();
                dc.uusInfo.setType(p.readInt());
                dc.uusInfo.setDcs(p.readInt());
                dc.uusInfo.setUserData(p.createByteArray());
                if (RILJ_LOGV) {
                    riljLogv(String.format("Incoming UUS : type=%d, dcs=%d, length=%d",
                             new Object[]{Integer.valueOf(dc.uusInfo.getType()),
                             Integer.valueOf(dc.uusInfo.getDcs()),
                             Integer.valueOf(dc.uusInfo.getUserData().length)}));
                    riljLogv("Incoming UUS : data (string)=" +
                             new String(dc.uusInfo.getUserData()));
                    riljLogv("Incoming UUS : data (hex): " +
                             IccUtils.bytesToHexString(dc.uusInfo.getUserData()));
                }
            } else {
                if (RILJ_LOGV) riljLogv("Incoming UUS : NOT present!");
            }
            dc.number = PhoneNumberUtils.stringFromStringAndTOA(dc.number, dc.TOA);
            response.add(dc);
            if (dc.isVoicePrivacy) {
                this.mVoicePrivacyOnRegistrants.notifyRegistrants();
                if (RILJ_LOGV) riljLog("InCall VoicePrivacy is enabled");
            } else {
                this.mVoicePrivacyOffRegistrants.notifyRegistrants();
                if (RILJ_LOGV) if (RILJ_LOGV) riljLog("InCall VoicePrivacy is disabled");
            }
        }
        Collections.sort(response);
        if (num == 0 && this.mTestingEmergencyCall.getAndSet(false) &&
            this.mEmergencyCallbackModeRegistrant != null) {
            if (RILJ_LOGV) riljLog("responseCallList: call ended, testing emergency call," +
                                   " notify ECM Registrants");
            this.mEmergencyCallbackModeRegistrant.notifyRegistrant();
        }
        return response;
    }

    /**
     * The RIL can't handle the RIL_REQUEST_SEND_SMS_EXPECT_MORE
     * request properly, so we use RIL_REQUEST_SEND_SMS instead.
     */
    @Override
    public void sendSMSExpectMore(String smscPDU, String pdu, Message result) {
        Rlog.v(RILJ_LOG_TAG, "sendSMSExpectMore");

        RILRequest rr = RILRequest.obtain(RIL_REQUEST_SEND_SMS, result);
        constructGsmSendSmsRilRequest(rr, smscPDU, pdu);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    @Override
    protected void
    processUnsolicited(Parcel p, int type) {
        int dataPosition = p.dataPosition(); //Let's save the parcel data position for later
        Object ret;

        int response = p.readInt();
        if (response >= 11000) {
            try {
                switch (response) {
                    case 11002:
                        ret = responseInts(p);
                        break;
                    case 11010:
                        ret = responseString(p);
                        break;
                    default:
                        Rlog.e(RILJ_LOG_TAG, "Unhandled OEM unsolicited response: " + response);
                        return;
                }
            } catch (Throwable tr) {
                Rlog.e(RILJ_LOG_TAG, "Exception processing unsol response: " + response +
                    "Exception:" + tr.toString());
                return;
            }

            switch (response) {
                case 11002:
                    unsljLogRet(response, responseVoid);
                    if (this.mCatSendSmsResultRegistrant != null) {
                        this.mCatSendSmsResultRegistrant.notifyRegistrant(
                          new AsyncResult(null, responseVoid, null));
                        return;
                    }
                    return;
                case 11010:
                    String str = responseVoid;
                    Rlog.d(RILJ_LOG_TAG, "Executing Am " + str);
                    return;
                default:
                    return;
            }
        } else {
            // Not an OEM response. Lets rewind the parcel
            p.setDataPosition(dataPosition);
            // Now lets forward the response to the superclass so it gets handled
            super.processUnsolicited(p, type);
            return;
        }
    }
}
