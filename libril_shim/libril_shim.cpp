#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <utils/Log.h>

#include <telephony/ril.h>

#define LOG_TAG "RIL_SHIM_LIBS"

const RIL_RadioFunctions *RIL_SAP_Init(const struct RIL_Env *env, int argc, char **argv) {
	RLOGD("Need to implentment RIL_SAP_Init\n");
	return RIL_SAP_Init(env, argc, argv);
}
