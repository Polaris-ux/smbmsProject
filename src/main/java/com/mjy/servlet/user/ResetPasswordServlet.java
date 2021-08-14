package com.mjy.servlet.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mjy.entity.Role;
import com.mjy.entity.User;
import com.mjy.service.UserService;
import com.mjy.service.impl.UserServiceImpl;
import com.mjy.util.Constants;
import com.mjy.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @description
 * @create 2021-07-31 21:25
 */
public class ResetPasswordServlet extends HttpServlet {
    private UserService userService;

    public ResetPasswordServlet(){
        userService=new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("savepwd".equals(method)){
            setPassword(req,resp);
        }else if("pwdmodify".equals(method)){
            checkOldPassword(req,resp);
        }else if("query".equals(method)){
            queryInfo(req,resp);
        }else if("view".equals(method)){
            viewInfo(req,resp);
        }else if("modify".equals(method)){
            updateTemp(req,resp);
        }else if("modifyexe".equals(method)){
            updateUserInfo(req,resp);
        }else if("deluser".equals(method)){
            deleteUser(req,resp);
        }else if("add".equals(method)){
            addUser(req,resp);
        }else if("ucexist".equals(method)){
            userCodeIsExists(req,resp);
        }else if("addtemp".equals(method)){
            addTemp(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    public void setPassword(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user=(User)session.getAttribute(Constants.USER_SESSION);
        String userCode = user.getUserCode();
        String rnewpassword = req.getParameter("rnewpassword");
        boolean flag=false;
        if(userCode!=null&& !StringUtils.isNullOrEmpty(rnewpassword)){
            flag=userService.resetPassword(userCode, rnewpassword);
        }

        if(flag){
            req.setAttribute("message","修改密码成功，请退出，使用新密码登录");
            //清除Session
            session.removeAttribute(Constants.USER_SESSION);
            req.getRequestDispatcher("/login.jsp").forward(req,resp);
        }else{
            req.setAttribute("message","修改密码失败，请重试");
        }
    }

    //验证旧密码
    public void checkOldPassword(HttpServletRequest req,HttpServletResponse resp){
        HttpSession session = req.getSession();
        User user=(User)session.getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        Map<String,String> map=new HashMap<>();
        if(user==null){//Session失效
            map.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            map.put("result","error");
        }else{

            if(oldpassword.equals(user.getUserPassword())){
                map.put("result","true");
            }else{
                map.put("result","false");
            }
        }

        try {
            //设置返回的格式
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            //将数据封装为json字符串
            JSONArray objects = new JSONArray();
            String s = objects.toJSONString(map);
            writer.write(s);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //展示用户信息
    public void queryInfo(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {

        //获取前端参数
        String queryName = req.getParameter("queryname");
        String tempRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");

        //所需参数
        int roleId=0;
        int currentPage=1;
        //方便维护
        int pageSize=Constants.PAGE_SIZE;
        //获取用户角色id
        if(!StringUtils.isNullOrEmpty(tempRole)){
            roleId=Integer.parseInt(tempRole);
        }

        if(!StringUtils.isNullOrEmpty(pageIndex)){
            currentPage=Integer.parseInt(pageIndex);
        }

        //总记录数
        int totalCount = userService.getUserNum(queryName, roleId);

        //维护一个分页类
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setCurrentPage(currentPage);
        pageSupport.setTotalCount(totalCount);

        //控制首页和尾页
        int totalPageCount = pageSupport.getTotalPageCount();
        if(currentPage<1){
            currentPage=1;
        }
        if(currentPage>totalPageCount){
            currentPage=totalPageCount;
        }

        List<Role> roleList = userService.getRoles();
        List<User> userList = userService.getUsers(queryName, roleId, currentPage, pageSize);

        //传递信息
        req.setAttribute("roleList",roleList);
        req.setAttribute("userList",userList);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPage);

        //
        req.getRequestDispatcher("/jsp/userlist.jsp").forward(req,resp);
    }

    //回显（浏览）用户数据
    public void viewInfo(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        //获取userID
        String uid = req.getParameter("uid");
        
        int userId=-1;
        if(!StringUtils.isNullOrEmpty(uid)){
            userId=Integer.parseInt(uid);
        }

        User user = userService.getUserById(userId);
        if(user!=null){
            req.setAttribute("user", user);
        }


        req.getRequestDispatcher("/jsp/userview.jsp").forward(req,resp);

    }

    //修改用户信息
    public void updateTemp(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        //获取userID
        String uid = req.getParameter("uid");

        int userId=-1;
        if(!StringUtils.isNullOrEmpty(uid)){
            userId=Integer.parseInt(uid);
        }

        User user = userService.getUserById(userId);
        if(user!=null){
            req.setAttribute("user", user);
        }
        List<Role> roles = userService.getRoles();
        if(roles!=null){
            req.setAttribute("roleList", roles);
        }
        req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req,resp);
    }

    public void updateUserInfo(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        User user=new User();

        //获取参数
        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        //
        try {
            user.setId(Integer.parseInt(uid));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(!StringUtils.isNullOrEmpty(birthday)){
                Date parse = sdf.parse(birthday);
                user.setBirthday(parse);
            }
            if(!StringUtils.isNullOrEmpty(userName)){
                user.setUserName(userName);
            }

            if(!StringUtils.isNullOrEmpty(gender)){
                user.setGender(Integer.parseInt(gender));
            }
            if(!StringUtils.isNullOrEmpty(phone)){
                user.setPhone(phone);
            }

            if(!StringUtils.isNullOrEmpty(address)){
                user.setAddress(address);
            }

            if(!StringUtils.isNullOrEmpty(userRole)){
                user.setUserRole(Integer.parseInt(userRole));
            }
            userService.updateUser(user);

            //跳转到页面
            req.getRequestDispatcher("/jsp/user.do?method=query").forward(req,resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void deleteUser(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        //获取要删除的用户id
        String uid = req.getParameter("uid");

        Map<String,String> map=new HashMap<>();

        boolean flag=false;
        if(uid!=null){

            User userById = userService.getUserById(Integer.parseInt(uid));
            if(userById!=null){
                flag=userService.deleteUser(Integer.parseInt(uid));
            }else{
                map.put("delResult","notexist");
            }
        }
        if(flag&&map.isEmpty()){
            map.put("delResult","true");
        }else if(!flag&&map.isEmpty()){
            map.put("delResult","false");
        }
        JSONArray objects = new JSONArray();
        String s = objects.toJSONString(map);

        PrintWriter writer = resp.getWriter();
        writer.write(s);
        writer.flush();
        writer.close();

        resp.sendRedirect("/smbms/jsp/user.do?method=query&queryname=&queryUserRole=0&pageIndex=1");
    }

    //addTemp
    public void addUser(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        //获取参数
        Map<String,Object> map=new HashMap<>();
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        Date parse=null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            parse= simpleDateFormat.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        map.put("userCode",userCode);
        map.put("userName",userName);
        map.put("userPassword",userPassword);
        map.put("gender",Integer.parseInt(gender));
        map.put("birthday",parse);
        map.put("phone",phone);
        map.put("address",address);
        map.put("userRole",Integer.parseInt(userRole));

        boolean flag = userService.addUser(map);

        if(flag){
            req.getRequestDispatcher("/jsp/user.do?method=query&queryname=&queryUserRole=0&pageIndex=1").forward(req,resp);
        }else{
            req.getRequestDispatcher("/jsp/useradd.jsp").forward(req,resp);
        }
    }

    //判断添加的用户编号是否存在
    public void userCodeIsExists(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        //获取userCode
        String userCode = req.getParameter("userCode");
        Map<String,String> map=new HashMap<>();
        if(!StringUtils.isNullOrEmpty(userCode)){
            User user = userService.getUserByCode(userCode);
            if(user!=null){
                map.put("userCode","exist");
            }else{
                map.put("userCode","notExist");
            }
        }else{
            map.put("userCode","notExist");
        }

        List<Role> roles = userService.getRoles();
        req.setAttribute("roleList",roles);
        try {
            JSONArray objects = new JSONArray();
            String s = objects.toJSONString(map);
            PrintWriter writer = resp.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/jsp/useradd.jsp").forward(req, resp);
    }

    //跳转到useradd.jsp页面
    public void addTemp(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {

        //获取角色列表
        List<Role> roles = userService.getRoles();
        req.setAttribute("roleList",roles);

        req.getRequestDispatcher("/jsp/useradd.jsp").forward(req,resp);

    }

}
