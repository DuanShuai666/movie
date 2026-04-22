package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Movie;

/**
 * 电影基本信息Service接口
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public interface IMovieService 
{
    /**
     * 查询电影基本信息
     * 
     * @param id 电影基本信息主键
     * @return 电影基本信息
     */
    public Movie selectMovieById(Long id);

    /**
     * 查询电影基本信息列表
     * 
     * @param movie 电影基本信息
     * @return 电影基本信息集合
     */
    public List<Movie> selectMovieList(Movie movie);

    /**
     * 新增电影基本信息
     * 
     * @param movie 电影基本信息
     * @return 结果
     */
    public int insertMovie(Movie movie);

    /**
     * 修改电影基本信息
     * 
     * @param movie 电影基本信息
     * @return 结果
     */
    public int updateMovie(Movie movie);

    /**
     * 批量删除电影基本信息
     * 
     * @param ids 需要删除的电影基本信息主键集合
     * @return 结果
     */
    public int deleteMovieByIds(Long[] ids);

    /**
     * 删除电影基本信息信息
     * 
     * @param id 电影基本信息主键
     * @return 结果
     */
    public int deleteMovieById(Long id);
}
