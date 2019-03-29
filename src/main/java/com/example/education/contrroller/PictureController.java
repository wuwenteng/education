package com.example.education.contrroller;

import com.example.education.service.UserService;
import com.example.education.user.Result;
import com.example.education.user.User;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author ASUS
 * @date 2018/10/16
 */
@Controller
public class PictureController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    UserService userService;

    @PostMapping("/profiles")
    public Result setUserPicture(@RequestParam(required = true)MultipartFile profile) {

        Result result = new Result();
        String upPath = request.getSession().getServletContext().getRealPath("/static/photo");

        String username = request.getSession().getAttribute("username").toString();
        if (!profile.isEmpty()) {
            File imgFile = findFiles(upPath, username);
            if (imgFile != null) {
                // 已有头像
            } else {
                // 没有头像
            }
        } else {
            System.out.println("没有文件");
            result.setCode("9");
            result.setMessage("文件为空");
        }

        return result;
    }

    @RequestMapping("updateheadimg")
    public Result updateMyHeadImg(String imgdatabase,String username){
        Result result = new Result();
        if(imgdatabase == null){
            System.out.println("输入为空。");
        }
        int a = imgdatabase.indexOf("base64");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path=request.getSession().getServletContext().getRealPath("/static/phone");
        imgdatabase = imgdatabase.substring(a+7);
        BASE64Decoder decoder=new BASE64Decoder();
        byte[] imgByte;
        try {
            imgByte = decoder.decodeBuffer(imgdatabase);
//            SAXReader reader=new SAXReader();
//            Document doc=reader.read(path+"\\WEB-INF\\classes\\headId.xml");
//            Element root=doc.getRootElement();
//            int headImgId=Integer.parseInt(root.getTextTrim());
            OutputStream os=new FileOutputStream(new File(path + username + ".png"));
//            root.setText(""+(headImgId+1));

            os.write(imgByte,0, imgByte.length);
            // 头像写入数据库
            User user = userService.select(username).get(0);
            System.out.println(os.toString());
            user.setImagePath(os.toString());
            userService.updatePhoto(user);
            os.close();
            // result.addObject("imgsrc", headImgId+".png");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result.setCode("20");
            result.setMessage("保存失败");
            return result;
        }
        result.setCode("200");
        return result;
    }

    /**
     * 递归查找文件
     * @param baseDirName  查找的文件夹路径
     * @param targetFileName  需要查找的文件名
     * files 查找到的文件集合
     */
    @Nullable
    private static File findFiles(String baseDirName, String targetFileName) {
        // 创建一个File对象
        File baseDir = new File(baseDirName);
        // 判断目录是否存在
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");
        }
        String tempName = null;

        File tempFile;
        File[] files = baseDir.listFiles();
        // 该文件夹下没有文件，为空文件夹
        if(files.length==0){
            System.out.println("为空文件夹");
            return null;
        }
        for (File file : files) {
            tempFile = file;
            String fileName = tempFile.getName();
            // 文件名，不带后缀
            tempName = fileName.substring(0, fileName.lastIndexOf("."));
            if (tempName.equals(targetFileName)) {
                System.out.println(tempFile.getAbsoluteFile().toString());
                return tempFile.getAbsoluteFile();

            }
        }
        return null;
    }


    /**
     * @param file 已有文件，或想要命名的文件加路径
     * @param toFile 想改的名字加路径
     */
    private void renameFile(String file, String toFile) {

        File toBeRenamed = new File(file);
        // 检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

            System.out.println("File does not exist: " + file);
            return;
        }

        File newFile = new File(toFile);

        //修改文件名
        if (toBeRenamed.renameTo(newFile)) {
            System.out.println("File has been renamed.");
        } else {
            System.out.println("Error renaming file");
        }
    }
}
