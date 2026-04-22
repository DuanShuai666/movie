package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MovieGenre;

/**
 * 电影类型关联Service接口
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public interface IMovieGenreService 
{
    /**
     * 查询电影类型关联
     * 
     * @param movieId 电影类型关联主键
     * @return 电影类型关联
     */
    public MovieGenre selectMovieGenreByMovieId(Long movieId);

    /**
     * 查询电影类型关联列表
     * 
     * @param movieGenre 电影类型关联
     * @return 电影类型关联集合
     */
    public List<MovieGenre> selectMovieGenreList(MovieGenre movieGenre);

    /**
     * 新增电影类型关联
     * 
     * @param movieGenre 电影类型关联
     * @return 结果
     */
    public int insertMovieGenre(MovieGenre movieGenre);

    /**
     * 修改电影类型关联
     * 
     * @param movieGenre 电影类型关联
     * @return 结果
     */
    public int updateMovieGenre(MovieGenre movieGenre);

    /**
     * 批量删除电影类型关联
     * 
     * @param movieIds 需要删除的电影类型关联主键集合
     * @return 结果
     */
    public int deleteMovieGenreByMovieIds(Long[] movieIds);

    /**
     * 删除电影类型关联信息
     * 
     * @param movieId 电影类型关联主键
     * @return 结果
     */
    public int deleteMovieGenreByMovieId(Long movieId);
}
