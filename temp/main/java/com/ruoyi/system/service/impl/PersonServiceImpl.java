package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PersonMapper;
import com.ruoyi.system.domain.Person;
import com.ruoyi.system.service.IPersonService;

/**
 * 演职人员Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@Service
public class PersonServiceImpl implements IPersonService 
{
    @Autowired
    private PersonMapper personMapper;

    /**
     * 查询演职人员
     * 
     * @param id 演职人员主键
     * @return 演职人员
     */
    @Override
    public Person selectPersonById(Long id)
    {
        return personMapper.selectPersonById(id);
    }

    /**
     * 查询演职人员列表
     * 
     * @param person 演职人员
     * @return 演职人员
     */
    @Override
    public List<Person> selectPersonList(Person person)
    {
        return personMapper.selectPersonList(person);
    }

    /**
     * 新增演职人员
     * 
     * @param person 演职人员
     * @return 结果
     */
    @Override
    public int insertPerson(Person person)
    {
        return personMapper.insertPerson(person);
    }

    /**
     * 修改演职人员
     * 
     * @param person 演职人员
     * @return 结果
     */
    @Override
    public int updatePerson(Person person)
    {
        return personMapper.updatePerson(person);
    }

    /**
     * 批量删除演职人员
     * 
     * @param ids 需要删除的演职人员主键
     * @return 结果
     */
    @Override
    public int deletePersonByIds(Long[] ids)
    {
        return personMapper.deletePersonByIds(ids);
    }

    /**
     * 删除演职人员信息
     * 
     * @param id 演职人员主键
     * @return 结果
     */
    @Override
    public int deletePersonById(Long id)
    {
        return personMapper.deletePersonById(id);
    }
}
