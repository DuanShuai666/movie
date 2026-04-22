package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DirectorMapper;
import com.ruoyi.system.domain.Director;
import com.ruoyi.system.service.IDirectorService;

/**
 * 导演Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@Service
public class DirectorServiceImpl implements IDirectorService 
{
    @Autowired
    private DirectorMapper directorMapper;

    /**
     * 查询导演
     * 
     * @param movieId 导演主键
     * @return 导演
     */
    @Override
    public Director selectDirectorByMovieId(Long movieId)
    {
        return directorMapper.selectDirectorByMovieId(movieId);
    }

    /**
     * 查询导演列表
     * 
     * @param director 导演
     * @return 导演
     */
    @Override
    public List<Director> selectDirectorList(Director director)
    {
        return directorMapper.selectDirectorList(director);
    }

    /**
     * 新增导演
     * 
     * @param director 导演
     * @return 结果
     */
    @Override
    public int insertDirector(Director director)
    {
        return directorMapper.insertDirector(director);
    }

    /**
     * 修改导演
     * 
     * @param director 导演
     * @return 结果
     */
    @Override
    public int updateDirector(Director director)
    {
        return directorMapper.updateDirector(director);
    }

    /**
     * 批量删除导演
     * 
     * @param movieIds 需要删除的导演主键
     * @return 结果
     */
    @Override
    public int deleteDirectorByMovieIds(Long[] movieIds)
    {
        return directorMapper.deleteDirectorByMovieIds(movieIds);
    }

    /**
     * 删除导演信息
     * 
     * @param movieId 导演主键
     * @return 结果
     */
    @Override
    public int deleteDirectorByMovieId(Long movieId)
    {
        return directorMapper.deleteDirectorByMovieId(movieId);
    }
}
