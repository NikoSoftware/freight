package net.xiaomotou.freight.poUser.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author niko
 * @since 2019-02-24
 */
public class PoUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开放id
     */
    @TableField("openId")
    private String openId;

    /**
     * 用户名
     */
    @TableField("nickName")
    private String nickName;

    /**
     * 用户头像
     */
    @TableField("avatarUrl")
    private String avatarUrl;

    /**
     * 性别
     */
    private String gender;

    /**
     * 管理员级别
     */
    private Integer administrator;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getAvatarUrl() {
        return  avatarUrl;
    }

    public void setAvatarUrl(String  avatarUrl) {
        this. avatarUrl =  avatarUrl;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public Integer getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Integer administrator) {
        this.administrator = administrator;
    }

    @Override
    public String toString() {
        return "PoUser{" +
        "openId=" + openId +
        ", nickName=" + nickName +
        ", avatarUrl=" + avatarUrl +
        ", gender=" + gender +
        ", administrator=" + administrator +
        "}";
    }
}
