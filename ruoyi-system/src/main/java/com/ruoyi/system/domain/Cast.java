package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 演员对象 cast
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public class Cast extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long movieId;

    /** $column.columnComment */
    private Long personId;

    /** 角色名 */
    @Excel(name = "角色名")
    private String character;

    /** 演员排序（0为主演） */
    @Excel(name = "演员排序", readConverterExp = "0=为主演")
    private Long order;

    public void setMovieId(Long movieId) 
    {
        this.movieId = movieId;
    }

    public Long getMovieId() 
    {
        return movieId;
    }

    public void setPersonId(Long personId) 
    {
        this.personId = personId;
    }

    public Long getPersonId() 
    {
        return personId;
    }

    public void setCharacter(String character) 
    {
        this.character = character;
    }

    public String getCharacter() 
    {
        return character;
    }

    public void setOrder(Long order) 
    {
        this.order = order;
    }

    public Long getOrder() 
    {
        return order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("movieId", getMovieId())
            .append("personId", getPersonId())
            .append("character", getCharacter())
            .append("order", getOrder())
            .toString();
    }
}
