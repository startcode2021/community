package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.dao.mapper.UserMapper;
import com.community.dao.pojo.User;
import com.community.dao.pojo.UserRole;
import com.community.service.UserRoleService;
import com.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService roleService;
    @Autowired
    HttpSession session;

    // 用户登录逻辑和验证处理
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 通过手机号查询用户
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", s));

        // 放入session
        session.setAttribute("loginUser",user);

        //创建一个新的UserDetails对象，最后验证登陆的需要
        UserDetails userDetails=null;
        if(user!=null){
            //System.out.println("未加密："+user.getPassword());
            //String BCryptPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            // 登录后会将登录密码进行加密，然后比对数据库中的密码，数据库密码需要加密存储！
            String password = user.getPassword();

            //创建一个集合来存放权限
            Collection<GrantedAuthority> authorities = getAuthorities(user);
            //实例化UserDetails对象
            userDetails = new org.springframework.security.core.userdetails.User(s,password,
                    true,
                    true,
                    true,
                    true, authorities);
        }
        return userDetails;
    }

    // 获取角色信息
    private Collection<GrantedAuthority> getAuthorities(User user){
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        UserRole role = roleService.getOne(new QueryWrapper<UserRole>().eq("id", user.getRoleid()));
        //注意：这里每个权限前面都要加ROLE_。否在最后验证不会通过
        authList.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        return authList;
    }

}

