package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Director;

/**
 * 导演Service接口
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public interface IDirectorService 
{
    /**
     * 查询导演
     * 
     * @param movieId 导演主键
     * @return 导演
     */
    public Director selectDirectorByMovieId(Long movieId);

    /**
     * 查询导演列表
     * 
     * @param director 导演
     * @return 导演集合
     */
    public List<Director> selectDirectorList(Director director);

    /**
     * 新增导演
     * 
     * @param director 导演
     * @return 结果
     */
    public int insertDirector(Director director);

    /**
     * 修改导演
     * 
     * @param director 导演
     * @return 结果
     */
    public int updateDirector(Director director);

    /**
     * 批量删除导演
     * 
     * @param movieIds 需要删除的导演主键集合
     * @return 结果
     */
    public int deleteDirectorByMovieIds(Long[] movieIds);

    /**
     * 删除导演信息
     * 
     * @param movieId 导演主键
     * @return 结果
     */
    public int deleteDirectorByMovieId(Long movieId);
}
