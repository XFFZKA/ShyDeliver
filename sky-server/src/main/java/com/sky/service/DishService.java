package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    public void saveWithFlavors(DishDTO dish);

    PageResult pageQuery(DishPageQueryDTO queryDTO);

    void deleteBatch(List<Long> ids);

    DishVO findByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dish);

    void setStatus(Integer status, Long id);

    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


}
