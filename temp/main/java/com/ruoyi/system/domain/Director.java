package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 导演对象 director
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public class Director extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long movieId;

    /** $column.columnComment */
    private Long personId;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("movieId", getMovieId())
            .append("personId", getPersonId())
            .toString();
    }
}
