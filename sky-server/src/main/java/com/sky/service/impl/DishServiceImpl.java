package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    @Transactional
    public void saveWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        Long dishId = dish.getId();


        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> {flavor.setDishId(dishId);});

            dishFlavorMapper.insertBatch(flavors);
        }


    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO queryDTO) {

        PageHelper.startPage(queryDTO.getPage(),queryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(queryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //是否在起售
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //是否被套餐关联了
        List<Long> setMeal = setmealDishMapper.findSealIdByDishId(ids);
        if (setMeal != null && setMeal.size() > 0) {
            throw  new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品表中的菜品数据

//        for (Long id : ids) {
//            dishMapper.delete(id);
//            dishFlavorMapper.deleteByDishId(id);
//        }

        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
        //删除菜品关联的口味数据
    }

    @Override
    public DishVO findByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        List<DishFlavor> dishFlavors = dishFlavorMapper.getFlavorByDishId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dish) {
        Dish dish1 = new Dish();
        BeanUtils.copyProperties(dish, dish1);
        dishMapper.update(dish1);

        Long dishId = dish.getId();
        dishFlavorMapper.deleteByDishId(dishId);


        List<DishFlavor> flavors = dish.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> {flavor.setDishId(dishId);});
            dishFlavorMapper.insertBatch(flavors);
        }


    }

    @Transactional
    @Override
    public void setStatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);

            List<Long> sealIds = setmealDishMapper.findSealIdByDishId(dishIds);
            if (sealIds != null && sealIds.size() > 0) {
                for(Long sealId : sealIds){
                    Setmeal setmeal = new Setmeal();
                    setmeal.setId(sealId);
                    setmeal.setStatus(StatusConstant.DISABLE);
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<Dish> dishes = dishMapper.list(dish);
        return dishes;
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavorByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
