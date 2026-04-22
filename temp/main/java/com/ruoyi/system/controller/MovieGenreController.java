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
import com.ruoyi.system.domain.MovieGenre;
import com.ruoyi.system.service.IMovieGenreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 电影类型关联Controller
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@RestController
@RequestMapping("/system/genre")
public class MovieGenreController extends BaseController
{
    @Autowired
    private IMovieGenreService movieGenreService;

    /**
     * 查询电影类型关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:genre:list')")
    @GetMapping("/list")
    public TableDataInfo list(MovieGenre movieGenre)
    {
        startPage();
        List<MovieGenre> list = movieGenreService.selectMovieGenreList(movieGenre);
        return getDataTable(list);
    }

    /**
     * 导出电影类型关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:genre:export')")
    @Log(title = "电影类型关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MovieGenre movieGenre)
    {
        List<MovieGenre> list = movieGenreService.selectMovieGenreList(movieGenre);
        ExcelUtil<MovieGenre> util = new ExcelUtil<MovieGenre>(MovieGenre.class);
        util.exportExcel(response, list, "电影类型关联数据");
    }

    /**
     * 获取电影类型关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:genre:query')")
    @GetMapping(value = "/{movieId}")
    public AjaxResult getInfo(@PathVariable("movieId") Long movieId)
    {
        return success(movieGenreService.selectMovieGenreByMovieId(movieId));
    }

    /**
     * 新增电影类型关联
     */
    @PreAuthorize("@ss.hasPermi('system:genre:add')")
    @Log(title = "电影类型关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MovieGenre movieGenre)
    {
        return toAjax(movieGenreService.insertMovieGenre(movieGenre));
    }

    /**
     * 修改电影类型关联
     */
    @PreAuthorize("@ss.hasPermi('system:genre:edit')")
    @Log(title = "电影类型关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MovieGenre movieGenre)
    {
        return toAjax(movieGenreService.updateMovieGenre(movieGenre));
    }

    /**
     * 删除电影类型关联
     */
    @PreAuthorize("@ss.hasPermi('system:genre:remove')")
    @Log(title = "电影类型关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{movieIds}")
    public AjaxResult remove(@PathVariable Long[] movieIds)
    {
        return toAjax(movieGenreService.deleteMovieGenreByMovieIds(movieIds));
    }
}
