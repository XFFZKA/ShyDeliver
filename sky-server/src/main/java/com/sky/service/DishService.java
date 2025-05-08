package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface DishService {
    public void saveWithFlavors(DishDTO dish);

    PageResult pageQuery(DishPageQueryDTO queryDTO);
}
