package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MovieMapper;
import com.ruoyi.system.domain.Movie;
import com.ruoyi.system.service.IMovieService;

/**
 * 电影基本信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@Service
public class MovieServiceImpl implements IMovieService 
{
    @Autowired
    private MovieMapper movieMapper;

    /**
     * 查询电影基本信息
     * 
     * @param id 电影基本信息主键
     * @return 电影基本信息
     */
    @Override
    public Movie selectMovieById(Long id)
    {
        return movieMapper.selectMovieById(id);
    }

    /**
     * 查询电影基本信息列表
     * 
     * @param movie 电影基本信息
     * @return 电影基本信息
     */
    @Override
    public List<Movie> selectMovieList(Movie movie)
    {
        return movieMapper.selectMovieList(movie);
    }

    /**
     * 新增电影基本信息
     * 
     * @param movie 电影基本信息
     * @return 结果
     */
    @Override
    public int insertMovie(Movie movie)
    {
        return movieMapper.insertMovie(movie);
    }

    /**
     * 修改电影基本信息
     * 
     * @param movie 电影基本信息
     * @return 结果
     */
    @Override
    public int updateMovie(Movie movie)
    {
        return movieMapper.updateMovie(movie);
    }

    /**
     * 批量删除电影基本信息
     * 
     * @param ids 需要删除的电影基本信息主键
     * @return 结果
     */
    @Override
    public int deleteMovieByIds(Long[] ids)
    {
        return movieMapper.deleteMovieByIds(ids);
    }

    /**
     * 删除电影基本信息信息
     * 
     * @param id 电影基本信息主键
     * @return 结果
     */
    @Override
    public int deleteMovieById(Long id)
    {
        return movieMapper.deleteMovieById(id);
    }
}
