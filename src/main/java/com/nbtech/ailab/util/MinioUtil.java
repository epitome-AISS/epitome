package com.nbtech.ailab.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * minio工具类
 */

import com.nbtech.ailab.biz.dto.FileInfoDto;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.constant.MaterialTypeConstant;
import com.nbtech.common.exception.BizException;

import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MinioUtil {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${minio.useUrl}")
    private String useUrl;

    public MinioUtil() {

    }

    private int expire = 5 * 60;

    public MinioClient getClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public boolean bucketExists(String bucketName) {
        try {
            return getClient().bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());
        } catch (Exception ex) {
            log.error("bucketExists异常", ex);
            return false;
        }
    }

    public boolean makeBucket(String bucketName) {
        try {
            getClient().makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());
        } catch (Exception ex) {
            log.error("makeBucket异常", ex);
            return false;
        }
        log.debug("makeBucket成功:{}", bucketName);
        return true;
    }

    public boolean mkdir(String bucketName, String dir) {
        try {
            getClient().putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(dir)
                            .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                            .build());
        } catch (Exception ex) {
            log.error("mkdir异常", ex);
            return false;
        }
        return true;
    }

    /**
     * 根据上传的文件 返回文件的上传路径
     *
     * @param file       上传的文件
     * @param folderName 文件夹的名称
     * @return
     */
    public FileInfoDto getFileUrl(MultipartFile file, String folderName) throws Exception {
        FileInfoDto fileInfo = new FileInfoDto();
        // 生成文件名
        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        if (folderName != null) {
            originalFileName = folderName + "/" + originalFileName;
        }
        // 上传文件
        try (InputStream fileStream = file.getInputStream()) {
            putObject(bucketName, originalFileName, fileStream, contentType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取当前素材类型
        String materialType = getFileType(file);
        String finalUrl = useUrl + "/" + bucketName + "/" + originalFileName;
        fileInfo.setMaterialType(materialType);
        fileInfo.setUrl(finalUrl);
        return fileInfo;
    }

    /**
     * 文件的预览链接
     * 
     * @param fileName 文件名称
     * @return
     */
    public String getPreviewUrl(String fileName) throws Exception {
        // 根据minio客户端账户获取文件url
        MinioClient minioClient = getClient();
        // 给文件生成临时路径
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .method(Method.GET)
                .expiry(expire)
                .build();
        return minioClient.getPresignedObjectUrl(args);
    }

    public boolean putObject(String bucketName, String objName, InputStream inputStream, String contentType) {
        try {
            // 检查 bucket 是否存在
            boolean bucketExists = getClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            // 如果 bucket 不存在，创建它
            if (!bucketExists) {
                try {
                    // 创建 bucket 并设置为 public
                    getClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

                    // 设置 bucket 策略为 public
                    String policy = String.format(
                            "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}",
                            bucketName
                    );

                    getClient().setBucketPolicy(
                            SetBucketPolicyArgs.builder()
                                    .bucket(bucketName)
                                    .config(policy)
                                    .build()
                    );

                    log.info("成功创建并设置为public的bucket: {}", bucketName);
                } catch (Exception e) {
                    log.error("创建bucket失败: {}", bucketName, e);
                    return false;
                }
            }

            // 上传文件
            getClient().putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build()
            );

            log.debug("putObject成功,bucketName:{},objName:{}", bucketName, objName);
            return true;

        } catch (Exception ex) {
            log.error("putObject异常,bucketName:{},objName:{}", bucketName, objName, ex);
            return false;
        }
    }

    public Iterable<Result<Item>> listObjects(String bucketName, String prefix) {
        return getClient().listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build());
    }

    public InputStream getObject(String bucketName, String objName) throws Exception {
        return getClient().getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objName)
                        .build());

    }

    public boolean removeObject(String bucketName, String objName) {
        try {
            getClient().removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objName)
                            .build());
        } catch (Exception ex) {
            log.error("removeObject异常", ex);
            return false;
        }
        return true;
    }

    /**
     * 根据返回给前端的url路径删除对应的文件
     *
     * @param urlString
     */
    public void removeUrl(String urlString) {
        try {
            String objName = urlString.replace(endpoint + "/" + bucketName, "");
            getClient().removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objName)
                            .build());
        } catch (Exception ex) {
            log.error("removeObject异常", ex);
        }
    }

    /**
     * 获取到当前文件的类型
     */
    private String getFileType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType.startsWith("image/")) {
            return MaterialTypeConstant.PICTURE;
        } else if (contentType.startsWith("text/")) {
            return MaterialTypeConstant.TEXT;
        } else if (contentType.startsWith("audio/")) {
            return MaterialTypeConstant.AUDIO;
        } else if (contentType.startsWith("video/")) {
            return MaterialTypeConstant.VIDEO;
        } else if (contentType.equals("application/pdf")) {
            return MaterialTypeConstant.PDF;
        } else {
            return MaterialTypeConstant.OTHER;
        }
    }

    /**
     * 上传文件到minio
     * 
     * @param decodedBytes 文件字节数组
     * @param fileKey      文件key
     */
    public void uploadObject(byte[] decodedBytes, String fileKey) {
        try {
            boolean success = putObject(bucketName, fileKey, new ByteArrayInputStream(decodedBytes), "image/jpeg");
            if (!success) {
                throw new BizException(BizResponseCodeEnum.IMAGE_UPLOAD_ERROR, fileKey);
            }
        } catch (Exception e) {
            log.error("minio上传文件失败", e);
            throw new BizException(BizResponseCodeEnum.IMAGE_UPLOAD_ERROR, fileKey);
        }
    }

    /**
     * 获取文件的base64
     * 
     * @param key 文件key
     * @return
     */
    public String getBase64(String key) throws Exception {
        try (InputStream inputStream = getObject(bucketName, key);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(buffer.toByteArray());
        }
    }

}
