package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {


    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dish) {
        dishService.saveWithFlavors(dish);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO queryDTO) {
        log.info("queryDTO:{}", queryDTO);
        PageResult pageResult = dishService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    public Result<DishVO> findById(@PathVariable Long id) {
        log.info("id:{}", id);
        DishVO dish = dishService.findByIdWithFlavor(id);
        return Result.success(dish);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dish) {
        dishService.updateWithFlavor(dish);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用菜品")
    public Result updateDishStatus(@PathVariable Integer status, Long id) {
        log.info("status:{}", status);
        dishService.setStatus(status,id);
        return Result.success();

    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("categoryId:{}", categoryId);
        List<Dish> dishes = dishService.list(categoryId);
        return Result.success(dishes);
    }

}
