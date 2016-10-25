/*
 * Copyright (C) 2015 The CyanogenMod Project
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
#include <stdint.h>
/* frameworks/rs/cpu_ref/rsd_cpu.h */
#include "cpu_ref/rsd_cpu.h"
#include "rs_hal.h"

using namespace android;
using namespace android::renderscript;

extern "C" RsdCpuReference * _ZN7android12renderscript15RsdCpuReference6createEPNS0_7ContextEjjPFPKNS1_9CpuSymbolES3_PKcEPFPNS1_9CpuScriptES3_PKNS0_6ScriptEEPFPN4llvm6ModuleEPN3bcc8RSScriptESK_SK_EPFS8_S8_jES8_(Context *rsc, uint32_t version_major,
        uint32_t version_minor, RsdCpuReference::sym_lookup_t lfn, RsdCpuReference::script_lookup_t slfn
        , bcc::RSLinkRuntimeCallback pLinkRuntimeCallback,
        RSSelectRTCallback pSelectRTCallback,
        const char *pBccPluginName
        );

extern "C" RsdCpuReference * _ZN7android12renderscript15RsdCpuReference6createEPNS0_7ContextEjjPFPKNS1_9CpuSymbolES3_PKcEPFPNS1_9CpuScriptES3_PKNS0_6ScriptEEPFPN4llvm6ModuleEPN3bcc8RSScriptESK_SK_EPFS8_S8_jE(Context *rsc, uint32_t version_major,
        uint32_t version_minor, RsdCpuReference::sym_lookup_t lfn, RsdCpuReference::script_lookup_t slfn
        , bcc::RSLinkRuntimeCallback pLinkRuntimeCallback,
        RSSelectRTCallback pSelectRTCallback
        ){
    return _ZN7android12renderscript15RsdCpuReference6createEPNS0_7ContextEjjPFPKNS1_9CpuSymbolES3_PKcEPFPNS1_9CpuScriptES3_PKNS0_6ScriptEEPFPN4llvm6ModuleEPN3bcc8RSScriptESK_SK_EPFS8_S8_jES8_(
           rsc, version_major, version_minor, lfn, slfn, pLinkRuntimeCallback, pSelectRTCallback, "");
}

