#
# SPDX-FileCopyrightText: The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

DEVICE_PATH := device/amediatech/x96x9

## Bluetooth
BOARD_HAVE_BLUETOOTH := true

## Bootloader
TARGET_BOOTLOADER_BOARD_NAME := x96x9

## DTB
TARGET_DTB_NAME := g12b_a311d_w200_x96x9
TARGET_DTBO_NAME := android_overlay_dt
BOARD_KERNEL_SEPARATED_DTBO := true

## Kernel
TARGET_KERNEL_PLATFORM_TARGET := x96x9
TARGET_KERNEL_SOURCE := vendor/amediatech/x96x9-build
BOARD_VENDOR_KERNEL_MODULES_LOAD := $(strip $(shell cat $(DEVICE_PATH)/vendor_dlkm.modules.load))
BOOT_KERNEL_MODULES := $(strip $(shell cat $(DEVICE_PATH)/vendor_boot.modules.load))
BOARD_VENDOR_RAMDISK_KERNEL_MODULES_LOAD := $(BOOT_KERNEL_MODULES)
RECOVERY_KERNEL_MODULES := $(BOOT_KERNEL_MODULES)
BOARD_RECOVERY_KERNEL_MODULES_LOAD := $(BOOT_KERNEL_MODULES)

## Partitions
BOARD_SUPER_PARTITION_SIZE := 2084569088

## Properties
TARGET_VENDOR_PROP += $(DEVICE_PATH)/vendor.prop

## Wi-Fi
BOARD_HOSTAPD_PRIVATE_LIB := lib_driver_cmd_bcmdhd
BOARD_WLAN_DEVICE := bcmdhd
BOARD_WPA_SUPPLICANT_DRIVER := NL80211
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_bcmdhd
WIFI_DRIVER_FW_PATH_STA := "/wifi/fw_bcm43456c5_ag.bin"
WIFI_DRIVER_FW_PATH_PARAM := "/sys/module/dhd/parameters/firmware_path"
WPA_SUPPLICANT_VERSION := VER_0_8_X

## Include the common tree BoardConfig makefile
include device/amlogic/g12-common/BoardConfigCommon.mk

## Include the proprietary BoardConfig makefile
include vendor/amediatech/x96x9/BoardConfigVendor.mk
