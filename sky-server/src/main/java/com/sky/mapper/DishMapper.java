package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(values = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO queryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id = #{id}")
    void delete(Long id);

    @AutoFill(values = OperationType.UPDATE)
    void update(Dish dish1);

    void deleteByIds(List<Long> ids);

    List<Dish> list(Dish dish);
}
