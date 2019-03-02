package net.xiaomotou.freight.priceVersion.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author niko
 * @since 2019-02-24
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

    @Override
    public String toString() {
        return "PriceVersion{" +
        "versionId=" + versionId +
        ", num=" + num +
        ", volumes=" + volumes +
        ", weights=" + weights +
        ", createTime=" + createTime +
        "}";
    }
}
