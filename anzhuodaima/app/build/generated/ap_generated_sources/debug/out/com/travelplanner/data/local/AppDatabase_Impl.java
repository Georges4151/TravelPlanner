package com.travelplanner.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.travelplanner.data.local.dao.AttractionDao;
import com.travelplanner.data.local.dao.AttractionDao_Impl;
import com.travelplanner.data.local.dao.AttractionDayDao;
import com.travelplanner.data.local.dao.AttractionDayDao_Impl;
import com.travelplanner.data.local.dao.ChecklistDao;
import com.travelplanner.data.local.dao.ChecklistDao_Impl;
import com.travelplanner.data.local.dao.FavoriteDao;
import com.travelplanner.data.local.dao.FavoriteDao_Impl;
import com.travelplanner.data.local.dao.HotelDao;
import com.travelplanner.data.local.dao.HotelDao_Impl;
import com.travelplanner.data.local.dao.ItineraryDao;
import com.travelplanner.data.local.dao.ItineraryDao_Impl;
import com.travelplanner.data.local.dao.ItineraryDayDao;
import com.travelplanner.data.local.dao.ItineraryDayDao_Impl;
import com.travelplanner.data.local.dao.ReminderDao;
import com.travelplanner.data.local.dao.ReminderDao_Impl;
import com.travelplanner.data.local.dao.RestaurantDao;
import com.travelplanner.data.local.dao.RestaurantDao_Impl;
import com.travelplanner.data.local.dao.TodoDao;
import com.travelplanner.data.local.dao.TodoDao_Impl;
import com.travelplanner.data.local.dao.TransportPlanDao;
import com.travelplanner.data.local.dao.TransportPlanDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AttractionDao _attractionDao;

  private volatile RestaurantDao _restaurantDao;

  private volatile HotelDao _hotelDao;

  private volatile ChecklistDao _checklistDao;

  private volatile ItineraryDao _itineraryDao;

  private volatile ReminderDao _reminderDao;

  private volatile ItineraryDayDao _itineraryDayDao;

  private volatile AttractionDayDao _attractionDayDao;

  private volatile TransportPlanDao _transportPlanDao;

  private volatile FavoriteDao _favoriteDao;

  private volatile TodoDao _todoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `attractions` (`id` INTEGER, `name` TEXT, `city` TEXT, `province` TEXT, `description` TEXT, `address` TEXT, `openTime` TEXT, `ticketPrice` TEXT, `playDuration` INTEGER, `crowdLevel` TEXT, `crowdIndex` TEXT, `bestVisitTime` TEXT, `imageUrl` TEXT, `rating` REAL, `commentCount` INTEGER, `latitude` REAL, `longitude` REAL, `reservationUrl` TEXT, `phone` TEXT, `facilities` TEXT, `tips` TEXT, `cacheTime` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `restaurants` (`id` INTEGER, `name` TEXT, `city` TEXT, `address` TEXT, `avgPrice` TEXT, `rating` REAL, `cuisineType` TEXT, `imageUrl` TEXT, `businessHours` TEXT, `latitude` REAL, `longitude` REAL, `phone` TEXT, `distance` INTEGER, `queueStatus` TEXT, `waitTime` INTEGER, `specialtyDishes` TEXT, `commentCount` INTEGER, `cacheTime` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `hotels` (`id` INTEGER, `name` TEXT, `city` TEXT, `address` TEXT, `price` TEXT, `roomType` TEXT, `rating` REAL, `imageUrl` TEXT, `facilities` TEXT, `latitude` REAL, `longitude` REAL, `phone` TEXT, `distanceToCenter` INTEGER, `nearbyAttractions` TEXT, `checkInTime` TEXT, `checkOutTime` TEXT, `commentCount` INTEGER, `bookingUrl` TEXT, `cacheTime` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `checklist_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `itineraryId` INTEGER, `itemName` TEXT, `category` TEXT, `isChecked` INTEGER, `notes` TEXT, `sortOrder` INTEGER, `createTime` INTEGER, `updateTime` INTEGER, FOREIGN KEY(`itineraryId`) REFERENCES `itineraries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_checklist_items_itineraryId` ON `checklist_items` (`itineraryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `itineraries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT, `destination` TEXT, `startDate` TEXT, `endDate` TEXT, `days` INTEGER, `personCount` INTEGER, `budget` TEXT, `style` TEXT, `status` INTEGER, `departureAddress` TEXT, `departureLatitude` REAL, `departureLongitude` REAL, `createTime` INTEGER, `updateTime` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reminders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `itineraryId` INTEGER, `title` TEXT, `content` TEXT, `remindTime` INTEGER, `type` TEXT, `status` INTEGER, `createTime` INTEGER, `updateTime` INTEGER, FOREIGN KEY(`itineraryId`) REFERENCES `itineraries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_reminders_itineraryId` ON `reminders` (`itineraryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `itinerary_days` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `itineraryId` INTEGER, `dayNumber` INTEGER, `date` TEXT, `notes` TEXT, `createTime` INTEGER, `updateTime` INTEGER, FOREIGN KEY(`itineraryId`) REFERENCES `itineraries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_itinerary_days_itineraryId` ON `itinerary_days` (`itineraryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attraction_days` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `dayId` INTEGER, `attractionId` INTEGER, `visitOrder` INTEGER, `plannedStartTime` TEXT, `plannedEndTime` TEXT, `notes` TEXT, `createTime` INTEGER, `updateTime` INTEGER, FOREIGN KEY(`dayId`) REFERENCES `itinerary_days`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`attractionId`) REFERENCES `attractions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attraction_days_dayId` ON `attraction_days` (`dayId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_attraction_days_attractionId` ON `attraction_days` (`attractionId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transport_plans` (`planId` TEXT NOT NULL, `itineraryId` INTEGER, `dayId` INTEGER, `fromAddress` TEXT, `toAddress` TEXT, `fromLatitude` TEXT, `fromLongitude` TEXT, `toLatitude` TEXT, `toLongitude` TEXT, `transportMode` TEXT, `distance` REAL, `duration` INTEGER, `cost` TEXT, `trafficStatus` TEXT, `routeData` TEXT, `createTime` INTEGER, `updateTime` INTEGER, PRIMARY KEY(`planId`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transport_plans_itineraryId` ON `transport_plans` (`itineraryId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transport_plans_dayId` ON `transport_plans` (`dayId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `poiId` TEXT, `type` TEXT, `name` TEXT, `address` TEXT, `imageUrl` TEXT, `rating` TEXT, `price` TEXT, `phone` TEXT, `location` TEXT, `createdAt` INTEGER, `poiJson` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `team_todos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `serverId` INTEGER, `teamId` INTEGER, `title` TEXT, `content` TEXT, `priority` TEXT, `dueDate` TEXT, `assigneeId` INTEGER, `assigneeName` TEXT, `creatorId` INTEGER, `creatorName` TEXT, `status` TEXT, `createTime` INTEGER, `updateTime` INTEGER)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_team_todos_teamId` ON `team_todos` (`teamId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_team_todos_assigneeId` ON `team_todos` (`assigneeId`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_team_todos_serverId` ON `team_todos` (`serverId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '91140e4ac3ea94b8a46b4ae4240bef27')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `attractions`");
        db.execSQL("DROP TABLE IF EXISTS `restaurants`");
        db.execSQL("DROP TABLE IF EXISTS `hotels`");
        db.execSQL("DROP TABLE IF EXISTS `checklist_items`");
        db.execSQL("DROP TABLE IF EXISTS `itineraries`");
        db.execSQL("DROP TABLE IF EXISTS `reminders`");
        db.execSQL("DROP TABLE IF EXISTS `itinerary_days`");
        db.execSQL("DROP TABLE IF EXISTS `attraction_days`");
        db.execSQL("DROP TABLE IF EXISTS `transport_plans`");
        db.execSQL("DROP TABLE IF EXISTS `favorites`");
        db.execSQL("DROP TABLE IF EXISTS `team_todos`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAttractions = new HashMap<String, TableInfo.Column>(22);
        _columnsAttractions.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("city", new TableInfo.Column("city", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("province", new TableInfo.Column("province", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("openTime", new TableInfo.Column("openTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("ticketPrice", new TableInfo.Column("ticketPrice", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("playDuration", new TableInfo.Column("playDuration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("crowdLevel", new TableInfo.Column("crowdLevel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("crowdIndex", new TableInfo.Column("crowdIndex", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("bestVisitTime", new TableInfo.Column("bestVisitTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("rating", new TableInfo.Column("rating", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("commentCount", new TableInfo.Column("commentCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("reservationUrl", new TableInfo.Column("reservationUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("facilities", new TableInfo.Column("facilities", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("tips", new TableInfo.Column("tips", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractions.put("cacheTime", new TableInfo.Column("cacheTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttractions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAttractions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAttractions = new TableInfo("attractions", _columnsAttractions, _foreignKeysAttractions, _indicesAttractions);
        final TableInfo _existingAttractions = TableInfo.read(db, "attractions");
        if (!_infoAttractions.equals(_existingAttractions)) {
          return new RoomOpenHelper.ValidationResult(false, "attractions(com.travelplanner.data.local.entity.AttractionEntity).\n"
                  + " Expected:\n" + _infoAttractions + "\n"
                  + " Found:\n" + _existingAttractions);
        }
        final HashMap<String, TableInfo.Column> _columnsRestaurants = new HashMap<String, TableInfo.Column>(18);
        _columnsRestaurants.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("city", new TableInfo.Column("city", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("avgPrice", new TableInfo.Column("avgPrice", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("rating", new TableInfo.Column("rating", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("cuisineType", new TableInfo.Column("cuisineType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("businessHours", new TableInfo.Column("businessHours", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("distance", new TableInfo.Column("distance", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("queueStatus", new TableInfo.Column("queueStatus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("waitTime", new TableInfo.Column("waitTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("specialtyDishes", new TableInfo.Column("specialtyDishes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("commentCount", new TableInfo.Column("commentCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("cacheTime", new TableInfo.Column("cacheTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRestaurants = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRestaurants = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRestaurants = new TableInfo("restaurants", _columnsRestaurants, _foreignKeysRestaurants, _indicesRestaurants);
        final TableInfo _existingRestaurants = TableInfo.read(db, "restaurants");
        if (!_infoRestaurants.equals(_existingRestaurants)) {
          return new RoomOpenHelper.ValidationResult(false, "restaurants(com.travelplanner.data.local.entity.RestaurantEntity).\n"
                  + " Expected:\n" + _infoRestaurants + "\n"
                  + " Found:\n" + _existingRestaurants);
        }
        final HashMap<String, TableInfo.Column> _columnsHotels = new HashMap<String, TableInfo.Column>(19);
        _columnsHotels.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("city", new TableInfo.Column("city", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("price", new TableInfo.Column("price", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("roomType", new TableInfo.Column("roomType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("rating", new TableInfo.Column("rating", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("facilities", new TableInfo.Column("facilities", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("distanceToCenter", new TableInfo.Column("distanceToCenter", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("nearbyAttractions", new TableInfo.Column("nearbyAttractions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("checkInTime", new TableInfo.Column("checkInTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("checkOutTime", new TableInfo.Column("checkOutTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("commentCount", new TableInfo.Column("commentCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("bookingUrl", new TableInfo.Column("bookingUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotels.put("cacheTime", new TableInfo.Column("cacheTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHotels = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHotels = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHotels = new TableInfo("hotels", _columnsHotels, _foreignKeysHotels, _indicesHotels);
        final TableInfo _existingHotels = TableInfo.read(db, "hotels");
        if (!_infoHotels.equals(_existingHotels)) {
          return new RoomOpenHelper.ValidationResult(false, "hotels(com.travelplanner.data.local.entity.HotelEntity).\n"
                  + " Expected:\n" + _infoHotels + "\n"
                  + " Found:\n" + _existingHotels);
        }
        final HashMap<String, TableInfo.Column> _columnsChecklistItems = new HashMap<String, TableInfo.Column>(9);
        _columnsChecklistItems.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("itineraryId", new TableInfo.Column("itineraryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("itemName", new TableInfo.Column("itemName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("isChecked", new TableInfo.Column("isChecked", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("createTime", new TableInfo.Column("createTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChecklistItems.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChecklistItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysChecklistItems.add(new TableInfo.ForeignKey("itineraries", "CASCADE", "NO ACTION", Arrays.asList("itineraryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesChecklistItems = new HashSet<TableInfo.Index>(1);
        _indicesChecklistItems.add(new TableInfo.Index("index_checklist_items_itineraryId", false, Arrays.asList("itineraryId"), Arrays.asList("ASC")));
        final TableInfo _infoChecklistItems = new TableInfo("checklist_items", _columnsChecklistItems, _foreignKeysChecklistItems, _indicesChecklistItems);
        final TableInfo _existingChecklistItems = TableInfo.read(db, "checklist_items");
        if (!_infoChecklistItems.equals(_existingChecklistItems)) {
          return new RoomOpenHelper.ValidationResult(false, "checklist_items(com.travelplanner.data.local.entity.ChecklistItemEntity).\n"
                  + " Expected:\n" + _infoChecklistItems + "\n"
                  + " Found:\n" + _existingChecklistItems);
        }
        final HashMap<String, TableInfo.Column> _columnsItineraries = new HashMap<String, TableInfo.Column>(15);
        _columnsItineraries.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("destination", new TableInfo.Column("destination", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("startDate", new TableInfo.Column("startDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("endDate", new TableInfo.Column("endDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("days", new TableInfo.Column("days", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("personCount", new TableInfo.Column("personCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("budget", new TableInfo.Column("budget", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("style", new TableInfo.Column("style", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("status", new TableInfo.Column("status", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("departureAddress", new TableInfo.Column("departureAddress", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("departureLatitude", new TableInfo.Column("departureLatitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("departureLongitude", new TableInfo.Column("departureLongitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("createTime", new TableInfo.Column("createTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraries.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysItineraries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesItineraries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoItineraries = new TableInfo("itineraries", _columnsItineraries, _foreignKeysItineraries, _indicesItineraries);
        final TableInfo _existingItineraries = TableInfo.read(db, "itineraries");
        if (!_infoItineraries.equals(_existingItineraries)) {
          return new RoomOpenHelper.ValidationResult(false, "itineraries(com.travelplanner.data.local.entity.ItineraryEntity).\n"
                  + " Expected:\n" + _infoItineraries + "\n"
                  + " Found:\n" + _existingItineraries);
        }
        final HashMap<String, TableInfo.Column> _columnsReminders = new HashMap<String, TableInfo.Column>(9);
        _columnsReminders.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("itineraryId", new TableInfo.Column("itineraryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("content", new TableInfo.Column("content", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("remindTime", new TableInfo.Column("remindTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("status", new TableInfo.Column("status", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("createTime", new TableInfo.Column("createTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReminders = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReminders.add(new TableInfo.ForeignKey("itineraries", "CASCADE", "NO ACTION", Arrays.asList("itineraryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReminders = new HashSet<TableInfo.Index>(1);
        _indicesReminders.add(new TableInfo.Index("index_reminders_itineraryId", false, Arrays.asList("itineraryId"), Arrays.asList("ASC")));
        final TableInfo _infoReminders = new TableInfo("reminders", _columnsReminders, _foreignKeysReminders, _indicesReminders);
        final TableInfo _existingReminders = TableInfo.read(db, "reminders");
        if (!_infoReminders.equals(_existingReminders)) {
          return new RoomOpenHelper.ValidationResult(false, "reminders(com.travelplanner.data.local.entity.ReminderEntity).\n"
                  + " Expected:\n" + _infoReminders + "\n"
                  + " Found:\n" + _existingReminders);
        }
        final HashMap<String, TableInfo.Column> _columnsItineraryDays = new HashMap<String, TableInfo.Column>(7);
        _columnsItineraryDays.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraryDays.put("itineraryId", new TableInfo.Column("itineraryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraryDays.put("dayNumber", new TableInfo.Column("dayNumber", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraryDays.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraryDays.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraryDays.put("createTime", new TableInfo.Column("createTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsItineraryDays.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysItineraryDays = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysItineraryDays.add(new TableInfo.ForeignKey("itineraries", "CASCADE", "NO ACTION", Arrays.asList("itineraryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesItineraryDays = new HashSet<TableInfo.Index>(1);
        _indicesItineraryDays.add(new TableInfo.Index("index_itinerary_days_itineraryId", false, Arrays.asList("itineraryId"), Arrays.asList("ASC")));
        final TableInfo _infoItineraryDays = new TableInfo("itinerary_days", _columnsItineraryDays, _foreignKeysItineraryDays, _indicesItineraryDays);
        final TableInfo _existingItineraryDays = TableInfo.read(db, "itinerary_days");
        if (!_infoItineraryDays.equals(_existingItineraryDays)) {
          return new RoomOpenHelper.ValidationResult(false, "itinerary_days(com.travelplanner.data.local.entity.ItineraryDayEntity).\n"
                  + " Expected:\n" + _infoItineraryDays + "\n"
                  + " Found:\n" + _existingItineraryDays);
        }
        final HashMap<String, TableInfo.Column> _columnsAttractionDays = new HashMap<String, TableInfo.Column>(9);
        _columnsAttractionDays.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("dayId", new TableInfo.Column("dayId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("attractionId", new TableInfo.Column("attractionId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("visitOrder", new TableInfo.Column("visitOrder", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("plannedStartTime", new TableInfo.Column("plannedStartTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("plannedEndTime", new TableInfo.Column("plannedEndTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("createTime", new TableInfo.Column("createTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttractionDays.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttractionDays = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysAttractionDays.add(new TableInfo.ForeignKey("itinerary_days", "CASCADE", "NO ACTION", Arrays.asList("dayId"), Arrays.asList("id")));
        _foreignKeysAttractionDays.add(new TableInfo.ForeignKey("attractions", "CASCADE", "NO ACTION", Arrays.asList("attractionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAttractionDays = new HashSet<TableInfo.Index>(2);
        _indicesAttractionDays.add(new TableInfo.Index("index_attraction_days_dayId", false, Arrays.asList("dayId"), Arrays.asList("ASC")));
        _indicesAttractionDays.add(new TableInfo.Index("index_attraction_days_attractionId", false, Arrays.asList("attractionId"), Arrays.asList("ASC")));
        final TableInfo _infoAttractionDays = new TableInfo("attraction_days", _columnsAttractionDays, _foreignKeysAttractionDays, _indicesAttractionDays);
        final TableInfo _existingAttractionDays = TableInfo.read(db, "attraction_days");
        if (!_infoAttractionDays.equals(_existingAttractionDays)) {
          return new RoomOpenHelper.ValidationResult(false, "attraction_days(com.travelplanner.data.local.entity.AttractionDayEntity).\n"
                  + " Expected:\n" + _infoAttractionDays + "\n"
                  + " Found:\n" + _existingAttractionDays);
        }
        final HashMap<String, TableInfo.Column> _columnsTransportPlans = new HashMap<String, TableInfo.Column>(17);
        _columnsTransportPlans.put("planId", new TableInfo.Column("planId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("itineraryId", new TableInfo.Column("itineraryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("dayId", new TableInfo.Column("dayId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("fromAddress", new TableInfo.Column("fromAddress", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("toAddress", new TableInfo.Column("toAddress", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("fromLatitude", new TableInfo.Column("fromLatitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("fromLongitude", new TableInfo.Column("fromLongitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("toLatitude", new TableInfo.Column("toLatitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("toLongitude", new TableInfo.Column("toLongitude", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("transportMode", new TableInfo.Column("transportMode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("distance", new TableInfo.Column("distance", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("duration", new TableInfo.Column("duration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("cost", new TableInfo.Column("cost", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("trafficStatus", new TableInfo.Column("trafficStatus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("routeData", new TableInfo.Column("routeData", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("createTime", new TableInfo.Column("createTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransportPlans.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransportPlans = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransportPlans = new HashSet<TableInfo.Index>(2);
        _indicesTransportPlans.add(new TableInfo.Index("index_transport_plans_itineraryId", false, Arrays.asList("itineraryId"), Arrays.asList("ASC")));
        _indicesTransportPlans.add(new TableInfo.Index("index_transport_plans_dayId", false, Arrays.asList("dayId"), Arrays.asList("ASC")));
        final TableInfo _infoTransportPlans = new TableInfo("transport_plans", _columnsTransportPlans, _foreignKeysTransportPlans, _indicesTransportPlans);
        final TableInfo _existingTransportPlans = TableInfo.read(db, "transport_plans");
        if (!_infoTransportPlans.equals(_existingTransportPlans)) {
          return new RoomOpenHelper.ValidationResult(false, "transport_plans(com.travelplanner.data.local.entity.TransportPlanEntity).\n"
                  + " Expected:\n" + _infoTransportPlans + "\n"
                  + " Found:\n" + _existingTransportPlans);
        }
        final HashMap<String, TableInfo.Column> _columnsFavorites = new HashMap<String, TableInfo.Column>(12);
        _columnsFavorites.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("poiId", new TableInfo.Column("poiId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("rating", new TableInfo.Column("rating", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("price", new TableInfo.Column("price", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("poiJson", new TableInfo.Column("poiJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavorites = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavorites = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavorites = new TableInfo("favorites", _columnsFavorites, _foreignKeysFavorites, _indicesFavorites);
        final TableInfo _existingFavorites = TableInfo.read(db, "favorites");
        if (!_infoFavorites.equals(_existingFavorites)) {
          return new RoomOpenHelper.ValidationResult(false, "favorites(com.travelplanner.data.local.entity.FavoriteEntity).\n"
                  + " Expected:\n" + _infoFavorites + "\n"
                  + " Found:\n" + _existingFavorites);
        }
        final HashMap<String, TableInfo.Column> _columnsTeamTodos = new HashMap<String, TableInfo.Column>(14);
        _columnsTeamTodos.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("serverId", new TableInfo.Column("serverId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("teamId", new TableInfo.Column("teamId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("content", new TableInfo.Column("content", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("priority", new TableInfo.Column("priority", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("dueDate", new TableInfo.Column("dueDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("assigneeId", new TableInfo.Column("assigneeId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("assigneeName", new TableInfo.Column("assigneeName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("creatorId", new TableInfo.Column("creatorId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("creatorName", new TableInfo.Column("creatorName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("createTime", new TableInfo.Column("createTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeamTodos.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeamTodos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTeamTodos = new HashSet<TableInfo.Index>(3);
        _indicesTeamTodos.add(new TableInfo.Index("index_team_todos_teamId", false, Arrays.asList("teamId"), Arrays.asList("ASC")));
        _indicesTeamTodos.add(new TableInfo.Index("index_team_todos_assigneeId", false, Arrays.asList("assigneeId"), Arrays.asList("ASC")));
        _indicesTeamTodos.add(new TableInfo.Index("index_team_todos_serverId", true, Arrays.asList("serverId"), Arrays.asList("ASC")));
        final TableInfo _infoTeamTodos = new TableInfo("team_todos", _columnsTeamTodos, _foreignKeysTeamTodos, _indicesTeamTodos);
        final TableInfo _existingTeamTodos = TableInfo.read(db, "team_todos");
        if (!_infoTeamTodos.equals(_existingTeamTodos)) {
          return new RoomOpenHelper.ValidationResult(false, "team_todos(com.travelplanner.data.local.entity.TodoEntity).\n"
                  + " Expected:\n" + _infoTeamTodos + "\n"
                  + " Found:\n" + _existingTeamTodos);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "91140e4ac3ea94b8a46b4ae4240bef27", "59e81f6417011377fb8a9361d0450d7b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "attractions","restaurants","hotels","checklist_items","itineraries","reminders","itinerary_days","attraction_days","transport_plans","favorites","team_todos");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `attractions`");
      _db.execSQL("DELETE FROM `restaurants`");
      _db.execSQL("DELETE FROM `hotels`");
      _db.execSQL("DELETE FROM `checklist_items`");
      _db.execSQL("DELETE FROM `itineraries`");
      _db.execSQL("DELETE FROM `reminders`");
      _db.execSQL("DELETE FROM `itinerary_days`");
      _db.execSQL("DELETE FROM `attraction_days`");
      _db.execSQL("DELETE FROM `transport_plans`");
      _db.execSQL("DELETE FROM `favorites`");
      _db.execSQL("DELETE FROM `team_todos`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AttractionDao.class, AttractionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RestaurantDao.class, RestaurantDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HotelDao.class, HotelDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChecklistDao.class, ChecklistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ItineraryDao.class, ItineraryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReminderDao.class, ReminderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ItineraryDayDao.class, ItineraryDayDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AttractionDayDao.class, AttractionDayDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransportPlanDao.class, TransportPlanDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FavoriteDao.class, FavoriteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TodoDao.class, TodoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AttractionDao attractionDao() {
    if (_attractionDao != null) {
      return _attractionDao;
    } else {
      synchronized(this) {
        if(_attractionDao == null) {
          _attractionDao = new AttractionDao_Impl(this);
        }
        return _attractionDao;
      }
    }
  }

  @Override
  public RestaurantDao restaurantDao() {
    if (_restaurantDao != null) {
      return _restaurantDao;
    } else {
      synchronized(this) {
        if(_restaurantDao == null) {
          _restaurantDao = new RestaurantDao_Impl(this);
        }
        return _restaurantDao;
      }
    }
  }

  @Override
  public HotelDao hotelDao() {
    if (_hotelDao != null) {
      return _hotelDao;
    } else {
      synchronized(this) {
        if(_hotelDao == null) {
          _hotelDao = new HotelDao_Impl(this);
        }
        return _hotelDao;
      }
    }
  }

  @Override
  public ChecklistDao checklistDao() {
    if (_checklistDao != null) {
      return _checklistDao;
    } else {
      synchronized(this) {
        if(_checklistDao == null) {
          _checklistDao = new ChecklistDao_Impl(this);
        }
        return _checklistDao;
      }
    }
  }

  @Override
  public ItineraryDao itineraryDao() {
    if (_itineraryDao != null) {
      return _itineraryDao;
    } else {
      synchronized(this) {
        if(_itineraryDao == null) {
          _itineraryDao = new ItineraryDao_Impl(this);
        }
        return _itineraryDao;
      }
    }
  }

  @Override
  public ReminderDao reminderDao() {
    if (_reminderDao != null) {
      return _reminderDao;
    } else {
      synchronized(this) {
        if(_reminderDao == null) {
          _reminderDao = new ReminderDao_Impl(this);
        }
        return _reminderDao;
      }
    }
  }

  @Override
  public ItineraryDayDao itineraryDayDao() {
    if (_itineraryDayDao != null) {
      return _itineraryDayDao;
    } else {
      synchronized(this) {
        if(_itineraryDayDao == null) {
          _itineraryDayDao = new ItineraryDayDao_Impl(this);
        }
        return _itineraryDayDao;
      }
    }
  }

  @Override
  public AttractionDayDao attractionDayDao() {
    if (_attractionDayDao != null) {
      return _attractionDayDao;
    } else {
      synchronized(this) {
        if(_attractionDayDao == null) {
          _attractionDayDao = new AttractionDayDao_Impl(this);
        }
        return _attractionDayDao;
      }
    }
  }

  @Override
  public TransportPlanDao transportPlanDao() {
    if (_transportPlanDao != null) {
      return _transportPlanDao;
    } else {
      synchronized(this) {
        if(_transportPlanDao == null) {
          _transportPlanDao = new TransportPlanDao_Impl(this);
        }
        return _transportPlanDao;
      }
    }
  }

  @Override
  public FavoriteDao favoriteDao() {
    if (_favoriteDao != null) {
      return _favoriteDao;
    } else {
      synchronized(this) {
        if(_favoriteDao == null) {
          _favoriteDao = new FavoriteDao_Impl(this);
        }
        return _favoriteDao;
      }
    }
  }

  @Override
  public TodoDao todoDao() {
    if (_todoDao != null) {
      return _todoDao;
    } else {
      synchronized(this) {
        if(_todoDao == null) {
          _todoDao = new TodoDao_Impl(this);
        }
        return _todoDao;
      }
    }
  }
}
