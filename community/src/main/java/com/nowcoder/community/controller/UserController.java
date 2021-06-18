package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired//登录才行
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) throws IOException {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();//OringinalFilename原始文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));//后缀名
        if (StringUtils.isBlank(suffix)) {//只要有后缀的
            model.addAttribute("error", "文件的格式不正确");
            return "/site/setting";
        }
        // 生成随机的文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);//本地路径
        try {
            headerImage.transferTo(dest);//把headerImage的内容写入到dest中去
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }

        //更新当前用户的头像的路径（web访问路径）
        //http://localhost:8080/community/user/header/xxx/png
        User user = hostHolder.getUser();//获取当前用户
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        //服务器存放的路径
        fileName = uploadPath + "/" + fileName;
        //文件的后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();)//try()声明的变量编译时会自动关闭，前提是有close方法
        {
            byte[] buffer = new byte[1024];//缓冲区
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {//一次read 1024 字节
                os.write(buffer, 0, b);//一次写入read到的字节数
            }
        } catch (IOException e) {
            logger.error("读取头像失败： " + e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.POST)
    public String updatePassword(Model model, String password, String newPassword, String confirmPassword) {
        if (StringUtils.isBlank(password)) {
            model.addAttribute("passwordMsg", "请输入原始密码！");
            return "/site/setting";
        }
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordMsg", "请输入新密码！");
            return "/site/setting";
        }
        if (StringUtils.isBlank(confirmPassword)) {
            model.addAttribute("confirmPasswordMsg", "请再次输入新密码！");
            return "/site/setting";
        }
        if (!confirmPassword.equals(newPassword)) {
            model.addAttribute("newPasswordMsg", "两次输入的新密码不相同！");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(password, newPassword, user.getId());
        if (map == null || map.isEmpty()) {
            //传给templates注册成功信息
            model.addAttribute("msg", "密码修改成功");
            //跳到回个人设置页面
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            //失败了传失败信息，跳到到原来的页面
            model.addAttribute("passwordMsg", "输入的原始密码错误！");
            return "redirect:/index";
        }
    }
}



