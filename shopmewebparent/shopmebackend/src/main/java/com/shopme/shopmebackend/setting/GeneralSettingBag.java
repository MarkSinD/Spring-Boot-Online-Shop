package com.shopme.shopmebackend.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class GeneralSettingBag extends SettingBag {
    public GeneralSettingBag(List<Setting> listSetting) {
        super(listSetting);
    }

    public void updateCurrentcySymbol(String value){
        super.update("CURRENCY_SYMBOL", value);
    }

    public void updateSiteLogo(String value){
        super.update("SITE_LOGO", value);
    }
}
