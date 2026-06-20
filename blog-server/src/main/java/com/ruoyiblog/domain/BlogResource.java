package com.ruoyiblog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyiblog.common.core.domain.BaseEntity;

/**
 * 资源上传记录对象 blog_resource
 *
 * @author ruoyi
 * @date 2026-06-20
 */
public class BlogResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源ID */
    private Long resourceId;

    /** 原始文件名 */
    private String fileName;

    /** 存储路径 */
    private String filePath;

    /** 文件大小（字节） */
    private Long fileSize;

    /** 文件类型（image/file/video） */
    private String fileType;

    /** MIME类型 */
    private String mimeType;

    /** 文件扩展名 */
    private String extension;

    /** 图片宽度 */
    private Long width;

    /** 图片高度 */
    private Long height;

    /** 存储方式（0本地 1阿里云OSS 2七牛云） */
    private String storageType;

    public void setResourceId(Long resourceId)
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId()
    {
        return resourceId;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public String getExtension()
    {
        return extension;
    }

    public void setWidth(Long width)
    {
        this.width = width;
    }

    public Long getWidth()
    {
        return width;
    }

    public void setHeight(Long height)
    {
        this.height = height;
    }

    public Long getHeight()
    {
        return height;
    }

    public void setStorageType(String storageType)
    {
        this.storageType = storageType;
    }

    public String getStorageType()
    {
        return storageType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("resourceId", getResourceId())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("fileSize", getFileSize())
            .append("fileType", getFileType())
            .append("mimeType", getMimeType())
            .append("extension", getExtension())
            .append("width", getWidth())
            .append("height", getHeight())
            .append("storageType", getStorageType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
