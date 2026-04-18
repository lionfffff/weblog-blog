package com.yhh.weblogcommon.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhh.weblogcommon.domain.dos.UserRoleDo;

import java.util.List;

/**
 * @Author Eason
 * @Date 2025-04-02 18:48
 * @PackageName:com.yhh.weblogcommon.domain.mapper
 * @ClassName: UserRoleMapper
 * @Description: TODO
 */
public interface UserRoleMapper extends BaseMapper<UserRoleDo> {
    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    default List<UserRoleDo> selectByUsername(String username) {
        LambdaQueryWrapper<UserRoleDo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleDo::getUsername, username);

        return selectList(wrapper);
    }
}
