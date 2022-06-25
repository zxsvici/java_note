package com.zxs.note.ftp;

import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.ChannelSftp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = FtpApplication.class)
@RunWith(SpringRunner.class)
public class SftpTest {

    private Sftp sftp1;
    private Sftp sftp2;
    private static final String fromDir = "/root/test";
    private static final String toDir = "/home/zxs/test";
    private static final String localDir = "F:/test/sftp";
    private List<String> fileNames;
    private List<String> dirNames;

    @Before
    public void before() {
        sftp1 = new Sftp("81.69.248.115",22, "root", "FYxw1223");
        sftp1.init();
        sftp2 = new Sftp("81.69.248.115",22, "zxs", "FYxw1223");
        sftp2.init();
        sftp2.mkDirs(toDir);
        fileNames = new ArrayList<>();
        dirNames = new ArrayList<>();
    }

    @Test
    public void test() {
        ls(fromDir);
        String temp = localDir + System.currentTimeMillis();
        File tempDir = new File(temp);
        dirNames.forEach(dir -> {
            if (!sftp2.exist(dir.replace(fromDir, toDir))) {
                sftp2.mkDirs(dir.replace(fromDir,toDir));
            }
            new File(dir.replace(fromDir, temp)).mkdirs();
        });
        fileNames.forEach(file -> {
//            sftp1.download(file, new File(file.replace(fromDir, localDir)));
            transfer(file, temp);
        });
        tempDir.delete();
    }

    private void ls(String path) {
        List<ChannelSftp.LsEntry> entries = sftp1.lsEntries(path);
        entries.forEach(entry -> {
            String filename = path + "/" + entry.getFilename();
            if(entry.getAttrs().isDir()) {
                dirNames.add(filename);
                ls(filename);
            }else {
                fileNames.add(filename);
            }
        });
    }

    private void transfer(String fileName, String dir) {
        String toName = fileName.replace(fromDir, toDir);
        File file = new File(fileName.replace(fromDir, dir));
        sftp1.download(fileName, file);
        sftp2.upload(toName, file);
    }
}
