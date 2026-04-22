package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电影类型对象 genre
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public class Genre extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** TMDB类型ID */
    private Long id;

    /** 类型名称（中文） */
    @Excel(name = "类型名称", readConverterExp = "中=文")
    private String name;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .toString();
    }
}
