package com.yang;

import org.junit.jupiter.api.Test;

import java.io.*;

public class testDate {

    @Test
    public void test01() {
        File file = new File("test.txt");
        boolean rs = file.mkdirs();
        System.out.println(rs);
    }

    @Test
    void fileCopy() throws Exception {
        File original = new File("D:\\IdeaProjects\\yangyang\\reggie\\src\\main\\resources\\image\\2022-05-06\\3fabb83a-1c09-4fd9-892b-4ef7457daafa.jpeg");
        File newFile = new File("D:\\IdeaProjects\\yangyang\\reggie\\src\\main\\resources\\image\\2022-05-07" + "/" + "a.jpg");
        fileCopy(new FileInputStream(original), new FileOutputStream(newFile));
    }

    private void fileCopy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int len;
        byte[] bytes = new byte[1024];
        while ((len = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
    }

}
