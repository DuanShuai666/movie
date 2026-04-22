package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Genre;
import com.ruoyi.system.service.IGenreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 电影类型Controller
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@RestController
@RequestMapping("/system/genre")
public class GenreController extends BaseController
{
    @Autowired
    private IGenreService genreService;

    /**
     * 查询电影类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:genre:list')")
    @GetMapping("/list")
    public TableDataInfo list(Genre genre)
    {
        startPage();
        List<Genre> list = genreService.selectGenreList(genre);
        return getDataTable(list);
    }

    /**
     * 导出电影类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:genre:export')")
    @Log(title = "电影类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Genre genre)
    {
        List<Genre> list = genreService.selectGenreList(genre);
        ExcelUtil<Genre> util = new ExcelUtil<Genre>(Genre.class);
        util.exportExcel(response, list, "电影类型数据");
    }

    /**
     * 获取电影类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:genre:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(genreService.selectGenreById(id));
    }

    /**
     * 新增电影类型
     */
    @PreAuthorize("@ss.hasPermi('system:genre:add')")
    @Log(title = "电影类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Genre genre)
    {
        return toAjax(genreService.insertGenre(genre));
    }

    /**
     * 修改电影类型
     */
    @PreAuthorize("@ss.hasPermi('system:genre:edit')")
    @Log(title = "电影类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Genre genre)
    {
        return toAjax(genreService.updateGenre(genre));
    }

    /**
     * 删除电影类型
     */
    @PreAuthorize("@ss.hasPermi('system:genre:remove')")
    @Log(title = "电影类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(genreService.deleteGenreByIds(ids));
    }
}
