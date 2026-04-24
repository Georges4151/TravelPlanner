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
import com.travelplanner.data.local.entity.HotelEntity;
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
public final class HotelDao_Impl implements HotelDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HotelEntity> __insertionAdapterOfHotelEntity;

  private final EntityDeletionOrUpdateAdapter<HotelEntity> __deletionAdapterOfHotelEntity;

  private final EntityDeletionOrUpdateAdapter<HotelEntity> __updateAdapterOfHotelEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExpired;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HotelDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHotelEntity = new EntityInsertionAdapter<HotelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `hotels` (`id`,`name`,`city`,`address`,`price`,`roomType`,`rating`,`imageUrl`,`facilities`,`latitude`,`longitude`,`phone`,`distanceToCenter`,`nearbyAttractions`,`checkInTime`,`checkOutTime`,`commentCount`,`bookingUrl`,`cacheTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final HotelEntity entity) {
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
        final String _tmp = BigDecimalConverter.toString(entity.getPrice());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getRoomType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getRoomType());
        }
        if (entity.getRating() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getRating());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImageUrl());
        }
        if (entity.getFacilities() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFacilities());
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
        if (entity.getDistanceToCenter() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getDistanceToCenter());
        }
        if (entity.getNearbyAttractions() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNearbyAttractions());
        }
        if (entity.getCheckInTime() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getCheckInTime());
        }
        if (entity.getCheckOutTime() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getCheckOutTime());
        }
        if (entity.getCommentCount() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getCommentCount());
        }
        if (entity.getBookingUrl() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getBookingUrl());
        }
        if (entity.getCacheTime() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getCacheTime());
        }
      }
    };
    this.__deletionAdapterOfHotelEntity = new EntityDeletionOrUpdateAdapter<HotelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `hotels` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final HotelEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfHotelEntity = new EntityDeletionOrUpdateAdapter<HotelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `hotels` SET `id` = ?,`name` = ?,`city` = ?,`address` = ?,`price` = ?,`roomType` = ?,`rating` = ?,`imageUrl` = ?,`facilities` = ?,`latitude` = ?,`longitude` = ?,`phone` = ?,`distanceToCenter` = ?,`nearbyAttractions` = ?,`checkInTime` = ?,`checkOutTime` = ?,`commentCount` = ?,`bookingUrl` = ?,`cacheTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final HotelEntity entity) {
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
        final String _tmp = BigDecimalConverter.toString(entity.getPrice());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getRoomType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getRoomType());
        }
        if (entity.getRating() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getRating());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImageUrl());
        }
        if (entity.getFacilities() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFacilities());
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
        if (entity.getDistanceToCenter() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getDistanceToCenter());
        }
        if (entity.getNearbyAttractions() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNearbyAttractions());
        }
        if (entity.getCheckInTime() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getCheckInTime());
        }
        if (entity.getCheckOutTime() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getCheckOutTime());
        }
        if (entity.getCommentCount() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getCommentCount());
        }
        if (entity.getBookingUrl() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getBookingUrl());
        }
        if (entity.getCacheTime() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getCacheTime());
        }
        if (entity.getId() == null) {
          statement.bindNull(20);
        } else {
          statement.bindLong(20, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteExpired = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM hotels WHERE cacheTime < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM hotels";
        return _query;
      }
    };
  }

  @Override
  public void insert(final HotelEntity hotel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHotelEntity.insert(hotel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<HotelEntity> hotels) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHotelEntity.insert(hotels);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final HotelEntity hotel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfHotelEntity.handle(hotel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final HotelEntity hotel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfHotelEntity.handle(hotel);
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
  public HotelEntity getById(final Long id) {
    final String _sql = "SELECT * FROM hotels WHERE id = ?";
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
      final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
      final int _cursorIndexOfRoomType = CursorUtil.getColumnIndexOrThrow(_cursor, "roomType");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistanceToCenter = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceToCenter");
      final int _cursorIndexOfNearbyAttractions = CursorUtil.getColumnIndexOrThrow(_cursor, "nearbyAttractions");
      final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
      final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfBookingUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingUrl");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final HotelEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new HotelEntity();
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
        final BigDecimal _tmpPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfPrice);
        }
        _tmpPrice = BigDecimalConverter.fromString(_tmp);
        _result.setPrice(_tmpPrice);
        final String _tmpRoomType;
        if (_cursor.isNull(_cursorIndexOfRoomType)) {
          _tmpRoomType = null;
        } else {
          _tmpRoomType = _cursor.getString(_cursorIndexOfRoomType);
        }
        _result.setRoomType(_tmpRoomType);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _result.setRating(_tmpRating);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _result.setImageUrl(_tmpImageUrl);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _result.setFacilities(_tmpFacilities);
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
        final Integer _tmpDistanceToCenter;
        if (_cursor.isNull(_cursorIndexOfDistanceToCenter)) {
          _tmpDistanceToCenter = null;
        } else {
          _tmpDistanceToCenter = _cursor.getInt(_cursorIndexOfDistanceToCenter);
        }
        _result.setDistanceToCenter(_tmpDistanceToCenter);
        final String _tmpNearbyAttractions;
        if (_cursor.isNull(_cursorIndexOfNearbyAttractions)) {
          _tmpNearbyAttractions = null;
        } else {
          _tmpNearbyAttractions = _cursor.getString(_cursorIndexOfNearbyAttractions);
        }
        _result.setNearbyAttractions(_tmpNearbyAttractions);
        final String _tmpCheckInTime;
        if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
          _tmpCheckInTime = null;
        } else {
          _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
        }
        _result.setCheckInTime(_tmpCheckInTime);
        final String _tmpCheckOutTime;
        if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
          _tmpCheckOutTime = null;
        } else {
          _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
        }
        _result.setCheckOutTime(_tmpCheckOutTime);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _result.setCommentCount(_tmpCommentCount);
        final String _tmpBookingUrl;
        if (_cursor.isNull(_cursorIndexOfBookingUrl)) {
          _tmpBookingUrl = null;
        } else {
          _tmpBookingUrl = _cursor.getString(_cursorIndexOfBookingUrl);
        }
        _result.setBookingUrl(_tmpBookingUrl);
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
  public List<HotelEntity> getByCity(final String city) {
    final String _sql = "SELECT * FROM hotels WHERE city = ? ORDER BY rating DESC";
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
      final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
      final int _cursorIndexOfRoomType = CursorUtil.getColumnIndexOrThrow(_cursor, "roomType");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistanceToCenter = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceToCenter");
      final int _cursorIndexOfNearbyAttractions = CursorUtil.getColumnIndexOrThrow(_cursor, "nearbyAttractions");
      final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
      final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfBookingUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingUrl");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<HotelEntity> _result = new ArrayList<HotelEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final HotelEntity _item;
        _item = new HotelEntity();
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
        final BigDecimal _tmpPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfPrice);
        }
        _tmpPrice = BigDecimalConverter.fromString(_tmp);
        _item.setPrice(_tmpPrice);
        final String _tmpRoomType;
        if (_cursor.isNull(_cursorIndexOfRoomType)) {
          _tmpRoomType = null;
        } else {
          _tmpRoomType = _cursor.getString(_cursorIndexOfRoomType);
        }
        _item.setRoomType(_tmpRoomType);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _item.setFacilities(_tmpFacilities);
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
        final Integer _tmpDistanceToCenter;
        if (_cursor.isNull(_cursorIndexOfDistanceToCenter)) {
          _tmpDistanceToCenter = null;
        } else {
          _tmpDistanceToCenter = _cursor.getInt(_cursorIndexOfDistanceToCenter);
        }
        _item.setDistanceToCenter(_tmpDistanceToCenter);
        final String _tmpNearbyAttractions;
        if (_cursor.isNull(_cursorIndexOfNearbyAttractions)) {
          _tmpNearbyAttractions = null;
        } else {
          _tmpNearbyAttractions = _cursor.getString(_cursorIndexOfNearbyAttractions);
        }
        _item.setNearbyAttractions(_tmpNearbyAttractions);
        final String _tmpCheckInTime;
        if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
          _tmpCheckInTime = null;
        } else {
          _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
        }
        _item.setCheckInTime(_tmpCheckInTime);
        final String _tmpCheckOutTime;
        if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
          _tmpCheckOutTime = null;
        } else {
          _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
        }
        _item.setCheckOutTime(_tmpCheckOutTime);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final String _tmpBookingUrl;
        if (_cursor.isNull(_cursorIndexOfBookingUrl)) {
          _tmpBookingUrl = null;
        } else {
          _tmpBookingUrl = _cursor.getString(_cursorIndexOfBookingUrl);
        }
        _item.setBookingUrl(_tmpBookingUrl);
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
  public List<HotelEntity> getByPriceRange(final String city, final double minPrice,
      final double maxPrice) {
    final String _sql = "SELECT * FROM hotels WHERE city = ? AND price BETWEEN ? AND ? ORDER BY rating DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (city == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, city);
    }
    _argIndex = 2;
    _statement.bindDouble(_argIndex, minPrice);
    _argIndex = 3;
    _statement.bindDouble(_argIndex, maxPrice);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
      final int _cursorIndexOfRoomType = CursorUtil.getColumnIndexOrThrow(_cursor, "roomType");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistanceToCenter = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceToCenter");
      final int _cursorIndexOfNearbyAttractions = CursorUtil.getColumnIndexOrThrow(_cursor, "nearbyAttractions");
      final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
      final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfBookingUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingUrl");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<HotelEntity> _result = new ArrayList<HotelEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final HotelEntity _item;
        _item = new HotelEntity();
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
        final BigDecimal _tmpPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfPrice);
        }
        _tmpPrice = BigDecimalConverter.fromString(_tmp);
        _item.setPrice(_tmpPrice);
        final String _tmpRoomType;
        if (_cursor.isNull(_cursorIndexOfRoomType)) {
          _tmpRoomType = null;
        } else {
          _tmpRoomType = _cursor.getString(_cursorIndexOfRoomType);
        }
        _item.setRoomType(_tmpRoomType);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _item.setFacilities(_tmpFacilities);
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
        final Integer _tmpDistanceToCenter;
        if (_cursor.isNull(_cursorIndexOfDistanceToCenter)) {
          _tmpDistanceToCenter = null;
        } else {
          _tmpDistanceToCenter = _cursor.getInt(_cursorIndexOfDistanceToCenter);
        }
        _item.setDistanceToCenter(_tmpDistanceToCenter);
        final String _tmpNearbyAttractions;
        if (_cursor.isNull(_cursorIndexOfNearbyAttractions)) {
          _tmpNearbyAttractions = null;
        } else {
          _tmpNearbyAttractions = _cursor.getString(_cursorIndexOfNearbyAttractions);
        }
        _item.setNearbyAttractions(_tmpNearbyAttractions);
        final String _tmpCheckInTime;
        if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
          _tmpCheckInTime = null;
        } else {
          _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
        }
        _item.setCheckInTime(_tmpCheckInTime);
        final String _tmpCheckOutTime;
        if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
          _tmpCheckOutTime = null;
        } else {
          _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
        }
        _item.setCheckOutTime(_tmpCheckOutTime);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final String _tmpBookingUrl;
        if (_cursor.isNull(_cursorIndexOfBookingUrl)) {
          _tmpBookingUrl = null;
        } else {
          _tmpBookingUrl = _cursor.getString(_cursorIndexOfBookingUrl);
        }
        _item.setBookingUrl(_tmpBookingUrl);
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
  public List<HotelEntity> getNearbyCenter(final String city, final int limit) {
    final String _sql = "SELECT * FROM hotels WHERE city = ? ORDER BY distanceToCenter ASC LIMIT ?";
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
      final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
      final int _cursorIndexOfRoomType = CursorUtil.getColumnIndexOrThrow(_cursor, "roomType");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistanceToCenter = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceToCenter");
      final int _cursorIndexOfNearbyAttractions = CursorUtil.getColumnIndexOrThrow(_cursor, "nearbyAttractions");
      final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
      final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfBookingUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingUrl");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<HotelEntity> _result = new ArrayList<HotelEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final HotelEntity _item;
        _item = new HotelEntity();
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
        final BigDecimal _tmpPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfPrice);
        }
        _tmpPrice = BigDecimalConverter.fromString(_tmp);
        _item.setPrice(_tmpPrice);
        final String _tmpRoomType;
        if (_cursor.isNull(_cursorIndexOfRoomType)) {
          _tmpRoomType = null;
        } else {
          _tmpRoomType = _cursor.getString(_cursorIndexOfRoomType);
        }
        _item.setRoomType(_tmpRoomType);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _item.setFacilities(_tmpFacilities);
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
        final Integer _tmpDistanceToCenter;
        if (_cursor.isNull(_cursorIndexOfDistanceToCenter)) {
          _tmpDistanceToCenter = null;
        } else {
          _tmpDistanceToCenter = _cursor.getInt(_cursorIndexOfDistanceToCenter);
        }
        _item.setDistanceToCenter(_tmpDistanceToCenter);
        final String _tmpNearbyAttractions;
        if (_cursor.isNull(_cursorIndexOfNearbyAttractions)) {
          _tmpNearbyAttractions = null;
        } else {
          _tmpNearbyAttractions = _cursor.getString(_cursorIndexOfNearbyAttractions);
        }
        _item.setNearbyAttractions(_tmpNearbyAttractions);
        final String _tmpCheckInTime;
        if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
          _tmpCheckInTime = null;
        } else {
          _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
        }
        _item.setCheckInTime(_tmpCheckInTime);
        final String _tmpCheckOutTime;
        if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
          _tmpCheckOutTime = null;
        } else {
          _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
        }
        _item.setCheckOutTime(_tmpCheckOutTime);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final String _tmpBookingUrl;
        if (_cursor.isNull(_cursorIndexOfBookingUrl)) {
          _tmpBookingUrl = null;
        } else {
          _tmpBookingUrl = _cursor.getString(_cursorIndexOfBookingUrl);
        }
        _item.setBookingUrl(_tmpBookingUrl);
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
  public List<HotelEntity> search(final String keyword) {
    final String _sql = "SELECT * FROM hotels WHERE name LIKE '%' || ? || '%' OR city LIKE '%' || ? || '%'";
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
      final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
      final int _cursorIndexOfRoomType = CursorUtil.getColumnIndexOrThrow(_cursor, "roomType");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfDistanceToCenter = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceToCenter");
      final int _cursorIndexOfNearbyAttractions = CursorUtil.getColumnIndexOrThrow(_cursor, "nearbyAttractions");
      final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
      final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfBookingUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookingUrl");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<HotelEntity> _result = new ArrayList<HotelEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final HotelEntity _item;
        _item = new HotelEntity();
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
        final BigDecimal _tmpPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfPrice);
        }
        _tmpPrice = BigDecimalConverter.fromString(_tmp);
        _item.setPrice(_tmpPrice);
        final String _tmpRoomType;
        if (_cursor.isNull(_cursorIndexOfRoomType)) {
          _tmpRoomType = null;
        } else {
          _tmpRoomType = _cursor.getString(_cursorIndexOfRoomType);
        }
        _item.setRoomType(_tmpRoomType);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _item.setFacilities(_tmpFacilities);
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
        final Integer _tmpDistanceToCenter;
        if (_cursor.isNull(_cursorIndexOfDistanceToCenter)) {
          _tmpDistanceToCenter = null;
        } else {
          _tmpDistanceToCenter = _cursor.getInt(_cursorIndexOfDistanceToCenter);
        }
        _item.setDistanceToCenter(_tmpDistanceToCenter);
        final String _tmpNearbyAttractions;
        if (_cursor.isNull(_cursorIndexOfNearbyAttractions)) {
          _tmpNearbyAttractions = null;
        } else {
          _tmpNearbyAttractions = _cursor.getString(_cursorIndexOfNearbyAttractions);
        }
        _item.setNearbyAttractions(_tmpNearbyAttractions);
        final String _tmpCheckInTime;
        if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
          _tmpCheckInTime = null;
        } else {
          _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
        }
        _item.setCheckInTime(_tmpCheckInTime);
        final String _tmpCheckOutTime;
        if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
          _tmpCheckOutTime = null;
        } else {
          _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
        }
        _item.setCheckOutTime(_tmpCheckOutTime);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
        final String _tmpBookingUrl;
        if (_cursor.isNull(_cursorIndexOfBookingUrl)) {
          _tmpBookingUrl = null;
        } else {
          _tmpBookingUrl = _cursor.getString(_cursorIndexOfBookingUrl);
        }
        _item.setBookingUrl(_tmpBookingUrl);
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
