package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Person;

/**
 * 演职人员Service接口
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
public interface IPersonService 
{
    /**
     * 查询演职人员
     * 
     * @param id 演职人员主键
     * @return 演职人员
     */
    public Person selectPersonById(Long id);

    /**
     * 查询演职人员列表
     * 
     * @param person 演职人员
     * @return 演职人员集合
     */
    public List<Person> selectPersonList(Person person);

    /**
     * 新增演职人员
     * 
     * @param person 演职人员
     * @return 结果
     */
    public int insertPerson(Person person);

    /**
     * 修改演职人员
     * 
     * @param person 演职人员
     * @return 结果
     */
    public int updatePerson(Person person);

    /**
     * 批量删除演职人员
     * 
     * @param ids 需要删除的演职人员主键集合
     * @return 结果
     */
    public int deletePersonByIds(Long[] ids);

    /**
     * 删除演职人员信息
     * 
     * @param id 演职人员主键
     * @return 结果
     */
    public int deletePersonById(Long id);
}
