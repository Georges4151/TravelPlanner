package com.travelplanner.mapper;

import com.travelplanner.entity.Budget;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BudgetMapper {
    @Select("SELECT * FROM budget WHERE itinerary_id = #{itineraryId}")
    Budget findByItineraryId(Long itineraryId);

    @Insert("INSERT INTO budget(itinerary_id, total_budget, transport_budget, hotel_budget, food_budget, ticket_budget, other_budget, create_time) VALUES(#{itineraryId}, #{totalBudget}, #{transportBudget}, #{hotelBudget}, #{foodBudget}, #{ticketBudget}, #{otherBudget}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Budget budget);

    @Update("UPDATE budget SET total_budget = #{totalBudget}, transport_budget = #{transportBudget}, hotel_budget = #{hotelBudget}, food_budget = #{foodBudget}, ticket_budget = #{ticketBudget}, other_budget = #{otherBudget}, actual_transport = #{actualTransport}, actual_hotel = #{actualHotel}, actual_food = #{actualFood}, actual_ticket = #{actualTicket}, actual_other = #{actualOther}, update_time = NOW() WHERE itinerary_id = #{itineraryId}")
    int update(Budget budget);

    @Delete("DELETE FROM budget WHERE itinerary_id = #{itineraryId}")
    int deleteByItineraryId(Long itineraryId);
}
