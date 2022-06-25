package com.zxs.note.ftp;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = FtpApplication.class)
@RunWith(SpringRunner.class)
public class FtpTest {

    private Ftp ftp;
    private static final String fromDir = "/root/test";
    private static final String toDir = "/home/zxs/test";
    private static final String localDir = "F:/test";
    private Ftp ftp2;
    private List<String> fileNames;
    private List<String> dirNames;

    @Before
    public void before() {
        FtpConfig ftpConfig=new FtpConfig();
        ftpConfig.setCharset(StandardCharsets.UTF_8);
        ftpConfig.setConnectionTimeout(3000);
        ftpConfig.setHost("81.69.248.115");
        ftpConfig.setPort(21);
        ftpConfig.setUser("root");
        ftpConfig.setPassword("FYxw1223");
        ftpConfig.setSoTimeout(6000);
        FtpMode mode= FtpMode.Passive;
        ftp = new Ftp(ftpConfig, mode);
        FtpConfig config=new FtpConfig();
        config.setCharset(StandardCharsets.UTF_8);
        config.setConnectionTimeout(3000);
        config.setHost("81.69.248.115");
        config.setPort(21);
        config.setUser("zxs");
        config.setPassword("FYxw1223");
        config.setSoTimeout(6000);
        FtpMode mod = FtpMode.Passive;
        ftp2 = new Ftp(config, mod);
        ftp.init();
        ftp2.init();
        fileNames = new ArrayList<>();
        dirNames = new ArrayList<>();
    }

    @Test
    public void connect() {
        ls(fromDir);
        dirNames.forEach(dir -> {
            if(!ftp2.exist(dir.replace(fromDir,toDir))) {
                ftp2.mkDirs(dir.replace(fromDir,toDir));
            }
        });
        String dir = localDir + System.currentTimeMillis();
        File local = new File(dir);
        local.mkdirs();
        fileNames.forEach(file -> transfer(file, dir));
        local.delete();
//        dirNames.forEach(item -> new File(item.replace(dir, localDir)).mkdirs());
//        fileNames.forEach(item -> ftp.download(item, new File(item.replace(dir, localDir))));
//        fileNames.forEach(item -> {
//            try {
//                ftp.download(dir, item, new FileOutputStream(item.replace(dir, localDir)));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void ls(String path) {
        FTPFile[] files = ftp.lsFiles(path);
        for (FTPFile file : files) {
            String name = path + "/" + file.getName();
            if (file.isDirectory()) {
                dirNames.add(name);
                ls(name);
            }else {
                fileNames.add(name);
            }
        }
    }

    private void transfer(String fileName, String dir) {
        String toName = fileName.replace(fromDir, toDir);
        File file = new File(fileName.replace(fromDir, dir));
        file.mkdirs();
        ftp.download(fileName, file);
        ftp2.upload(toName, file);
    }
}
