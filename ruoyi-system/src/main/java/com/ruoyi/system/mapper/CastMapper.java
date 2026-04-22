package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Cast;

/**
 * 演员Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public interface CastMapper 
{
    /**
     * 查询演员
     * 
     * @param movieId 演员主键
     * @return 演员
     */
    public Cast selectCastByMovieId(Long movieId);

    /**
     * 查询演员列表
     * 
     * @param cast 演员
     * @return 演员集合
     */
    public List<Cast> selectCastList(Cast cast);

    /**
     * 新增演员
     * 
     * @param cast 演员
     * @return 结果
     */
    public int insertCast(Cast cast);

    /**
     * 修改演员
     * 
     * @param cast 演员
     * @return 结果
     */
    public int updateCast(Cast cast);

    /**
     * 删除演员
     * 
     * @param movieId 演员主键
     * @return 结果
     */
    public int deleteCastByMovieId(Long movieId);

    /**
     * 批量删除演员
     * 
     * @param movieIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCastByMovieIds(Long[] movieIds);
}
