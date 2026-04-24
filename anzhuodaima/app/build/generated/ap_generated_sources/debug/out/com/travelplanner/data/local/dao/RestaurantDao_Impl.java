package com.travelplanner.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.travelplanner.data.local.BigDecimalConverter;
import com.travelplanner.data.local.entity.RestaurantEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantDao_Impl implements RestaurantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RestaurantEntity> __insertionAdapterOfRestaurantEntity;

  private final EntityDeletionOrUpdateAdapter<RestaurantEntity> __deletionAdapterOfRestaurantEntity;

  private final EntityDeletionOrUpdateAdapter<RestaurantEntity> __updateAdapterOfRestaurantEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExpired;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RestaurantDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestaurantEntity = new EntityInsertionAdapter<RestaurantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `restaurants` (`id`,`name`,`city`,`address`,`avgPrice`,`rating`,`cuisineType`,`imageUrl`,`businessHours`,`latitude`,`longitude`,`phone`,`distance`,`queueStatus`,`waitTime`,`specialtyDishes`,`commentCount`,`cacheTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RestaurantEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getCity() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCity());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAddress());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getAvgPrice());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getRating() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getRating());
        }
        if (entity.getCuisineType() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCuisineType());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImageUrl());
        }
        if (entity.getBusinessHours() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getBusinessHours());
        }
        if (entity.getLatitude() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getLongitude());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPhone());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getDistance());
        }
        if (entity.getQueueStatus() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getQueueStatus());
        }
        if (entity.getWaitTime() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getWaitTime());
        }
        if (entity.getSpecialtyDishes() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getSpecialtyDishes());
        }
        if (entity.getCommentCount() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getCommentCount());
        }
        if (entity.getCacheTime() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getCacheTime());
        }
      }
    };
    this.__deletionAdapterOfRestaurantEntity = new EntityDeletionOrUpdateAdapter<RestaurantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `restaurants` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RestaurantEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfRestaurantEntity = new EntityDeletionOrUpdateAdapter<RestaurantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `restaurants` SET `id` = ?,`name` = ?,`city` = ?,`address` = ?,`avgPrice` = ?,`rating` = ?,`cuisineType` = ?,`imageUrl` = ?,`businessHours` = ?,`latitude` = ?,`longitude` = ?,`phone` = ?,`distance` = ?,`queueStatus` = ?,`waitTime` = ?,`specialtyDishes` = ?,`commentCount` = ?,`cacheTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RestaurantEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getCity() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCity());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAddress());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getAvgPrice());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getRating() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getRating());
        }
        if (entity.getCuisineType() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCuisineType());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImageUrl());
        }
        if (entity.getBusinessHours() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getBusinessHours());
        }
        if (entity.getLatitude() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getLongitude());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPhone());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getDistance());
        }
        if (entity.getQueueStatus() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getQueueStatus());
        }
        if (entity.getWaitTime() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getWaitTime());
        }
        if (entity.getSpecialtyDishes() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getSpecialtyDishes());
        }
        if (entity.getCommentCount() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getCommentCount());
        }
        if (entity.getCacheTime() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getCacheTime());
        }
        if (entity.getId() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteExpired = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM restaurants WHERE cacheTime < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM restaurants";
        return _query;
      }
    };
  }

  @Override
  public void insert(final RestaurantEntity restaurant) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRestaurantEntity.insert(restaurant);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<RestaurantEntity> restaurants) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRestaurantEntity.insert(restaurants);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final RestaurantEntity restaurant) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfRestaurantEntity.handle(restaurant);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final RestaurantEntity restaurant) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfRestaurantEntity.handle(restaurant);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteExpired(final long expireTime) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExpired.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, expireTime);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteExpired.release(_stmt);
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public RestaurantEntity getById(final Long id) {
    final String _sql = "SELECT * FROM restaurants WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfAvgPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "avgPrice");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCuisineType = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisineType");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfBusinessHours = CursorUtil.getColumnIndexOrThrow(_cursor, "businessHours");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfQueueStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "queueStatus");
      final int _cursorIndexOfWaitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "waitTime");
      final int _cursorIndexOfSpecialtyDishes = CursorUtil.getColumnIndexOrThrow(_cursor, "specialtyDishes");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final RestaurantEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new RestaurantEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpCity;
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _tmpCity = null;
        } else {
          _tmpCity = _cursor.getString(_cursorIndexOfCity);
        }
        _result.setCity(_tmpCity);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _result.setAddress(_tmpAddress);
        final BigDecimal _tmpAvgPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfAvgPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfAvgPrice);
        }
        _tmpAvgPrice = BigDecimalConverter.fromString(_tmp);
        _result.setAvgPrice(_tmpAvgPrice);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _result.setRating(_tmpRating);
        final String _tmpCuisineType;
        if (_cursor.isNull(_cursorIndexOfCuisineType)) {
          _tmpCuisineType = null;
        } else {
          _tmpCuisineType = _cursor.getString(_cursorIndexOfCuisineType);
        }
        _result.setCuisineType(_tmpCuisineType);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _result.setImageUrl(_tmpImageUrl);
        final String _tmpBusinessHours;
        if (_cursor.isNull(_cursorIndexOfBusinessHours)) {
          _tmpBusinessHours = null;
        } else {
          _tmpBusinessHours = _cursor.getString(_cursorIndexOfBusinessHours);
        }
        _result.setBusinessHours(_tmpBusinessHours);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _result.setLatitude(_tmpLatitude);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        _result.setLongitude(_tmpLongitude);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final Integer _tmpDistance;
        if (_cursor.isNull(_cursorIndexOfDistance)) {
          _tmpDistance = null;
        } else {
          _tmpDistance = _cursor.getInt(_cursorIndexOfDistance);
        }
        _result.setDistance(_tmpDistance);
        final String _tmpQueueStatus;
        if (_cursor.isNull(_cursorIndexOfQueueStatus)) {
          _tmpQueueStatus = null;
        } else {
          _tmpQueueStatus = _cursor.getString(_cursorIndexOfQueueStatus);
        }
        _result.setQueueStatus(_tmpQueueStatus);
        final Integer _tmpWaitTime;
        if (_cursor.isNull(_cursorIndexOfWaitTime)) {
          _tmpWaitTime = null;
        } else {
          _tmpWaitTime = _cursor.getInt(_cursorIndexOfWaitTime);
        }
        _result.setWaitTime(_tmpWaitTime);
        final String _tmpSpecialtyDishes;
        if (_cursor.isNull(_cursorIndexOfSpecialtyDishes)) {
          _tmpSpecialtyDishes = null;
        } else {
          _tmpSpecialtyDishes = _cursor.getString(_cursorIndexOfSpecialtyDishes);
        }
        _result.setSpecialtyDishes(_tmpSpecialtyDishes);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _result.setCommentCount(_tmpCommentCount);
        final Long _tmpCacheTime;
        if (_cursor.isNull(_cursorIndexOfCacheTime)) {
          _tmpCacheTime = null;
        } else {
          _tmpCacheTime = _cursor.getLong(_cursorIndexOfCacheTime);
        }
        _result.setCacheTime(_tmpCacheTime);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RestaurantEntity> getByCity(final String city) {
    final String _sql = "SELECT * FROM restaurants WHERE city = ? ORDER BY rating DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (city == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, city);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfAvgPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "avgPrice");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCuisineType = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisineType");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfBusinessHours = CursorUtil.getColumnIndexOrThrow(_cursor, "businessHours");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfQueueStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "queueStatus");
      final int _cursorIndexOfWaitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "waitTime");
      final int _cursorIndexOfSpecialtyDishes = CursorUtil.getColumnIndexOrThrow(_cursor, "specialtyDishes");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<RestaurantEntity> _result = new ArrayList<RestaurantEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RestaurantEntity _item;
        _item = new RestaurantEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpCity;
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _tmpCity = null;
        } else {
          _tmpCity = _cursor.getString(_cursorIndexOfCity);
        }
        _item.setCity(_tmpCity);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final BigDecimal _tmpAvgPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfAvgPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfAvgPrice);
        }
        _tmpAvgPrice = BigDecimalConverter.fromString(_tmp);
        _item.setAvgPrice(_tmpAvgPrice);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpCuisineType;
        if (_cursor.isNull(_cursorIndexOfCuisineType)) {
          _tmpCuisineType = null;
        } else {
          _tmpCuisineType = _cursor.getString(_cursorIndexOfCuisineType);
        }
        _item.setCuisineType(_tmpCuisineType);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpBusinessHours;
        if (_cursor.isNull(_cursorIndexOfBusinessHours)) {
          _tmpBusinessHours = null;
        } else {
          _tmpBusinessHours = _cursor.getString(_cursorIndexOfBusinessHours);
        }
        _item.setBusinessHours(_tmpBusinessHours);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final Integer _tmpDistance;
        if (_cursor.isNull(_cursorIndexOfDistance)) {
          _tmpDistance = null;
        } else {
          _tmpDistance = _cursor.getInt(_cursorIndexOfDistance);
        }
        _item.setDistance(_tmpDistance);
        final String _tmpQueueStatus;
        if (_cursor.isNull(_cursorIndexOfQueueStatus)) {
          _tmpQueueStatus = null;
        } else {
          _tmpQueueStatus = _cursor.getString(_cursorIndexOfQueueStatus);
        }
        _item.setQueueStatus(_tmpQueueStatus);
        final Integer _tmpWaitTime;
        if (_cursor.isNull(_cursorIndexOfWaitTime)) {
          _tmpWaitTime = null;
        } else {
          _tmpWaitTime = _cursor.getInt(_cursorIndexOfWaitTime);
        }
        _item.setWaitTime(_tmpWaitTime);
        final String _tmpSpecialtyDishes;
        if (_cursor.isNull(_cursorIndexOfSpecialtyDishes)) {
          _tmpSpecialtyDishes = null;
        } else {
          _tmpSpecialtyDishes = _cursor.getString(_cursorIndexOfSpecialtyDishes);
        }
        _item.setSpecialtyDishes(_tmpSpecialtyDishes);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final Long _tmpCacheTime;
        if (_cursor.isNull(_cursorIndexOfCacheTime)) {
          _tmpCacheTime = null;
        } else {
          _tmpCacheTime = _cursor.getLong(_cursorIndexOfCacheTime);
        }
        _item.setCacheTime(_tmpCacheTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RestaurantEntity> getByCityAndCuisine(final String city, final String cuisineType) {
    final String _sql = "SELECT * FROM restaurants WHERE city = ? AND cuisineType = ? ORDER BY rating DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (city == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, city);
    }
    _argIndex = 2;
    if (cuisineType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, cuisineType);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfAvgPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "avgPrice");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCuisineType = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisineType");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfBusinessHours = CursorUtil.getColumnIndexOrThrow(_cursor, "businessHours");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfQueueStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "queueStatus");
      final int _cursorIndexOfWaitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "waitTime");
      final int _cursorIndexOfSpecialtyDishes = CursorUtil.getColumnIndexOrThrow(_cursor, "specialtyDishes");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<RestaurantEntity> _result = new ArrayList<RestaurantEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RestaurantEntity _item;
        _item = new RestaurantEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpCity;
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _tmpCity = null;
        } else {
          _tmpCity = _cursor.getString(_cursorIndexOfCity);
        }
        _item.setCity(_tmpCity);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final BigDecimal _tmpAvgPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfAvgPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfAvgPrice);
        }
        _tmpAvgPrice = BigDecimalConverter.fromString(_tmp);
        _item.setAvgPrice(_tmpAvgPrice);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpCuisineType;
        if (_cursor.isNull(_cursorIndexOfCuisineType)) {
          _tmpCuisineType = null;
        } else {
          _tmpCuisineType = _cursor.getString(_cursorIndexOfCuisineType);
        }
        _item.setCuisineType(_tmpCuisineType);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpBusinessHours;
        if (_cursor.isNull(_cursorIndexOfBusinessHours)) {
          _tmpBusinessHours = null;
        } else {
          _tmpBusinessHours = _cursor.getString(_cursorIndexOfBusinessHours);
        }
        _item.setBusinessHours(_tmpBusinessHours);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final Integer _tmpDistance;
        if (_cursor.isNull(_cursorIndexOfDistance)) {
          _tmpDistance = null;
        } else {
          _tmpDistance = _cursor.getInt(_cursorIndexOfDistance);
        }
        _item.setDistance(_tmpDistance);
        final String _tmpQueueStatus;
        if (_cursor.isNull(_cursorIndexOfQueueStatus)) {
          _tmpQueueStatus = null;
        } else {
          _tmpQueueStatus = _cursor.getString(_cursorIndexOfQueueStatus);
        }
        _item.setQueueStatus(_tmpQueueStatus);
        final Integer _tmpWaitTime;
        if (_cursor.isNull(_cursorIndexOfWaitTime)) {
          _tmpWaitTime = null;
        } else {
          _tmpWaitTime = _cursor.getInt(_cursorIndexOfWaitTime);
        }
        _item.setWaitTime(_tmpWaitTime);
        final String _tmpSpecialtyDishes;
        if (_cursor.isNull(_cursorIndexOfSpecialtyDishes)) {
          _tmpSpecialtyDishes = null;
        } else {
          _tmpSpecialtyDishes = _cursor.getString(_cursorIndexOfSpecialtyDishes);
        }
        _item.setSpecialtyDishes(_tmpSpecialtyDishes);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final Long _tmpCacheTime;
        if (_cursor.isNull(_cursorIndexOfCacheTime)) {
          _tmpCacheTime = null;
        } else {
          _tmpCacheTime = _cursor.getLong(_cursorIndexOfCacheTime);
        }
        _item.setCacheTime(_tmpCacheTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RestaurantEntity> getNearby(final String city, final int limit) {
    final String _sql = "SELECT * FROM restaurants WHERE city = ? ORDER BY distance ASC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (city == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, city);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfAvgPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "avgPrice");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCuisineType = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisineType");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfBusinessHours = CursorUtil.getColumnIndexOrThrow(_cursor, "businessHours");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfQueueStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "queueStatus");
      final int _cursorIndexOfWaitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "waitTime");
      final int _cursorIndexOfSpecialtyDishes = CursorUtil.getColumnIndexOrThrow(_cursor, "specialtyDishes");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<RestaurantEntity> _result = new ArrayList<RestaurantEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RestaurantEntity _item;
        _item = new RestaurantEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpCity;
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _tmpCity = null;
        } else {
          _tmpCity = _cursor.getString(_cursorIndexOfCity);
        }
        _item.setCity(_tmpCity);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final BigDecimal _tmpAvgPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfAvgPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfAvgPrice);
        }
        _tmpAvgPrice = BigDecimalConverter.fromString(_tmp);
        _item.setAvgPrice(_tmpAvgPrice);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpCuisineType;
        if (_cursor.isNull(_cursorIndexOfCuisineType)) {
          _tmpCuisineType = null;
        } else {
          _tmpCuisineType = _cursor.getString(_cursorIndexOfCuisineType);
        }
        _item.setCuisineType(_tmpCuisineType);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpBusinessHours;
        if (_cursor.isNull(_cursorIndexOfBusinessHours)) {
          _tmpBusinessHours = null;
        } else {
          _tmpBusinessHours = _cursor.getString(_cursorIndexOfBusinessHours);
        }
        _item.setBusinessHours(_tmpBusinessHours);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final Integer _tmpDistance;
        if (_cursor.isNull(_cursorIndexOfDistance)) {
          _tmpDistance = null;
        } else {
          _tmpDistance = _cursor.getInt(_cursorIndexOfDistance);
        }
        _item.setDistance(_tmpDistance);
        final String _tmpQueueStatus;
        if (_cursor.isNull(_cursorIndexOfQueueStatus)) {
          _tmpQueueStatus = null;
        } else {
          _tmpQueueStatus = _cursor.getString(_cursorIndexOfQueueStatus);
        }
        _item.setQueueStatus(_tmpQueueStatus);
        final Integer _tmpWaitTime;
        if (_cursor.isNull(_cursorIndexOfWaitTime)) {
          _tmpWaitTime = null;
        } else {
          _tmpWaitTime = _cursor.getInt(_cursorIndexOfWaitTime);
        }
        _item.setWaitTime(_tmpWaitTime);
        final String _tmpSpecialtyDishes;
        if (_cursor.isNull(_cursorIndexOfSpecialtyDishes)) {
          _tmpSpecialtyDishes = null;
        } else {
          _tmpSpecialtyDishes = _cursor.getString(_cursorIndexOfSpecialtyDishes);
        }
        _item.setSpecialtyDishes(_tmpSpecialtyDishes);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final Long _tmpCacheTime;
        if (_cursor.isNull(_cursorIndexOfCacheTime)) {
          _tmpCacheTime = null;
        } else {
          _tmpCacheTime = _cursor.getLong(_cursorIndexOfCacheTime);
        }
        _item.setCacheTime(_tmpCacheTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RestaurantEntity> search(final String keyword) {
    final String _sql = "SELECT * FROM restaurants WHERE name LIKE '%' || ? || '%' OR city LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    _argIndex = 2;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfAvgPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "avgPrice");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCuisineType = CursorUtil.getColumnIndexOrThrow(_cursor, "cuisineType");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfBusinessHours = CursorUtil.getColumnIndexOrThrow(_cursor, "businessHours");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfQueueStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "queueStatus");
      final int _cursorIndexOfWaitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "waitTime");
      final int _cursorIndexOfSpecialtyDishes = CursorUtil.getColumnIndexOrThrow(_cursor, "specialtyDishes");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<RestaurantEntity> _result = new ArrayList<RestaurantEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RestaurantEntity _item;
        _item = new RestaurantEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpCity;
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _tmpCity = null;
        } else {
          _tmpCity = _cursor.getString(_cursorIndexOfCity);
        }
        _item.setCity(_tmpCity);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final BigDecimal _tmpAvgPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfAvgPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfAvgPrice);
        }
        _tmpAvgPrice = BigDecimalConverter.fromString(_tmp);
        _item.setAvgPrice(_tmpAvgPrice);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpCuisineType;
        if (_cursor.isNull(_cursorIndexOfCuisineType)) {
          _tmpCuisineType = null;
        } else {
          _tmpCuisineType = _cursor.getString(_cursorIndexOfCuisineType);
        }
        _item.setCuisineType(_tmpCuisineType);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpBusinessHours;
        if (_cursor.isNull(_cursorIndexOfBusinessHours)) {
          _tmpBusinessHours = null;
        } else {
          _tmpBusinessHours = _cursor.getString(_cursorIndexOfBusinessHours);
        }
        _item.setBusinessHours(_tmpBusinessHours);
        final Double _tmpLatitude;
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _tmpLatitude = null;
        } else {
          _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        _item.setLatitude(_tmpLatitude);
        final Double _tmpLongitude;
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _tmpLongitude = null;
        } else {
          _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        _item.setLongitude(_tmpLongitude);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final Integer _tmpDistance;
        if (_cursor.isNull(_cursorIndexOfDistance)) {
          _tmpDistance = null;
        } else {
          _tmpDistance = _cursor.getInt(_cursorIndexOfDistance);
        }
        _item.setDistance(_tmpDistance);
        final String _tmpQueueStatus;
        if (_cursor.isNull(_cursorIndexOfQueueStatus)) {
          _tmpQueueStatus = null;
        } else {
          _tmpQueueStatus = _cursor.getString(_cursorIndexOfQueueStatus);
        }
        _item.setQueueStatus(_tmpQueueStatus);
        final Integer _tmpWaitTime;
        if (_cursor.isNull(_cursorIndexOfWaitTime)) {
          _tmpWaitTime = null;
        } else {
          _tmpWaitTime = _cursor.getInt(_cursorIndexOfWaitTime);
        }
        _item.setWaitTime(_tmpWaitTime);
        final String _tmpSpecialtyDishes;
        if (_cursor.isNull(_cursorIndexOfSpecialtyDishes)) {
          _tmpSpecialtyDishes = null;
        } else {
          _tmpSpecialtyDishes = _cursor.getString(_cursorIndexOfSpecialtyDishes);
        }
        _item.setSpecialtyDishes(_tmpSpecialtyDishes);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final Long _tmpCacheTime;
        if (_cursor.isNull(_cursorIndexOfCacheTime)) {
          _tmpCacheTime = null;
        } else {
          _tmpCacheTime = _cursor.getLong(_cursorIndexOfCacheTime);
        }
        _item.setCacheTime(_tmpCacheTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
