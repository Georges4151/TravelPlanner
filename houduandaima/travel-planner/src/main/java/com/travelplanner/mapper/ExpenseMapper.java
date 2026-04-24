package com.travelplanner.mapper;

import com.travelplanner.entity.Expense;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExpenseMapper {
    @Select("SELECT * FROM expense WHERE itinerary_id = #{itineraryId} ORDER BY expense_time DESC")
    List<Expense> findByItineraryId(Long itineraryId);

    @Select("SELECT SUM(amount) FROM expense WHERE itinerary_id = #{itineraryId}")
    java.math.BigDecimal sumAmountByItineraryId(Long itineraryId);

    @Insert("INSERT INTO expense(itinerary_id, user_id, category, item_name, amount, pay_method, notes, expense_time, create_time) VALUES(#{itineraryId}, #{userId}, #{category}, #{itemName}, #{amount}, #{payMethod}, #{notes}, #{expenseTime}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Expense expense);

    @Delete("DELETE FROM expense WHERE id = #{id}")
    int deleteById(Long id);
}
