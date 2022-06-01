package com.yang.web.controller;


import com.yang.pojo.Dish;
import com.yang.pojo.SetMeal;
import com.yang.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class Common {

    @Value("${reggie.imagePath}")
    private String imagePath;

    @Value("${file.separator}")
    private String separator;

    /**
     * 文件上传
     *
     * @param file 要上传的文件
     * @return 服务器中保存的文件名
     */
    @PostMapping("/upload")
    public R<String> uploadImage(MultipartFile file) {
        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        //拿到原始文件名称的后缀
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //通过UUID创建新的文件名称
        String newFileName = UUID.randomUUID() + suffix;
        //通过流将文件保存到指定目录
        File filePath = new File(imagePath + LocalDate.now());
        //保证当前文件夹存在
        if (!filePath.exists()) {
            boolean flag = filePath.mkdirs();
            System.out.println(flag);
        }
        //将文件写入指定目录
        try {
            file.transferTo(new File(filePath + separator + newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(newFileName);
    }

    /**
     * 文件下载
     *
     * @param name     要下载的文件名称
     * @param response 用来获取输出流进行文件的下载
     */
    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response) {
        log.info("{}", name);
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //创建流
            String[] paths = name.split("/");
            //图片名/日:期 时间
            if (name.contains(":")) {
                inputStream = new FileInputStream(imagePath + paths[1].split(" ")[0] + separator + paths[0]);
            } else {
                inputStream = new FileInputStream(imagePath + LocalDate.now() + separator + paths[0]);
            }
            outputStream = response.getOutputStream();
            fileCopy(inputStream, outputStream);

            response.setContentType("image/jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fileCopy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int len;
        byte[] bytes = new byte[1024];
        while ((len = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
    }

    /**
     * 将传过来的菜品对象的原始图片进行删除，创建新的图片
     *
     * @param obj 要修改的菜品
     */
    public void updateImage(Object obj) {
        LocalDateTime updateTime;
        String imageName;
        if (obj instanceof Dish) {
            Dish dish = (Dish) obj;
            updateTime = dish.getUpdateTime();
            imageName = dish.getImage();
        }else{
            SetMeal setMeal = (SetMeal) obj;
            updateTime = setMeal.getUpdateTime();
            imageName = setMeal.getImage();
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String oldPathName = imagePath + updateTime.toString().split("T")[0] + separator + imageName;
        File newDir = new File(imagePath + LocalDate.now());
        //如果两个目录为同一个则不用复制
        if (oldPathName.contains(newDir.getName())) {
            return;
        }
        //如果原来文件夹不存在就新创建一个
        if (!newDir.exists()) {
            boolean dirs = newDir.mkdirs();
            log.info("创建文件夹{}{}", newDir, dirs ? "成功" : "失败");
        }
        String newPathName = imagePath + LocalDate.now() + separator + imageName;

        File originalImage = new File(oldPathName);
        File newImage = new File(newPathName);

        //进行文件拷贝
        try {
            bis = new BufferedInputStream(new FileInputStream(originalImage));
            bos = new BufferedOutputStream(new FileOutputStream(newImage));

            fileCopy(bis, bos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
