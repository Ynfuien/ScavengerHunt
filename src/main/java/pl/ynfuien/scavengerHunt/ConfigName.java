package pl.ynfuien.scavengerHunt;

import pl.ynfuien.ydevlib.config.ConfigObject;

public enum ConfigName implements ConfigObject.Name {
    LANG,
    CONFIG;

    @Override
    public String getFileName() {
        return name().toLowerCase().replace('_', '-') + ".yml";
    }
}
