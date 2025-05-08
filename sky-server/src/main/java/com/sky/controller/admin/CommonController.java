package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import com.sky.utils.LocalFileStorageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequestMapping("/admin/common")
@RestController
@Api(tags = "通用接口")
@Slf4j
public class CommonController {


/*    //OSS
    @Autowired
    private AliOssUtil ossUtil;*/

    //本地
    @Autowired
    private LocalFileStorageUtil localFileStorageUtil;


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file)  {
        log.info("file {}", file);
        try {
            String originalFilename = file.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + substring;
            //String path = ossUtil.upload(file.getBytes(), objectName);   //OSS
            String path = localFileStorageUtil.upload(file.getBytes(), objectName);    //本地
            log.info("path {}", path);
            return Result.success(path);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
