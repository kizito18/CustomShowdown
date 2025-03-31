package com.zitos.web.binkes.models

import com.varabyte.kobweb.compose.ui.graphics.Colors.LightGray
import com.varabyte.kobweb.compose.ui.graphics.Colors.White
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.rgba


enum class ThemeByKizito(
    val hex: String,
    val rgb: CSSColorValue
) {
   // Primary (hex = "#FFE7E1", rgb = rgb(r = 255, g = 231, b = 225)),
    //Secondary (hex = "#F7F7F7", rgb = rgb(r = 247, g = 247, b = 247)),

    Primary (hex = "#F4F5F7", rgb = rgb(r = 244, g = 245, b = 247)),
    Secondary (hex = "#FFFFFF", rgb = rgb(r = 255, g = 255, b = 255)),

    LightGrayBlue (hex = "#F8F9FA",rgb = rgb(r = 248, g = 249, b = 250)),

    BabyBlue (hex = "#2C71F4",rgb = rgb(r = 44, g = 113, b = 244)),

    PersianOrange (hex = "#D47C54", rgb =  rgb(r = 212, g = 124, b = 84)),
  //  LightGrayShimmer3 (hex = "#D3D3D3", rgba(r = 211, g = 211, b = 211, a = 204)),

    Black (hex = "#000000", rgb = rgb(r = 0, g = 0, b = 0)),




    Gray(hex = "#CFCFCF", rgb = rgb(r = 207, g = 207, b = 207)),
    LightGray(hex = "#EDEDED", rgb = rgb(r = 237, g = 237, b = 237)),
    LighterGray(hex = "#F9F9F9", rgb = rgb(r = 249, g = 249, b = 249)),
    White(hex = "#FFFFFF",rgb = rgb(r = 255, g = 255, b = 255))
}