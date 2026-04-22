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
import com.ruoyi.system.domain.Cast;
import com.ruoyi.system.service.ICastService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 演员Controller
 * 
 * @author ruoyi
 * @date 2026-04-16
 */
@RestController
@RequestMapping("/system/cast")
public class CastController extends BaseController
{
    @Autowired
    private ICastService castService;

    /**
     * 查询演员列表
     */
    @PreAuthorize("@ss.hasPermi('system:cast:list')")
    @GetMapping("/list")
    public TableDataInfo list(Cast cast)
    {
        startPage();
        List<Cast> list = castService.selectCastList(cast);
        return getDataTable(list);
    }

    /**
     * 导出演员列表
     */
    @PreAuthorize("@ss.hasPermi('system:cast:export')")
    @Log(title = "演员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Cast cast)
    {
        List<Cast> list = castService.selectCastList(cast);
        ExcelUtil<Cast> util = new ExcelUtil<Cast>(Cast.class);
        util.exportExcel(response, list, "演员数据");
    }

    /**
     * 获取演员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cast:query')")
    @GetMapping(value = "/{movieId}")
    public AjaxResult getInfo(@PathVariable("movieId") Long movieId)
    {
        return success(castService.selectCastByMovieId(movieId));
    }

    /**
     * 新增演员
     */
    @PreAuthorize("@ss.hasPermi('system:cast:add')")
    @Log(title = "演员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Cast cast)
    {
        return toAjax(castService.insertCast(cast));
    }

    /**
     * 修改演员
     */
    @PreAuthorize("@ss.hasPermi('system:cast:edit')")
    @Log(title = "演员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Cast cast)
    {
        return toAjax(castService.updateCast(cast));
    }

    /**
     * 删除演员
     */
    @PreAuthorize("@ss.hasPermi('system:cast:remove')")
    @Log(title = "演员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{movieIds}")
    public AjaxResult remove(@PathVariable Long[] movieIds)
    {
        return toAjax(castService.deleteCastByMovieIds(movieIds));
    }
}
