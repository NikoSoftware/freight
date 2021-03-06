package net.xiaomotou.freight.priceVersion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author niko
 * @since 2019-03-04
 */
public class PriceVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本号
     */
    @TableId(value = "versionId", type = IdType.AUTO)
    private Integer versionId;

    /**
     * 本期加入数量
     */
    private Integer num;

    /**
     * 活动体积集合
     */
    private String volumes;

    /**
     * 活动重量集合
     */
    private String weights;

    private LocalDateTime createTime;

    @TableField("curVolume")
    private Double curVolume;

    @TableField("curWeight")
    private Double curWeight;

    @TableField("curPrice")
    private Double curPrice;

    @TableField("toVolume")
    private Double toVolume;

    @TableField("toWeight")
    private Double toWeight;

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    public String getVolumes() {
        return volumes;
    }

    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }
    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public Double getCurVolume() {
        return curVolume;
    }

    public void setCurVolume(Double curVolume) {
        this.curVolume = curVolume;
    }
    public Double getCurWeight() {
        return curWeight;
    }

    public void setCurWeight(Double curWeight) {
        this.curWeight = curWeight;
    }
    public Double getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(Double curPrice) {
        this.curPrice = curPrice;
    }
    public Double getToVolume() {
        return toVolume;
    }

    public void setToVolume(Double toVolume) {
        this.toVolume = toVolume;
    }
    public Double getToWeight() {
        return toWeight;
    }

    public void setToWeight(Double toWeight) {
        this.toWeight = toWeight;
    }

    @Override
    public String toString() {
        return "PriceVersion{" +
        "versionId=" + versionId +
        ", num=" + num +
        ", volumes=" + volumes +
        ", weights=" + weights +
        ", createTime=" + createTime +
        ", curVolume=" + curVolume +
        ", curWeight=" + curWeight +
        ", curPrice=" + curPrice +
        ", toVolume=" + toVolume +
        ", toWeight=" + toWeight +
        "}";
    }
}
