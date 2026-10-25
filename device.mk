#
# SPDX-FileCopyrightText: The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

## Bluetooth
PRODUCT_PACKAGES += \
    X96X9BluetoothOverlay \
    libbt-vendor

$(call soong_config_set,brcm_libbt,bdroid_buildcfg_include_dir,$(LOCAL_PATH)/bluetooth/include)
$(call soong_config_set,brcm_libbt,custom_bt_config,//$(LOCAL_PATH):vnd_x96x9.txt)

## Factory
PRODUCT_HOST_PACKAGES += \
    aml_image_packer

## Init
PRODUCT_PACKAGES += \
    init.amlogic.wifi_buildin.rc

## OpenVFD
PRODUCT_SOURCE_ROOT_DIRS += kernel/platform/kernel-5.15/vendor/amlogic/openvfd

PRODUCT_PACKAGES += \
    init.openvfd.rc \
    OpenVFDService \
    OpenVfdSettings \
    TvSettingsOpenVfdOverlay

## Keylayout (IR)
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/keylayout/Vendor_0001_Product_0001.kl:$(TARGET_COPY_OUT_VENDOR)/usr/keylayout/Vendor_0001_Product_0001.kl

## TEE
TARGET_HAS_TEE := false

## Soong Namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH) \
    hardware/broadcom/libbt

## Inherit from the common tree product makefile
$(call inherit-product, device/amlogic/g12-common/g12.mk)

## Inherit from the proprietary files makefile
$(call inherit-product, vendor/amediatech/x96x9/x96x9-vendor.mk)
