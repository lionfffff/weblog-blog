package com.yhh.weblogjwt.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yhh.weblogcommon.domain.dos.UserDO;
import com.yhh.weblogcommon.domain.dos.UserRoleDo;
import com.yhh.weblogcommon.domain.mapper.UserMapper;
import com.yhh.weblogcommon.domain.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author Eason
 * @Date 2025-04-02 11:25
 * @PackageName:com.yhh.weblogjwt.service
 * @ClassName: UserDetailServiceImpl
 * @Description: TODO
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查询
        UserDO userDO = userMapper.findByUsername(username);

        // 判断用户是否存在
        if (Objects.isNull(userDO)) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        // 用户角色
        List<UserRoleDo> roleDOS = userRoleMapper.selectByUsername(username);

//        // 转数组
//        if (!CollectionUtils.isEmpty(roleDOS)) {
//            List<String> roles = roleDOS.stream().map(p -> p.getRole()).collect(Collectors.toList());
//            roles.toArray(new String[roles.size()]);
//        }
//        // authorities 用于指定角色，这里写死为 ADMIN 管理员
//        return User.withUsername(userDO.getUsername())
//                .password(userDO.getPassword())
//                .authorities("ROLE_ADMIN")
//                .build();

        //将角色转换为GrantedAuthority集合
        List<GrantedAuthority> authorities = CollectionUtils.isEmpty(roleDOS)
                ? Collections.emptyList()
                : roleDOS.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        // 构建UserDetails对象
        return User.withUsername(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities(authorities) // 使用动态获取的角色
                .build();

    }
}
