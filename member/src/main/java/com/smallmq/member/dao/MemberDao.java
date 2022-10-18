package com.smallmq.member.dao;

import com.smallmq.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 *
 * @author smallMQ
 * @email 2271443486@qq.com
 * @date 2022-10-18 20:47:14
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

}
