package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 演职人员对象 person
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public class Person extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** TMDB人物ID */
    private Long id;

    /** 中文名（优先使用） */
    @Excel(name = "中文名", readConverterExp = "优=先使用")
    private String name;

    /** 原名 */
    @Excel(name = "原名")
    private String originalName;

    /** 性别：0未设置，1女，2男 */
    @Excel(name = "性别：0未设置，1女，2男")
    private Integer gender;

    /** 头像相对路径 */
    @Excel(name = "头像相对路径")
    private String profilePath;

    /** 简介（可选） */
    @Excel(name = "简介", readConverterExp = "可=选")
    private String biography;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setOriginalName(String originalName) 
    {
        this.originalName = originalName;
    }

    public String getOriginalName() 
    {
        return originalName;
    }

    public void setGender(Integer gender) 
    {
        this.gender = gender;
    }

    public Integer getGender() 
    {
        return gender;
    }

    public void setProfilePath(String profilePath) 
    {
        this.profilePath = profilePath;
    }

    public String getProfilePath() 
    {
        return profilePath;
    }

    public void setBiography(String biography) 
    {
        this.biography = biography;
    }

    public String getBiography() 
    {
        return biography;
    }

    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("originalName", getOriginalName())
            .append("gender", getGender())
            .append("profilePath", getProfilePath())
            .append("biography", getBiography())
            .append("birthday", getBirthday())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
