package net.xiaomotou.freight.priceVersion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import net.xiaomotou.freight.model.ImageModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p>
 * 
 * </p>
 *
 * @author niko
 * @since 2019-03-04
 */
public class PriceVersionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本号
     */
    private Integer versionId;

    /**
     * 本期加入数量
     */
    private Integer num;

    /**
     * 活动体积集合
     */
    private ArrayList<KeyValue> volumes;


    /**
     * 活动重量集合
     */
    private ArrayList<KeyValue> weights;

    private LocalDateTime createTime;

    private Double curVolume;

    private Double curWeight;

    private Double curPrice;

    private Double toVolume;

    private Double toWeight;


    private ArrayList<ImageModel> imageList;

    public ArrayList<ImageModel> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ImageModel> imageList) {
        this.imageList = imageList;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }
    public Integer getNum() {
        return num;
    }

    public ArrayList<KeyValue> getVolumes() {
        return volumes;
    }

    public void setVolumes(ArrayList<KeyValue> volumes) {
        this.volumes = volumes;
    }

    public ArrayList<KeyValue> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<KeyValue> weights) {
        this.weights = weights;
    }

    public void setNum(Integer num) {
        this.num = num;
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

}
