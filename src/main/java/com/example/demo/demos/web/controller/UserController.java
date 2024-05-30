package com.example.demo.demos.web.controller;

import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
public class UserController{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // http://127.0.0.1:8080/save_drawUser?userId=1&userName=zhangsan&department=develop
    @RequestMapping("/save_drawUser")
    @ResponseBody
    public String save_drawUser(User u) {
//        System.out.println(u.toString());
        int update = jdbcTemplate.update("insert into draw.user values(?,?,?);",u.getUserId(),u.getUserName(),u.getDepartment());
        return update > 0 ? "success" : "error";
    }

    // http://127.0.0.1:8080/queryDrawUser?userId=1
    @RequestMapping("/queryDrawUser")
    @ResponseBody
    public List<User> queryDrawUser(String userId) {
        String sql;
        if (userId == "") {
            sql = "select * from draw.user";
            List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setUserId(rs.getString("userId"));
                    user.setUserName(rs.getString("userName"));
                    user.setDepartment(rs.getString("department"));
                    return user;
                }
            });
            return users;
        } else {
            sql = "select * from draw.user where userId = ?";
            List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), userId);
            return users;
        }
    }
    @RequestMapping("/deleteUserByUserId")
    @ResponseBody
    public List<User> deleteUserByUserId(String userId){
        String sql = "delete from draw.user where userId = "+ userId;
        jdbcTemplate.update(sql);
        return this.queryDrawUser(null);
    }
}
