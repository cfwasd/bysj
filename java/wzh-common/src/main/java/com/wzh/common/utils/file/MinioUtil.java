package com.wzh.common.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.web.multipart.MultipartFile;
import com.wzh.common.utils.ServletUtils;
import com.wzh.common.utils.spring.SpringUtils;
import io.minio.*;
import io.minio.http.Method;

/**
 * Minio 文件存储工具类
 * 
 * @author ruoyi
 */
public class MinioUtil
{
    /**
     * 上传文件
     * 
     * @param bucketName 桶名称
     * @param fileName
     * @throws IOException
     */
    public static String uploadFile(String bucketName, String fileName, MultipartFile multipartFile) throws IOException
    {
        String url = "";
        MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
        try (InputStream inputStream = multipartFile.getInputStream())
        {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(inputStream, multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).method(Method.GET).build());
            url = url.substring(0, url.indexOf('?'));
            return ServletUtils.urlDecode(url);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     * 
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @param outputStream 输出流
     */
    public static void downloadFile(String bucketName, String fileName, OutputStream outputStream) throws IOException
    {
        MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()))
        {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = stream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
        }
        catch (Exception e)
        {
            throw new IOException("下载文件失败", e);
        }
    }

    /**
     * 下载文件(不传入输出流版本)
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件字节数组
     */
    /**
     * 下载文件并返回字节数组
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public static InputStream downloadAsStream(String bucketName, String fileName) throws IOException {
        MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
//            return minioClient.getObject(bucketName,fileName);
        } catch (Exception e) {
            throw new IOException("获取文件流失败", e);
        }
    }

    /**
     * 获取文件预览URL
     * 
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @param expiry 过期时间(秒)
     * @return 文件预览URL
     */
    public static String getPreviewUrl(String bucketName, String fileName, int expiry) throws IOException
    {
        MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
        try
        {
            String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(expiry)
                    .build());
            return ServletUtils.urlDecode(url.split("\\?")[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IOException("获取预览链接失败", e);
        }
    }
}
