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
import com.ruoyi.system.domain.Director;
import com.ruoyi.system.service.IDirectorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 导演Controller
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@RestController
@RequestMapping("/system/director")
public class DirectorController extends BaseController
{
    @Autowired
    private IDirectorService directorService;

    /**
     * 查询导演列表
     */
    @PreAuthorize("@ss.hasPermi('system:director:list')")
    @GetMapping("/list")
    public TableDataInfo list(Director director)
    {
        startPage();
        List<Director> list = directorService.selectDirectorList(director);
        return getDataTable(list);
    }

    /**
     * 导出导演列表
     */
    @PreAuthorize("@ss.hasPermi('system:director:export')")
    @Log(title = "导演", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Director director)
    {
        List<Director> list = directorService.selectDirectorList(director);
        ExcelUtil<Director> util = new ExcelUtil<Director>(Director.class);
        util.exportExcel(response, list, "导演数据");
    }

    /**
     * 获取导演详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:director:query')")
    @GetMapping(value = "/{movieId}")
    public AjaxResult getInfo(@PathVariable("movieId") Long movieId)
    {
        return success(directorService.selectDirectorByMovieId(movieId));
    }

    /**
     * 新增导演
     */
    @PreAuthorize("@ss.hasPermi('system:director:add')")
    @Log(title = "导演", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Director director)
    {
        return toAjax(directorService.insertDirector(director));
    }

    /**
     * 修改导演
     */
    @PreAuthorize("@ss.hasPermi('system:director:edit')")
    @Log(title = "导演", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Director director)
    {
        return toAjax(directorService.updateDirector(director));
    }

    /**
     * 删除导演
     */
    @PreAuthorize("@ss.hasPermi('system:director:remove')")
    @Log(title = "导演", businessType = BusinessType.DELETE)
	@DeleteMapping("/{movieIds}")
    public AjaxResult remove(@PathVariable Long[] movieIds)
    {
        return toAjax(directorService.deleteDirectorByMovieIds(movieIds));
    }
}
