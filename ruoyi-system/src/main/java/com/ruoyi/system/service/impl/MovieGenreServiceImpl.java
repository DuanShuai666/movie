package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MovieGenreMapper;
import com.ruoyi.system.domain.MovieGenre;
import com.ruoyi.system.service.IMovieGenreService;

/**
 * 电影类型关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@Service
public class MovieGenreServiceImpl implements IMovieGenreService 
{
    @Autowired
    private MovieGenreMapper movieGenreMapper;

    /**
     * 查询电影类型关联
     * 
     * @param movieId 电影类型关联主键
     * @return 电影类型关联
     */
    @Override
    public MovieGenre selectMovieGenreByMovieId(Long movieId)
    {
        return movieGenreMapper.selectMovieGenreByMovieId(movieId);
    }

    /**
     * 查询电影类型关联列表
     * 
     * @param movieGenre 电影类型关联
     * @return 电影类型关联
     */
    @Override
    public List<MovieGenre> selectMovieGenreList(MovieGenre movieGenre)
    {
        return movieGenreMapper.selectMovieGenreList(movieGenre);
    }

    /**
     * 新增电影类型关联
     * 
     * @param movieGenre 电影类型关联
     * @return 结果
     */
    @Override
    public int insertMovieGenre(MovieGenre movieGenre)
    {
        return movieGenreMapper.insertMovieGenre(movieGenre);
    }

    /**
     * 修改电影类型关联
     * 
     * @param movieGenre 电影类型关联
     * @return 结果
     */
    @Override
    public int updateMovieGenre(MovieGenre movieGenre)
    {
        return movieGenreMapper.updateMovieGenre(movieGenre);
    }

    /**
     * 批量删除电影类型关联
     * 
     * @param movieIds 需要删除的电影类型关联主键
     * @return 结果
     */
    @Override
    public int deleteMovieGenreByMovieIds(Long[] movieIds)
    {
        return movieGenreMapper.deleteMovieGenreByMovieIds(movieIds);
    }

    /**
     * 删除电影类型关联信息
     * 
     * @param movieId 电影类型关联主键
     * @return 结果
     */
    @Override
    public int deleteMovieGenreByMovieId(Long movieId)
    {
        return movieGenreMapper.deleteMovieGenreByMovieId(movieId);
    }
}
