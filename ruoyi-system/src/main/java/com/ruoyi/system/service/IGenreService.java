package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Genre;

/**
 * 电影类型Service接口
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public interface IGenreService 
{
    /**
     * 查询电影类型
     * 
     * @param id 电影类型主键
     * @return 电影类型
     */
    public Genre selectGenreById(Long id);

    /**
     * 查询电影类型列表
     * 
     * @param genre 电影类型
     * @return 电影类型集合
     */
    public List<Genre> selectGenreList(Genre genre);

    /**
     * 新增电影类型
     * 
     * @param genre 电影类型
     * @return 结果
     */
    public int insertGenre(Genre genre);

    /**
     * 修改电影类型
     * 
     * @param genre 电影类型
     * @return 结果
     */
    public int updateGenre(Genre genre);

    /**
     * 批量删除电影类型
     * 
     * @param ids 需要删除的电影类型主键集合
     * @return 结果
     */
    public int deleteGenreByIds(Long[] ids);

    /**
     * 删除电影类型信息
     * 
     * @param id 电影类型主键
     * @return 结果
     */
    public int deleteGenreById(Long id);
}
