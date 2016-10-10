/*
 * Copyright (C) 2016 The CyanogenMod Project
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

package org.cyanogenmod.hardware;

import org.cyanogenmod.internal.util.FileUtils;

/**
 * S-Pen Detection
 */
public class HighTouchSensitivity {
    private static final String CONTROL_PATH = "/sys/class/sec/sec_epen/epen_saving_mode";

    /**
     * Whether device supports S-Pen Detection.
     *
     * @return boolean Supported devices must return always true
     */
    public static boolean isSupported() {
        return FileUtils.isFileReadable(CONTROL_PATH) &&
                FileUtils.isFileWritable(CONTROL_PATH);
    }

    /**
     * This method return the current activation status of S-Pen Detection
     *
     * @return boolean Must be false if S-Pen Detection is not supported or not activated,
     * or the operation failed while reading the status; true in any other case.
     */
    public static boolean isEnabled() {
        return FileUtils.readOneLine(CONTROL_PATH).equals("0");
    }

    /**
     * This method allows to setup S-Pen Detection status.
     *
     * @param status The new S-Pen Detection status
     * @return boolean Must be false if S-Pen Detection is not supported or the operation
     * failed; true in any other case.
     */
    public static boolean setEnabled(boolean state) {
        return FileUtils.writeLine(CONTROL_PATH, (state ? "0" : "1"));
    }
}