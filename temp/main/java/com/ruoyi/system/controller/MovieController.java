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
import com.ruoyi.system.domain.Movie;
import com.ruoyi.system.service.IMovieService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 电影基本信息Controller
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@RestController
@RequestMapping("/system/movie")
public class MovieController extends BaseController
{
    @Autowired
    private IMovieService movieService;

    /**
     * 查询电影基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:movie:list')")
    @GetMapping("/list")
    public TableDataInfo list(Movie movie)
    {
        startPage();
        List<Movie> list = movieService.selectMovieList(movie);
        return getDataTable(list);
    }

    /**
     * 导出电影基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:movie:export')")
    @Log(title = "电影基本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Movie movie)
    {
        List<Movie> list = movieService.selectMovieList(movie);
        ExcelUtil<Movie> util = new ExcelUtil<Movie>(Movie.class);
        util.exportExcel(response, list, "电影基本信息数据");
    }

    /**
     * 获取电影基本信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:movie:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(movieService.selectMovieById(id));
    }

    /**
     * 新增电影基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:movie:add')")
    @Log(title = "电影基本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Movie movie)
    {
        return toAjax(movieService.insertMovie(movie));
    }

    /**
     * 修改电影基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:movie:edit')")
    @Log(title = "电影基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Movie movie)
    {
        return toAjax(movieService.updateMovie(movie));
    }

    /**
     * 删除电影基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:movie:remove')")
    @Log(title = "电影基本信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(movieService.deleteMovieByIds(ids));
    }
}
