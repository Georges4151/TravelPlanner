package com.travelplanner.mapper;

import com.travelplanner.entity.TravelChecklist;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TravelChecklistMapper {
    
    @Select("SELECT * FROM travel_checklist WHERE itinerary_id = #{itineraryId} ORDER BY category, id")
    List<TravelChecklist> findByItineraryId(@Param("itineraryId") Long itineraryId);
    
    @Select("SELECT * FROM travel_checklist WHERE itinerary_id = #{itineraryId} AND category = #{category} ORDER BY id")
    List<TravelChecklist> findByItineraryIdAndCategory(@Param("itineraryId") Long itineraryId, 
                                                        @Param("category") String category);
    
    @Select("SELECT * FROM travel_checklist WHERE id = #{id}")
    TravelChecklist findById(@Param("id") Long id);
    
    @Select("SELECT COUNT(*) FROM travel_checklist WHERE itinerary_id = #{itineraryId}")
    int countByItineraryId(@Param("itineraryId") Long itineraryId);
    
    @Select("SELECT COUNT(*) FROM travel_checklist WHERE itinerary_id = #{itineraryId} AND is_packed = true")
    int countPackedByItineraryId(@Param("itineraryId") Long itineraryId);
    
    @Insert("INSERT INTO travel_checklist(itinerary_id, item_name, category, quantity, is_packed, is_purchased, notes) " +
            "VALUES(#{itineraryId}, #{itemName}, #{category}, #{quantity}, #{isPacked}, #{isPurchased}, #{notes})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TravelChecklist checklist);
    
    @Update("UPDATE travel_checklist SET item_name=#{itemName}, category=#{category}, quantity=#{quantity}, " +
            "is_packed=#{isPacked}, is_purchased=#{isPurchased}, notes=#{notes} WHERE id=#{id}")
    int update(TravelChecklist checklist);
    
    @Update("UPDATE travel_checklist SET is_packed = #{isPacked} WHERE id = #{id}")
    int updatePackedStatus(@Param("id") Long id, @Param("isPacked") Boolean isPacked);
    
    @Update("UPDATE travel_checklist SET is_purchased = #{isPurchased} WHERE id = #{id}")
    int updatePurchasedStatus(@Param("id") Long id, @Param("isPurchased") Boolean isPurchased);
    
    @Delete("DELETE FROM travel_checklist WHERE id = #{id}")
    int delete(@Param("id") Long id);
    
    @Delete("DELETE FROM travel_checklist WHERE itinerary_id = #{itineraryId}")
    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
    
    @Select("SELECT DISTINCT category FROM travel_checklist WHERE itinerary_id = #{itineraryId} ORDER BY category")
    List<String> findCategoriesByItineraryId(@Param("itineraryId") Long itineraryId);
}
