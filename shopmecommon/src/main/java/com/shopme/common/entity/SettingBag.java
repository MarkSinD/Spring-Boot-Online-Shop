package com.shopme.common.entity;

import java.util.List;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class SettingBag {
    private List<Setting> listSetting;

    public SettingBag(List<Setting> listSetting){
        this.listSetting = listSetting;
    }

    public Setting get(String key){
        int index = listSetting.indexOf(new Setting(key));
        if(index >= 0){
            return listSetting.get(index);
        }
        return null;
    }

    public String getValue(String key){
        Setting setting = get(key);
        if(setting != null){
            return setting.getValue();
        }
        return null;
    }

    public void update(String key, String value){
        Setting setting = get(key);
        if(setting != null && value != null){
            setting.setValue(value);
        }
    }

    public List<Setting> list(){
        return listSetting;
    }
}
