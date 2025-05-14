package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetmealService {

    void saveWithDish(SetmealDTO setmeal);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
