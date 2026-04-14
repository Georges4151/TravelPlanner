package com.travelplanner.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.travelplanner.data.local.dao.AttractionDao;
import com.travelplanner.data.local.dao.AttractionDayDao;
import com.travelplanner.data.local.dao.ChecklistDao;
import com.travelplanner.data.local.dao.FavoriteDao;
import com.travelplanner.data.local.dao.HotelDao;
import com.travelplanner.data.local.dao.ItineraryDao;
import com.travelplanner.data.local.dao.ItineraryDayDao;
import com.travelplanner.data.local.dao.ReminderDao;
import com.travelplanner.data.local.dao.RestaurantDao;
import com.travelplanner.data.local.dao.TransportPlanDao;
import com.travelplanner.data.local.entity.AttractionDayEntity;
import com.travelplanner.data.local.entity.AttractionEntity;
import com.travelplanner.data.local.entity.ChecklistItemEntity;
import com.travelplanner.data.local.entity.FavoriteEntity;
import com.travelplanner.data.local.entity.HotelEntity;
import com.travelplanner.data.local.entity.ItineraryDayEntity;
import com.travelplanner.data.local.entity.ItineraryEntity;
import com.travelplanner.data.local.entity.ReminderEntity;
import com.travelplanner.data.local.entity.RestaurantEntity;
import com.travelplanner.data.local.entity.TransportPlanEntity;

@Database(entities = {
        AttractionEntity.class,
        RestaurantEntity.class,
        HotelEntity.class,
        ChecklistItemEntity.class,
        ItineraryEntity.class,
        ReminderEntity.class,
        ItineraryDayEntity.class,
        AttractionDayEntity.class,
        TransportPlanEntity.class,
        FavoriteEntity.class
}, version = 3, exportSchema = false)
@TypeConverters({ BigDecimalConverter.class })
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "travel_planner_db";

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AttractionDao attractionDao();

    public abstract RestaurantDao restaurantDao();

    public abstract HotelDao hotelDao();

    public abstract ChecklistDao checklistDao();

    public abstract ItineraryDao itineraryDao();

    public abstract ReminderDao reminderDao();

    public abstract ItineraryDayDao itineraryDayDao();

    public abstract AttractionDayDao attractionDayDao();

    public abstract TransportPlanDao transportPlanDao();

    public abstract FavoriteDao favoriteDao();
}
