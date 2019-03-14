package net.xiaomotou.freight.poOrder.mapper;

import net.xiaomotou.freight.poOrder.entity.PoOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xiaomotou.freight.poOrder.entity.PoOrderAndUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author niko
 * @since 2019-03-13
 */
public interface PoOrderMapper extends BaseMapper<PoOrder> {

    @Select("select po_order.*, po_user.* from po_order,po_user where po_order.version = #{version} and po_order.openId = po_user.openId ")
    List<PoOrderAndUser> getPoOrderAndUser(@Param("version")Integer version);

    @Select("update po_order set status = #{status} where id = #{id}")
    void updateStatus(@Param("id")Integer id,@Param("status")String status);

}
