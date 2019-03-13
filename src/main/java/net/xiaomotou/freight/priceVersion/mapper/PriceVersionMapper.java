package net.xiaomotou.freight.priceVersion.mapper;

import net.xiaomotou.freight.priceVersion.entity.PriceVersion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author niko
 * @since 2019-03-04
 */
public interface PriceVersionMapper extends BaseMapper<PriceVersion> {

    @Select("select * from price_version order by versionId desc limit 1 "  )
    PriceVersion getMaxPriceVersion();
}
