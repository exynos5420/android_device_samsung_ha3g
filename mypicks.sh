# I will list here the open commits I pick to build
# From the LineageOS gerrit:
repopick 164243 -g https://review.lineageos.org

# From the exynos5420 gerrit
repopick 2360 2361 2643 2678 2692 -g https://review.exynos5420.com

# For Substratum Support
repopick -t OMS7 -g https://review.lineageos.org

# From my github
cd system/core
git fetch https://github.com/tincho5588/android_system_core && git cherry-pick 71487616edf3f78f317ce013a159eede6d2f072f
croot
cd frameworks/av
git fetch https://github.com/tincho5588/android_frameworks_av && git cherry-pick c7b461fba377b95903c9d3f76be0f5011766ba8c
croot
cd packages/apps/Dialer
git fetch https://github.com/tincho5588/android_packages_apps_Dialer.git && git cherry-pick a16575f9fd5bbd09d933bb36d33618880471e861
croot