package net.xiaomotou.freight.priceVersion.entity;

import java.text.DecimalFormat;

public class KeyValue{
    private Double key;
    private Double value;
    private Double rate;
    DecimalFormat df = new DecimalFormat("#.00");
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {

        this.rate = Double.parseDouble(df.format(rate));
    }

    public Double getKey() {
        return key;
    }

    public void setKey(Double key) {
        this.key = key;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


}
