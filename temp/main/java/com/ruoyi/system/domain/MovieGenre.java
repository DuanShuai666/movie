package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电影类型关联对象 movie_genre
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public class MovieGenre extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long movieId;

    /** $column.columnComment */
    private Long genreId;

    public void setMovieId(Long movieId) 
    {
        this.movieId = movieId;
    }

    public Long getMovieId() 
    {
        return movieId;
    }

    public void setGenreId(Long genreId) 
    {
        this.genreId = genreId;
    }

    public Long getGenreId() 
    {
        return genreId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("movieId", getMovieId())
            .append("genreId", getGenreId())
            .toString();
    }
}
