<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="AlertDialog.AppCompat" parent="@style/Base.AlertDialog.AppCompat">
    </style>
    <style name="AlertDialog.AppCompat.Light" parent="@style/Base.AlertDialog.AppCompat.Light">
    </style>
    <style name="AndroidThemeColorAccentYellow">
        <item name="android:colorAccent">#ffffff00</item>
    </style>
    <style name="Animation.AppCompat.Dialog" parent="@style/Base.Animation.AppCompat.Dialog">
    </style>
    <style name="Animation.AppCompat.DropDownUp" parent="@style/Base.Animation.AppCompat.DropDownUp">
    </style>
    <style name="Animation.AppCompat.Tooltip" parent="@style/Base.Animation.AppCompat.Tooltip">
    </style>
    <style name="Animation.Design.BottomSheetDialog" parent="@style/Animation.AppCompat.Dialog">
        <item name="android:windowEnterAnimation">@anim/design_bottom_sheet_slide_in</item>
        <item name="android:windowExitAnimation">@anim/design_bottom_sheet_slide_out</item>
    </style>
    <style name="Animation.MaterialComponents.BottomSheetDialog" parent="@style/Animation.AppCompat.Dialog">
        <item name="android:windowEnterAnimation">@anim/mtrl_bottom_sheet_slide_in</item>
        <item name="android:windowExitAnimation">@anim/mtrl_bottom_sheet_slide_out</item>
    </style>
    <style name="Base.AlertDialog.AppCompat" parent="@android:style/Widget">
        <item name="android:layout">@layout/abc_alert_dialog_material</item>
        <item name="buttonIconDimen">@dimen/abc_alert_dialog_button_dimen</item>
        <item name="listItemLayout">@layout/select_dialog_item_material</item>
        <item name="listLayout">@layout/abc_select_dialog_material</item>
        <item name="multiChoiceItemLayout">@layout/select_dialog_multichoice_material</item>
        <item name="singleChoiceItemLayout">@layout/select_dialog_singlechoice_material</item>
    </style>
    <style name="Base.AlertDialog.AppCompat.Light" parent="@style/Base.AlertDialog.AppCompat">
    </style>
    <style name="Base.Animation.AppCompat.Dialog" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/abc_popup_enter</item>
        <item name="android:windowExitAnimation">@anim/abc_popup_exit</item>
    </style>
    <style name="Base.Animation.AppCompat.DropDownUp" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/abc_grow_fade_in_from_bottom</item>
        <item name="android:windowExitAnimation">@anim/abc_shrink_fade_out_from_bottom</item>
    </style>
    <style name="Base.Animation.AppCompat.Tooltip" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/abc_tooltip_enter</item>
        <i