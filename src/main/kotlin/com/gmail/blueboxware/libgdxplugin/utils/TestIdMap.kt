/*
 * Copyright 2016 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gmail.blueboxware.libgdxplugin.utils

fun isTestId(string: String?): String? {
    if (string in TEST_ID_MAP) {
        return TEST_ID_MAP[string]
    }
    for ((prefix, description) in TEST_ID_PREFIXES_MAP) {
        if (string?.startsWith(prefix) == true) {
            return description
        }
    }
    return null
}


internal val TEST_ID_PREFIXES_MAP = mapOf(

    "ca-app-pub-3940256099942544/" to "AdMob test ad ID",
    "ca-app-pub-3940256099942544~" to "AdMob sample app ID"
)

internal val TEST_ID_MAP = mapOf(

    "0ac59b0996d947309c33f59d6676399f" to "MoPub banner unit ID",
    "11a17b188668469fb0412708c3d16813" to "MoPub native unit ID",
    "12345678901234567890123456789012" to "InMobi account ID",
    "1234567890qwerty0987654321qwerty12345" to "InMobi account ID",
    "123456789abcdfghjiukljnm09874" to "InMobi account ID",
    "12346789pqrstuvwxy987654321pqwr" to "InMobi account ID",
    "plid-1431977751489005" to "InMobi placement ID from example code",
    "15173ac6d3e54c9389b9a5ddca69b34b" to "MoPub rewarded rich media unit ID",
    "1a19654b05694a2385448499f03f48df" to "InMobi account ID",
    "23b49916add211e281c11231392559e4" to "MoPub banner unit ID",
    "24534e1901884e398f1253216226017e" to "MoPub interstitial unit ID",
    "252412d5e9364a05ab77d9396346d73d" to "MoPub mrect unit ID",
    "2aae44d2ab91424d9850870af33e5af7" to "MoPub mrect unit ID",
    "3aba0056add211e281c11231392559e4" to "MoPub interstitial unit ID",
    "4028cb8b2c3a0b45012c406824e800ba" to "InMobi account ID",
    "4f117153f5c24fa6a3a92b818a5eb630" to "MoPub interstitial unit ID",
    "33BE2250B43518CCDA7DE426D04EE231" to "Dummy test device ID from example code",
    "4f21c409cd1cb2fb7000001b" to "ChartBoost app ID",
    "4f7b433509b6025804000002" to "ChartBoost app ID",
    "58fe200484fbd5b9670000e3" to "Vungle app ID",
    "5916309cb46f6b5a3e00009c" to "Vungle app ID",
    "76a3fefaced247959582d2d2df6f4757" to "MoPub native unit ID",
    "8f000bd5e00246de9c789eed39ff6096" to "MoPub rewarded video unit ID",
    "920b6145fb1546cf8b5cf2ac34638bb7" to "MoPub rewarded video unit ID",
    "92e2de2fd7070327bdeb54c15a5295309c6fcd2d" to "ChartBoost app signature",
    "98c29e015e7346bd9c380b1467b33850" to "MoPub rewarded rich media unit ID",
    "DEFAULT32590" to "Vungle placement ID",
    "DEFAULT87043" to "Vungle placement ID",
    "E621E1F8-C36C-495A-93FC-0C247A3E6E5F" to "MillennialMedia advertising identifier",
    "PLMT02I05269" to "Vungle placement ID",
    "PLMT03R77999" to "Vungle placement ID",
    "TESTINT07107" to "Vungle placement ID",
    "TESTREW28799" to "Vungle placement ID",
    "a8919cca19784497872ae69d48f678e1" to "MoPub leaderboard unit ID",
    "app185a7e71e1714831a49ec7" to "AdColony app ID",
    "appbdee68ae27024084bb334a" to "AdColony app ID",
    "b195f8dd8ded45fe847ad89ed1d016da" to "MoPub banner unit ID",
    "b2b67c2a8c0944eda272ed8e4ddf7ed4" to "MoPub native video unit ID",
    "d456ea115eec497ab33e02531a5efcbc" to "MoPub leaderboard unit ID",
    "dd2d41b69ac01b80f443f5b6cf06096d457f82bd" to "ChartBoost App signature",
    "df19afdaf27f4fb4a2c2b85e2c10bc6a" to "InMobi account ID",
    "tPdB5-ZZSAu6xC_VxPrC0QEBW5ww3pQYyCbXihbJCEYAxh2VOmrGWxaxWqqe" to "Tapjoy SDK key",
    "vz06e8c32a037749699e7050" to "AdColony zone ID",
    "vzf8fb4670a60e4a139d01b5" to "AdColony zone ID",
    "00000000-0000-0000-0000-000000000000" to "Tapjoy app ID from example code"

)

