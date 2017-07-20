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

    /**********************************************************
     * SAMSUNG RESPONSE
     **********************************************************/
    private static final int RIL_UNSOL_STK_SEND_SMS_RESULT = 11002;
    private static final int RIL_UNSOL_STK_CALL_CONTROL_RESULT =11003;
    private static final int RIL_UNSOL_DEVICE_READY_NOTI = 11008;
    private static final int RIL_UNSOL_AM = 11010;
    private static final int RIL_UNSOL_SIM_PB_READY = 11021;

    // Number of per-network elements expected in QUERY_AVAILABLE_NETWORKS's response.
    // 4 elements is default, but many RILs actually return 5, making it impossible to
    // divide the response array without prior knowledge of the number of elements.
    protected int mQANElements = SystemProperties.getInt("ro.ril.telephony.mqanelements", 4);

    public ha3gRIL(Context context, int preferredNetworkType, int cdmaSubscription) {
        this(context, preferredNetworkType, cdmaSubscription, null);
    }

    public ha3gRIL(Context context, int preferredNetworkType,
                   int cdmaSubscription, Integer instanceId) {
        super(context, preferredNetworkType, cdmaSubscription, instanceId);
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

    @Override
    public void
    acceptCall(Message result) {
        acceptCall(0, result);
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
        dial(address, clirMode, uusInfo, null, result);
    }

    @Override
    public void
    dial(String address, int clirMode, UUSInfo uusInfo, CallDetails callDetails, Message result) {
        RILRequest rr = RILRequest.obtain(10, result);
        rr.mParcel.writeString(address);
        rr.mParcel.writeInt(clirMode);
        if (callDetails != null) {
            rr.mParcel.writeInt(callDetails.call_type);
            rr.mParcel.writeInt(callDetails.call_domain);
            rr.mParcel.writeString(callDetails.getCsvFromExtras());
        } else {
            rr.mParcel.writeInt(0);
            rr.mParcel.writeInt(1);
            rr.mParcel.writeString("");
        }
        if (uusInfo == null) {
            rr.mParcel.writeInt(0);
        } else {
            rr.mParcel.writeInt(1);
            rr.mParcel.writeInt(uusInfo.getType());
            rr.mParcel.writeInt(uusInfo.getDcs());
            rr.mParcel.writeByteArray(uusInfo.getUserData());
        }
        if (RILJ_LOGV) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest) + " " + callDetails);
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
            appStatus.pin1_num_retries = p.readInt();
            appStatus.puk1_num_retries = p.readInt();
            appStatus.pin2_num_retries = p.readInt();
            appStatus.puk2_num_retries = p.readInt();
            appStatus.perso_unblock_retries = p.readInt();
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
            if (RILJ_LOGD) Rlog.d(RILJ_LOG_TAG, "dc.index " + dc.index + " dc.id " + dc.id + " dc.callDetails " + dc.callDetails);
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
                    riljLogv(String.format("Incoming UUS : type=%d, dcs=%d, length=%d", new Object[]{Integer.valueOf(dc.uusInfo.getType()), Integer.valueOf(dc.uusInfo.getDcs()), Integer.valueOf(dc.uusInfo.getUserData().length)}));
                    riljLogv("Incoming UUS : data (string)=" + new String(dc.uusInfo.getUserData()));
                    riljLogv("Incoming UUS : data (hex): " + IccUtils.bytesToHexString(dc.uusInfo.getUserData()));
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
        if (num == 0 && this.mTestingEmergencyCall.getAndSet(false) && this.mEmergencyCallbackModeRegistrant != null) {
            if (RILJ_LOGV) riljLog("responseCallList: call ended, testing emergency call, notify ECM Registrants");
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
        Rlog.v(RILJ_LOG_TAG, "XMM7260: sendSMSExpectMore");

        RILRequest rr = RILRequest.obtain(RIL_REQUEST_SEND_SMS, result);
        constructGsmSendSmsRilRequest(rr, smscPDU, pdu);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    // This method is used in the search network functionality.
    // See mobile network setting -> network operators
    @Override
    protected Object // ToDo: check this method out later
    responseOperatorInfos(Parcel p) {
        String strings[] = (String[])responseStrings(p);
        ArrayList<OperatorInfo> ret;

        if (strings.length % mQANElements != 0) {
            throw new RuntimeException("RIL_REQUEST_QUERY_AVAILABLE_NETWORKS: invalid response. Got "
                                       + strings.length + " strings, expected multiple of " + mQANElements);
        }

        ret = new ArrayList<OperatorInfo>(strings.length / mQANElements);
        for (int i = 0 ; i < strings.length ; i += mQANElements) {
            String strOperatorLong = strings[i+0];
            String strOperatorNumeric = strings[i+2];
            String strState = strings[i+3].toLowerCase();

            Rlog.v(RILJ_LOG_TAG,
                   "XMM7260: Add OperatorInfo: " + strOperatorLong +
                   ", " + strOperatorLong +
                   ", " + strOperatorNumeric +
                   ", " + strState);

            ret.add(new OperatorInfo(strOperatorLong, // operatorAlphaLong
                                     strOperatorLong, // operatorAlphaShort
                                     strOperatorNumeric,    // operatorNumeric
                                     strState));  // stateString
        }

        return ret;
    }

    public void
    setSimPower(int on, Message result) {
        RILRequest rr = RILRequest.obtain(10023, result);
        rr.mParcel.writeInt(1);
        rr.mParcel.writeInt(on);
        riljLog(rr.serialString() + "> " + requestToString(rr.mRequest) + " int : " + on);
        send(rr);
    }

    @Override
    protected RILRequest
    processSolicited (Parcel p, int type) {
        int dataPosition = p.dataPosition(); //Let's save the parcel data position for later

        int serial = p.readInt();
        int error = p.readInt();

        RILRequest rr = findAndRemoveRequestFromList(serial);

        if (rr == null) {
            Rlog.w(RILJ_LOG_TAG, "Unexpected solicited response! sn: " + serial + " error: " + error);
            return null;
        }

        Object obj = null;
        if (error == 0 || p.dataAvail() > 0) {
            try {
                switch (rr.mRequest) {
                    case 10001:
                        obj = responseVoid(p);
                        break;
                    case 10002:
                        obj = responseVoid(p);
                        break;
                    case 10003:
                        obj = responseFailCause(p);
                        break;
                    case 10004:
                        obj = responseVoid(p);
                        break;
                    case 10005:
                        obj = responseVoid(p);
                        break;
                    case 10006:
                        obj = responseVoid(p);
                        break;
                    case 10007:
                        obj = responseVoid(p);
                        break;
                    case 10008:
                        obj = responseCbSettings(p);
                        break;
                    case 10009:
                        obj = responseInts(p);
                        break;
                    case 10010:
                        obj = responseSIM_PB(p);
                        break;
                    case 10011:
                        obj = responseInts(p);
                        break;
                    case 10012:
                        obj = responseInts(p);
                        break;
                    case 10013:
                        obj = responseSIM_LockInfo(p);
                        break;
                    case 10014:
                        obj = responseVoid(p);
                        break;
                    case 10015:
                        obj = responseVoid(p);
                        break;
                    case 10016:
                        obj = responsePreferredNetworkList(p);
                        break;
                    case 10017:
                        obj = responseInts(p);
                        break;
                    case 10018:
                        obj = responseInts(p);
                        break;
                    case 10019:
                        obj = responseVoid(p);
                        break;
                    case 10020:
                        obj = responseSMS(p);
                        break;
                    case 10021:
                        obj = responseVoid(p);
                        break;
                    case 10022:
                        obj = responseVoid(p);
                        break;
                    case 10023:
                        obj = responseSimPowerDone(p);
                        break;
                    case 10025:
                        obj = responseBootstrap(p);
                        break;
                    case 10026:
                        obj = responseNaf(p);
                        break;
                    case 10027:
                        obj = responseString(p);
                        break;
                    case 10028:
                        obj = responseVoid(p);
                        break;
                    case 10029:
                        obj = responseInts(p);
                        break;
                    case 10030:
                        obj = responseVoid(p);
                        break;
                    case 10031:
                        obj = responseInts(p);
                        break;
                    case 10032:
                        obj = responseVoid(p);
                        break;
                    case 10033:
                        obj = responseVoid(p);
                        break;
                    default:
                        // Not an OEM request. Lets rewind the parcel
                        p.setDataPosition(dataPosition);
                        // Now lets forward the request to the superclass so it gets handled
                        super.processSolicited(p, type);
                        return;
                }
            } catch (Throwable tr) {
                Rlog.w(RILJ_LOG_TAG, rr.serialString() + "< " + requestToString(rr.mRequest) + " exception, possible invalid RIL response", tr);
                if (rr.mResult == null) {
                    return rr;
                }
                AsyncResult.forMessage(rr.mResult, null, tr);
                rr.mResult.sendToTarget();
                return rr;
            }
        }
        riljLog(rr.serialString() + "< " + requestToString(rr.mRequest) + " " + retToString(rr.mRequest, obj));
        if (error != 0) {
            return rr;
        }
        AsyncResult.forMessage(rr.mResult, obj, null);
        rr.mResult.sendToTarget();
        return rr;
    }

    @Override
    protected void //ToDo: rewrite this thing to handle the OEM unsolicited responses
    processUnsolicited(Parcel p, int type) {
        Object ret;

        int dataPosition = p.dataPosition();
        int origResponse = p.readInt();
        int newResponse = origResponse;

        /* Remap incorrect respones or ignore them */
        switch (origResponse) {
            case RIL_UNSOL_STK_CALL_CONTROL_RESULT:
            case RIL_UNSOL_DEVICE_READY_NOTI: /* Registrant notification */
            case RIL_UNSOL_SIM_PB_READY: /* Registrant notification */
                Rlog.v(RILJ_LOG_TAG,
                       "XMM7260: ignoring unsolicited response " +
                       origResponse);
                return;
        }

        if (newResponse != origResponse) {
            riljLog("SlteRIL: remap unsolicited response from " +
                    origResponse + " to " + newResponse);
            p.setDataPosition(dataPosition);
            p.writeInt(newResponse);
        }

        switch (newResponse) {
            case RIL_UNSOL_AM:
                ret = responseString(p);
                break;
            case RIL_UNSOL_STK_SEND_SMS_RESULT:
                ret = responseInts(p);
                break;
            default:
                // Rewind the Parcel
                p.setDataPosition(dataPosition);

                // Forward responses that we are not overriding to the super class
                super.processUnsolicited(p, type);
                return;
        }

        switch (newResponse) {
            case RIL_UNSOL_AM:
                String strAm = (String)ret;
                // Add debug to check if this wants to execute any useful am command
                Rlog.v(RILJ_LOG_TAG, "XMM7260: am=" + strAm);
                break;
        }
    }
}
