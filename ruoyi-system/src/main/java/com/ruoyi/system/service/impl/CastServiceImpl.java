package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CastMapper;
import com.ruoyi.system.domain.Cast;
import com.ruoyi.system.service.ICastService;

/**
 * 演员Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@Service
public class CastServiceImpl implements ICastService 
{
    @Autowired
    private CastMapper castMapper;

    /**
     * 查询演员
     * 
     * @param movieId 演员主键
     * @return 演员
     */
    @Override
    public Cast selectCastByMovieId(Long movieId)
    {
        return castMapper.selectCastByMovieId(movieId);
    }

    /**
     * 查询演员列表
     * 
     * @param cast 演员
     * @return 演员
     */
    @Override
    public List<Cast> selectCastList(Cast cast)
    {
        return castMapper.selectCastList(cast);
    }

    /**
     * 新增演员
     * 
     * @param cast 演员
     * @return 结果
     */
    @Override
    public int insertCast(Cast cast)
    {
        return castMapper.insertCast(cast);
    }

    /**
     * 修改演员
     * 
     * @param cast 演员
     * @return 结果
     */
    @Override
    public int updateCast(Cast cast)
    {
        return castMapper.updateCast(cast);
    }

    /**
     * 批量删除演员
     * 
     * @param movieIds 需要删除的演员主键
     * @return 结果
     */
    @Override
    public int deleteCastByMovieIds(Long[] movieIds)
    {
        return castMapper.deleteCastByMovieIds(movieIds);
    }

    /**
     * 删除演员信息
     * 
     * @param movieId 演员主键
     * @return 结果
     */
    @Override
    public int deleteCastByMovieId(Long movieId)
    {
        return castMapper.deleteCastByMovieId(movieId);
    }
}
