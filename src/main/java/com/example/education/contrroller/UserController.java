package com.example.education.contrroller;

import com.example.education.service.UserService;
import com.example.education.user.Result;
import com.example.education.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ASUS
 * @date 2018/10/23
 */
@Controller
public class UserController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    UserService userService;

    @PostMapping(value = "/uploadPic")
    @ResponseBody
    public Result uploadPic(@RequestParam("picture") MultipartFile picture, Model model) throws IOException {
        Result result = new Result();
        HttpSession session = request.getSession();

        // 裁剪图片用的书数据
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String w = request.getParameter("w");
        String h = request.getParameter("h");

        int intX = Integer.parseInt(x);
        int intY = Integer.parseInt(y);
        int intW = Double.valueOf(w).intValue();
        int intH = Double.valueOf(h).intValue();

        String username = session.getAttribute("username").toString();
        if (username !=null) {
            // 新的文件名 这里使用用户名

            System.out.println(username);
            // 原始文件名
            String originalFilename = picture.getOriginalFilename();
            System.out.println(originalFilename);



            // 获取上传图片的后缀名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            System.out.println(extension);

            // 上传图片的相对路径
            String path = "static\\upload";
            // 图片上传的绝对路径
            String url = request.getSession().getServletContext().getRealPath("/") + path;
            System.out.println(url);
            String url2 = "E:\\idea\\thymeleaf\\src\\main\\resources\\static\\upload";

            File dir = new File(url);
            if(!dir.exists()) {
                // 创建文件夹
                dir.mkdirs();
            }

            String picPath = path + "\\" +username +extension;
            // 上传图片

            InputStream inputStream = picture.getInputStream();
            if (intW != 0 && intH != 0) {
                BufferedImage image = zipPicture(inputStream);
                cut(image,url + "\\" + username +extension,intX,intY,intW,intH);
                // 将相对路径写回
                User user = new User();
                user.setUsername(username);
                user.setImagePath(picPath);
                //将路径保存好，以备使用
                userService.updatePhoto(user);

                result.setUser(user);
                result.setCode("200");

                model.addAttribute("path",picPath);
            } else {
                result.setCode("400");
                result.setMessage("请重新选择图像的上传部分");
            }

        } else {
            result.setCode("401");
            result.setMessage("请先登录");
        }

        return result;
    }

    @PostMapping(value = "/zipPicture")
    @ResponseBody
    public Result zipPic(@RequestParam("picture") MultipartFile picture) {

        Result result = new Result();
        try {
            InputStream inputStream = picture.getInputStream();
            String name = picture.getOriginalFilename();
            BufferedImage image = zipPicture(inputStream);
            String url = request.getSession().getServletContext().getRealPath("/") + "tmp\\upload";

            String path = url + "\\" + name;
            File dir = new File(url);
            if(!dir.exists()) {
                // 创建文件夹
                dir.mkdirs();
            }
            // 输出为文件
            ImageIO.write(image, "JPEG", new File(path));
            result.setCode("200");
            User user = new User();
            user.setImagePath("tmp\\upload\\" + name);
            result.setUser(user);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode("400");
        }
        return result;
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param bi 源图像
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    private void cut(BufferedImage bi, String result,
                     int x, int y, int width, int height) {

        // String srcImageFile
        try {
            // 读取源图像
            // BufferedImage bi = ImageIO.read(inputStream);
            // 源图宽度
            int srcWidth = bi.getHeight();
            System.out.println(srcWidth);
            // 源图高度
            int srcHeight = bi.getWidth();
            System.out.println(srcHeight);

            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                // 绘制切割后的图
                g.drawImage(img, 0, 0, width, height, null);
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 等比例压缩图像 压缩为300X300的正方形图像
     * @param inputStream 图片输入流
     * @return tag 图片
     * @throws IOException 文件为空时会报错
     */
    private BufferedImage zipPicture(InputStream inputStream) throws IOException {
        BufferedImage bi = ImageIO.read(inputStream);
        int origHeight = bi.getHeight();
        int origWidth = bi.getWidth();
        //double scale = 1;
        int comBase = 300;
        int newHeight = 0;
        int newWidth = 0;
        int fillW = 0;
        int fillH = 0;
        if (origHeight > comBase || origWidth > comBase) {
            if (origHeight >= origWidth) {
                newHeight = comBase;
                newWidth = origWidth * newHeight / origHeight;
                fillW = (newHeight - newWidth) / 2;
            } else {
                newWidth = comBase;
                newHeight = origHeight * newWidth / origWidth;
                fillH = (newWidth - newHeight) / 2;
            }
        } else {
            newHeight = origHeight;
            fillH = (comBase - newHeight) / 2;
            newWidth = origWidth;
            fillW = (comBase - newWidth) / 2;
        }
        System.out.println("新的图片的长：" + newHeight + "留白为：" + fillH);
        System.out.println("新的图片的宽：" + newWidth + "留白为：" + fillW);

        BufferedImage tag = new BufferedImage(comBase,comBase,BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.fillRect(0,0,comBase,comBase);
        g.drawImage(bi,fillW,fillH,newWidth,newHeight, Color.white,null);
        g.dispose();
        // tag.getGraphics().drawImage(bi,0, 0, newWidth, newHeight,null);

        return tag;
    }
}
