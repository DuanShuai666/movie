package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电影基本信息对象 movie
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public class Movie extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** TMDB电影ID */
    private Long id;

    /** 中文标题 */
    @Excel(name = "中文标题")
    private String title;

    /** 原标题 */
    @Excel(name = "原标题")
    private String originalTitle;

    /** 简介（中文） */
    @Excel(name = "简介", readConverterExp = "中=文")
    private String overview;

    /** 上映日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上映日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date releaseDate;

    /** 上映年份 */
    @Excel(name = "上映年份")
    private Long year;

    /** 片长（分钟） */
    @Excel(name = "片长", readConverterExp = "分=钟")
    private Long runtime;

    /** 评分 */
    @Excel(name = "评分")
    private BigDecimal voteAverage;

    /** 评分人数 */
    @Excel(name = "评分人数")
    private Long voteCount;

    /** 热度值 */
    @Excel(name = "热度值")
    private BigDecimal popularity;

    /** 海报相对路径 */
    @Excel(name = "海报相对路径")
    private String posterPath;

    /** 背景图相对路径 */
    @Excel(name = "背景图相对路径")
    private String backdropPath;

    /** IMDB ID */
    @Excel(name = "IMDB ID")
    private String imdbId;

    /** 状态（Released/Rumored等） */
    @Excel(name = "状态", readConverterExp = "R=eleased/Rumored等")
    private String status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdAt;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date updatedAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setOriginalTitle(String originalTitle) 
    {
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() 
    {
        return originalTitle;
    }

    public void setOverview(String overview) 
    {
        this.overview = overview;
    }

    public String getOverview() 
    {
        return overview;
    }

    public void setReleaseDate(Date releaseDate) 
    {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDate() 
    {
        return releaseDate;
    }

    public void setYear(Long year) 
    {
        this.year = year;
    }

    public Long getYear() 
    {
        return year;
    }

    public void setRuntime(Long runtime) 
    {
        this.runtime = runtime;
    }

    public Long getRuntime() 
    {
        return runtime;
    }

    public void setVoteAverage(BigDecimal voteAverage) 
    {
        this.voteAverage = voteAverage;
    }

    public BigDecimal getVoteAverage() 
    {
        return voteAverage;
    }

    public void setVoteCount(Long voteCount) 
    {
        this.voteCount = voteCount;
    }

    public Long getVoteCount() 
    {
        return voteCount;
    }

    public void setPopularity(BigDecimal popularity) 
    {
        this.popularity = popularity;
    }

    public BigDecimal getPopularity() 
    {
        return popularity;
    }

    public void setPosterPath(String posterPath) 
    {
        this.posterPath = posterPath;
    }

    public String getPosterPath() 
    {
        return posterPath;
    }

    public void setBackdropPath(String backdropPath) 
    {
        this.backdropPath = backdropPath;
    }

    public String getBackdropPath() 
    {
        return backdropPath;
    }

    public void setImdbId(String imdbId) 
    {
        this.imdbId = imdbId;
    }

    public String getImdbId() 
    {
        return imdbId;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("originalTitle", getOriginalTitle())
            .append("overview", getOverview())
            .append("releaseDate", getReleaseDate())
            .append("year", getYear())
            .append("runtime", getRuntime())
            .append("voteAverage", getVoteAverage())
            .append("voteCount", getVoteCount())
            .append("popularity", getPopularity())
            .append("posterPath", getPosterPath())
            .append("backdropPath", getBackdropPath())
            .append("imdbId", getImdbId())
            .append("status", getStatus())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
