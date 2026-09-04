#!/usr/bin/env -S PYTHONPATH=../../../tools/extract-utils python3
#
# SPDX-FileCopyrightText: The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

import re

from extract_utils.main import (
    ExtractUtils,
    ExtractUtilsModule,
)

namespace_imports = [
    'vendor/amlogic/g12-common',
]

module = ExtractUtilsModule(
    'x96x9',
    'amediatech',
    add_firmware_proprietary_file=True,
    namespace_imports=namespace_imports,
)

if __name__ == '__main__':
    utils = ExtractUtils.device_with_common(module, '../amlogic/g12-common', module.vendor)
    utils.run()

    path = f'../../../vendor/{module.vendor}/{module.device}/Android.mk'
    with open(path) as f:
        content = f.read()
    content = re.sub(
        r'ifeq \(\$\(TARGET_DEVICE\),x96x9\)',
        'ifneq ($(filter x96x9 x96x9_car x96x9_tab,$(TARGET_DEVICE)),)',
        content,
    )
    with open(path, 'w') as f:
        f.write(content)
