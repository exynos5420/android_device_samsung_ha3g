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

#include "rs_hal.h"
using namespace android;
using namespace android::renderscript;
extern "C" void _ZN7android12renderscript12rsrSetObjectEPKNS0_7ContextEPvPNS0_10ObjectBaseE(android::renderscript::Context const*,
											    void*,
											    android::renderscript::ObjectBase*);

extern "C" void _ZN7android12renderscript12rsrSetObjectEPKNS0_7ContextEPPNS0_10ObjectBaseES5_(Context const* rsc,
											      ObjectBase** dst,
											      ObjectBase* src){
	_ZN7android12renderscript12rsrSetObjectEPKNS0_7ContextEPvPNS0_10ObjectBaseE(rsc, (void*)dst, src);
}
 
extern "C" void _ZN7android12renderscript14rsrClearObjectEPKNS0_7ContextEPv(android::renderscript::Context const*, void*);

extern "C" void _ZN7android12renderscript14rsrClearObjectEPKNS0_7ContextEPPNS0_10ObjectBaseE(android::renderscript::Context const* src,
											     android::renderscript::ObjectBase** dst){
	_ZN7android12renderscript14rsrClearObjectEPKNS0_7ContextEPv(src, (void*)dst);
}

extern "C" bool _ZN7android12renderscript11rsrIsObjectEPKNS0_7ContextEPKNS0_10ObjectBaseE(android::renderscript::Context const*, android::renderscript::ObjectBase const * src){
	android::renderscript::ObjectBase **osrc = (android::renderscript::ObjectBase **)src;
	return osrc != nullptr;
}
