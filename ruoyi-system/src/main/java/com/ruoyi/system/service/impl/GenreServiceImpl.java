package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.GenreMapper;
import com.ruoyi.system.domain.Genre;
import com.ruoyi.system.service.IGenreService;

/**
 * 电影类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@Service
public class GenreServiceImpl implements IGenreService 
{
    @Autowired
    private GenreMapper genreMapper;

    /**
     * 查询电影类型
     * 
     * @param id 电影类型主键
     * @return 电影类型
     */
    @Override
    public Genre selectGenreById(Long id)
    {
        return genreMapper.selectGenreById(id);
    }

    /**
     * 查询电影类型列表
     * 
     * @param genre 电影类型
     * @return 电影类型
     */
    @Override
    public List<Genre> selectGenreList(Genre genre)
    {
        return genreMapper.selectGenreList(genre);
    }

    /**
     * 新增电影类型
     * 
     * @param genre 电影类型
     * @return 结果
     */
    @Override
    public int insertGenre(Genre genre)
    {
        return genreMapper.insertGenre(genre);
    }

    /**
     * 修改电影类型
     * 
     * @param genre 电影类型
     * @return 结果
     */
    @Override
    public int updateGenre(Genre genre)
    {
        return genreMapper.updateGenre(genre);
    }

    /**
     * 批量删除电影类型
     * 
     * @param ids 需要删除的电影类型主键
     * @return 结果
     */
    @Override
    public int deleteGenreByIds(Long[] ids)
    {
        return genreMapper.deleteGenreByIds(ids);
    }

    /**
     * 删除电影类型信息
     * 
     * @param id 电影类型主键
     * @return 结果
     */
    @Override
    public int deleteGenreById(Long id)
    {
        return genreMapper.deleteGenreById(id);
    }
}
