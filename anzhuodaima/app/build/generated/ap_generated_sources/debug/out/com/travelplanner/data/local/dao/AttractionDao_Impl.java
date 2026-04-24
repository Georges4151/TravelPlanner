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
import com.travelplanner.data.local.entity.AttractionEntity;
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
public final class AttractionDao_Impl implements AttractionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AttractionEntity> __insertionAdapterOfAttractionEntity;

  private final EntityDeletionOrUpdateAdapter<AttractionEntity> __deletionAdapterOfAttractionEntity;

  private final EntityDeletionOrUpdateAdapter<AttractionEntity> __updateAdapterOfAttractionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExpired;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AttractionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAttractionEntity = new EntityInsertionAdapter<AttractionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `attractions` (`id`,`name`,`city`,`province`,`description`,`address`,`openTime`,`ticketPrice`,`playDuration`,`crowdLevel`,`crowdIndex`,`bestVisitTime`,`imageUrl`,`rating`,`commentCount`,`latitude`,`longitude`,`reservationUrl`,`phone`,`facilities`,`tips`,`cacheTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AttractionEntity entity) {
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
        if (entity.getProvince() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getProvince());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAddress());
        }
        if (entity.getOpenTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getOpenTime());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getTicketPrice());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        if (entity.getPlayDuration() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getPlayDuration());
        }
        if (entity.getCrowdLevel() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getCrowdLevel());
        }
        if (entity.getCrowdIndex() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCrowdIndex());
        }
        if (entity.getBestVisitTime() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getBestVisitTime());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getImageUrl());
        }
        if (entity.getRating() == null) {
          statement.bindNull(14);
        } else {
          statement.bindDouble(14, entity.getRating());
        }
        if (entity.getCommentCount() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getCommentCount());
        }
        if (entity.getLatitude() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getLongitude());
        }
        if (entity.getReservationUrl() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getReservationUrl());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getPhone());
        }
        if (entity.getFacilities() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getFacilities());
        }
        if (entity.getTips() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getTips());
        }
        if (entity.getCacheTime() == null) {
          statement.bindNull(22);
        } else {
          statement.bindLong(22, entity.getCacheTime());
        }
      }
    };
    this.__deletionAdapterOfAttractionEntity = new EntityDeletionOrUpdateAdapter<AttractionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `attractions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AttractionEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfAttractionEntity = new EntityDeletionOrUpdateAdapter<AttractionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `attractions` SET `id` = ?,`name` = ?,`city` = ?,`province` = ?,`description` = ?,`address` = ?,`openTime` = ?,`ticketPrice` = ?,`playDuration` = ?,`crowdLevel` = ?,`crowdIndex` = ?,`bestVisitTime` = ?,`imageUrl` = ?,`rating` = ?,`commentCount` = ?,`latitude` = ?,`longitude` = ?,`reservationUrl` = ?,`phone` = ?,`facilities` = ?,`tips` = ?,`cacheTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AttractionEntity entity) {
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
        if (entity.getProvince() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getProvince());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDescription());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAddress());
        }
        if (entity.getOpenTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getOpenTime());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getTicketPrice());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        if (entity.getPlayDuration() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getPlayDuration());
        }
        if (entity.getCrowdLevel() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getCrowdLevel());
        }
        if (entity.getCrowdIndex() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCrowdIndex());
        }
        if (entity.getBestVisitTime() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getBestVisitTime());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getImageUrl());
        }
        if (entity.getRating() == null) {
          statement.bindNull(14);
        } else {
          statement.bindDouble(14, entity.getRating());
        }
        if (entity.getCommentCount() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getCommentCount());
        }
        if (entity.getLatitude() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getLongitude());
        }
        if (entity.getReservationUrl() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getReservationUrl());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getPhone());
        }
        if (entity.getFacilities() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getFacilities());
        }
        if (entity.getTips() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getTips());
        }
        if (entity.getCacheTime() == null) {
          statement.bindNull(22);
        } else {
          statement.bindLong(22, entity.getCacheTime());
        }
        if (entity.getId() == null) {
          statement.bindNull(23);
        } else {
          statement.bindLong(23, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteExpired = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attractions WHERE cacheTime < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attractions";
        return _query;
      }
    };
  }

  @Override
  public long insert(final AttractionEntity attraction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfAttractionEntity.insertAndReturnId(attraction);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<AttractionEntity> attractions) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAttractionEntity.insert(attractions);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final AttractionEntity attraction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAttractionEntity.handle(attraction);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final AttractionEntity attraction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAttractionEntity.handle(attraction);
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
  public AttractionEntity getById(final Long id) {
    final String _sql = "SELECT * FROM attractions WHERE id = ?";
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
      final int _cursorIndexOfProvince = CursorUtil.getColumnIndexOrThrow(_cursor, "province");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfOpenTime = CursorUtil.getColumnIndexOrThrow(_cursor, "openTime");
      final int _cursorIndexOfTicketPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketPrice");
      final int _cursorIndexOfPlayDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "playDuration");
      final int _cursorIndexOfCrowdLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdLevel");
      final int _cursorIndexOfCrowdIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdIndex");
      final int _cursorIndexOfBestVisitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bestVisitTime");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfReservationUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationUrl");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final AttractionEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new AttractionEntity();
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
        final String _tmpProvince;
        if (_cursor.isNull(_cursorIndexOfProvince)) {
          _tmpProvince = null;
        } else {
          _tmpProvince = _cursor.getString(_cursorIndexOfProvince);
        }
        _result.setProvince(_tmpProvince);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _result.setDescription(_tmpDescription);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _result.setAddress(_tmpAddress);
        final String _tmpOpenTime;
        if (_cursor.isNull(_cursorIndexOfOpenTime)) {
          _tmpOpenTime = null;
        } else {
          _tmpOpenTime = _cursor.getString(_cursorIndexOfOpenTime);
        }
        _result.setOpenTime(_tmpOpenTime);
        final BigDecimal _tmpTicketPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfTicketPrice);
        }
        _tmpTicketPrice = BigDecimalConverter.fromString(_tmp);
        _result.setTicketPrice(_tmpTicketPrice);
        final Integer _tmpPlayDuration;
        if (_cursor.isNull(_cursorIndexOfPlayDuration)) {
          _tmpPlayDuration = null;
        } else {
          _tmpPlayDuration = _cursor.getInt(_cursorIndexOfPlayDuration);
        }
        _result.setPlayDuration(_tmpPlayDuration);
        final String _tmpCrowdLevel;
        if (_cursor.isNull(_cursorIndexOfCrowdLevel)) {
          _tmpCrowdLevel = null;
        } else {
          _tmpCrowdLevel = _cursor.getString(_cursorIndexOfCrowdLevel);
        }
        _result.setCrowdLevel(_tmpCrowdLevel);
        final String _tmpCrowdIndex;
        if (_cursor.isNull(_cursorIndexOfCrowdIndex)) {
          _tmpCrowdIndex = null;
        } else {
          _tmpCrowdIndex = _cursor.getString(_cursorIndexOfCrowdIndex);
        }
        _result.setCrowdIndex(_tmpCrowdIndex);
        final String _tmpBestVisitTime;
        if (_cursor.isNull(_cursorIndexOfBestVisitTime)) {
          _tmpBestVisitTime = null;
        } else {
          _tmpBestVisitTime = _cursor.getString(_cursorIndexOfBestVisitTime);
        }
        _result.setBestVisitTime(_tmpBestVisitTime);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _result.setImageUrl(_tmpImageUrl);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _result.setRating(_tmpRating);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _result.setCommentCount(_tmpCommentCount);
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
        final String _tmpReservationUrl;
        if (_cursor.isNull(_cursorIndexOfReservationUrl)) {
          _tmpReservationUrl = null;
        } else {
          _tmpReservationUrl = _cursor.getString(_cursorIndexOfReservationUrl);
        }
        _result.setReservationUrl(_tmpReservationUrl);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _result.setPhone(_tmpPhone);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _result.setFacilities(_tmpFacilities);
        final String _tmpTips;
        if (_cursor.isNull(_cursorIndexOfTips)) {
          _tmpTips = null;
        } else {
          _tmpTips = _cursor.getString(_cursorIndexOfTips);
        }
        _result.setTips(_tmpTips);
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
  public List<AttractionEntity> getByCity(final String city) {
    final String _sql = "SELECT * FROM attractions WHERE city = ? ORDER BY rating DESC";
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
      final int _cursorIndexOfProvince = CursorUtil.getColumnIndexOrThrow(_cursor, "province");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfOpenTime = CursorUtil.getColumnIndexOrThrow(_cursor, "openTime");
      final int _cursorIndexOfTicketPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketPrice");
      final int _cursorIndexOfPlayDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "playDuration");
      final int _cursorIndexOfCrowdLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdLevel");
      final int _cursorIndexOfCrowdIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdIndex");
      final int _cursorIndexOfBestVisitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bestVisitTime");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfReservationUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationUrl");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<AttractionEntity> _result = new ArrayList<AttractionEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AttractionEntity _item;
        _item = new AttractionEntity();
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
        final String _tmpProvince;
        if (_cursor.isNull(_cursorIndexOfProvince)) {
          _tmpProvince = null;
        } else {
          _tmpProvince = _cursor.getString(_cursorIndexOfProvince);
        }
        _item.setProvince(_tmpProvince);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final String _tmpOpenTime;
        if (_cursor.isNull(_cursorIndexOfOpenTime)) {
          _tmpOpenTime = null;
        } else {
          _tmpOpenTime = _cursor.getString(_cursorIndexOfOpenTime);
        }
        _item.setOpenTime(_tmpOpenTime);
        final BigDecimal _tmpTicketPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfTicketPrice);
        }
        _tmpTicketPrice = BigDecimalConverter.fromString(_tmp);
        _item.setTicketPrice(_tmpTicketPrice);
        final Integer _tmpPlayDuration;
        if (_cursor.isNull(_cursorIndexOfPlayDuration)) {
          _tmpPlayDuration = null;
        } else {
          _tmpPlayDuration = _cursor.getInt(_cursorIndexOfPlayDuration);
        }
        _item.setPlayDuration(_tmpPlayDuration);
        final String _tmpCrowdLevel;
        if (_cursor.isNull(_cursorIndexOfCrowdLevel)) {
          _tmpCrowdLevel = null;
        } else {
          _tmpCrowdLevel = _cursor.getString(_cursorIndexOfCrowdLevel);
        }
        _item.setCrowdLevel(_tmpCrowdLevel);
        final String _tmpCrowdIndex;
        if (_cursor.isNull(_cursorIndexOfCrowdIndex)) {
          _tmpCrowdIndex = null;
        } else {
          _tmpCrowdIndex = _cursor.getString(_cursorIndexOfCrowdIndex);
        }
        _item.setCrowdIndex(_tmpCrowdIndex);
        final String _tmpBestVisitTime;
        if (_cursor.isNull(_cursorIndexOfBestVisitTime)) {
          _tmpBestVisitTime = null;
        } else {
          _tmpBestVisitTime = _cursor.getString(_cursorIndexOfBestVisitTime);
        }
        _item.setBestVisitTime(_tmpBestVisitTime);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
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
        final String _tmpReservationUrl;
        if (_cursor.isNull(_cursorIndexOfReservationUrl)) {
          _tmpReservationUrl = null;
        } else {
          _tmpReservationUrl = _cursor.getString(_cursorIndexOfReservationUrl);
        }
        _item.setReservationUrl(_tmpReservationUrl);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _item.setFacilities(_tmpFacilities);
        final String _tmpTips;
        if (_cursor.isNull(_cursorIndexOfTips)) {
          _tmpTips = null;
        } else {
          _tmpTips = _cursor.getString(_cursorIndexOfTips);
        }
        _item.setTips(_tmpTips);
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
  public List<AttractionEntity> getByCityAndCrowdLevel(final String city, final String crowdLevel) {
    final String _sql = "SELECT * FROM attractions WHERE city = ? AND crowdLevel = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (city == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, city);
    }
    _argIndex = 2;
    if (crowdLevel == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, crowdLevel);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfProvince = CursorUtil.getColumnIndexOrThrow(_cursor, "province");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfOpenTime = CursorUtil.getColumnIndexOrThrow(_cursor, "openTime");
      final int _cursorIndexOfTicketPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketPrice");
      final int _cursorIndexOfPlayDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "playDuration");
      final int _cursorIndexOfCrowdLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdLevel");
      final int _cursorIndexOfCrowdIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdIndex");
      final int _cursorIndexOfBestVisitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bestVisitTime");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfReservationUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationUrl");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<AttractionEntity> _result = new ArrayList<AttractionEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AttractionEntity _item;
        _item = new AttractionEntity();
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
        final String _tmpProvince;
        if (_cursor.isNull(_cursorIndexOfProvince)) {
          _tmpProvince = null;
        } else {
          _tmpProvince = _cursor.getString(_cursorIndexOfProvince);
        }
        _item.setProvince(_tmpProvince);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final String _tmpOpenTime;
        if (_cursor.isNull(_cursorIndexOfOpenTime)) {
          _tmpOpenTime = null;
        } else {
          _tmpOpenTime = _cursor.getString(_cursorIndexOfOpenTime);
        }
        _item.setOpenTime(_tmpOpenTime);
        final BigDecimal _tmpTicketPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfTicketPrice);
        }
        _tmpTicketPrice = BigDecimalConverter.fromString(_tmp);
        _item.setTicketPrice(_tmpTicketPrice);
        final Integer _tmpPlayDuration;
        if (_cursor.isNull(_cursorIndexOfPlayDuration)) {
          _tmpPlayDuration = null;
        } else {
          _tmpPlayDuration = _cursor.getInt(_cursorIndexOfPlayDuration);
        }
        _item.setPlayDuration(_tmpPlayDuration);
        final String _tmpCrowdLevel;
        if (_cursor.isNull(_cursorIndexOfCrowdLevel)) {
          _tmpCrowdLevel = null;
        } else {
          _tmpCrowdLevel = _cursor.getString(_cursorIndexOfCrowdLevel);
        }
        _item.setCrowdLevel(_tmpCrowdLevel);
        final String _tmpCrowdIndex;
        if (_cursor.isNull(_cursorIndexOfCrowdIndex)) {
          _tmpCrowdIndex = null;
        } else {
          _tmpCrowdIndex = _cursor.getString(_cursorIndexOfCrowdIndex);
        }
        _item.setCrowdIndex(_tmpCrowdIndex);
        final String _tmpBestVisitTime;
        if (_cursor.isNull(_cursorIndexOfBestVisitTime)) {
          _tmpBestVisitTime = null;
        } else {
          _tmpBestVisitTime = _cursor.getString(_cursorIndexOfBestVisitTime);
        }
        _item.setBestVisitTime(_tmpBestVisitTime);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
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
        final String _tmpReservationUrl;
        if (_cursor.isNull(_cursorIndexOfReservationUrl)) {
          _tmpReservationUrl = null;
        } else {
          _tmpReservationUrl = _cursor.getString(_cursorIndexOfReservationUrl);
        }
        _item.setReservationUrl(_tmpReservationUrl);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _item.setFacilities(_tmpFacilities);
        final String _tmpTips;
        if (_cursor.isNull(_cursorIndexOfTips)) {
          _tmpTips = null;
        } else {
          _tmpTips = _cursor.getString(_cursorIndexOfTips);
        }
        _item.setTips(_tmpTips);
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
  public List<AttractionEntity> search(final String keyword) {
    final String _sql = "SELECT * FROM attractions WHERE name LIKE '%' || ? || '%' OR city LIKE '%' || ? || '%'";
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
      final int _cursorIndexOfProvince = CursorUtil.getColumnIndexOrThrow(_cursor, "province");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfOpenTime = CursorUtil.getColumnIndexOrThrow(_cursor, "openTime");
      final int _cursorIndexOfTicketPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketPrice");
      final int _cursorIndexOfPlayDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "playDuration");
      final int _cursorIndexOfCrowdLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdLevel");
      final int _cursorIndexOfCrowdIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "crowdIndex");
      final int _cursorIndexOfBestVisitTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bestVisitTime");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfCommentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "commentCount");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfReservationUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationUrl");
      final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
      final int _cursorIndexOfFacilities = CursorUtil.getColumnIndexOrThrow(_cursor, "facilities");
      final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
      final int _cursorIndexOfCacheTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTime");
      final List<AttractionEntity> _result = new ArrayList<AttractionEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AttractionEntity _item;
        _item = new AttractionEntity();
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
        final String _tmpProvince;
        if (_cursor.isNull(_cursorIndexOfProvince)) {
          _tmpProvince = null;
        } else {
          _tmpProvince = _cursor.getString(_cursorIndexOfProvince);
        }
        _item.setProvince(_tmpProvince);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item.setDescription(_tmpDescription);
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        _item.setAddress(_tmpAddress);
        final String _tmpOpenTime;
        if (_cursor.isNull(_cursorIndexOfOpenTime)) {
          _tmpOpenTime = null;
        } else {
          _tmpOpenTime = _cursor.getString(_cursorIndexOfOpenTime);
        }
        _item.setOpenTime(_tmpOpenTime);
        final BigDecimal _tmpTicketPrice;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfTicketPrice)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfTicketPrice);
        }
        _tmpTicketPrice = BigDecimalConverter.fromString(_tmp);
        _item.setTicketPrice(_tmpTicketPrice);
        final Integer _tmpPlayDuration;
        if (_cursor.isNull(_cursorIndexOfPlayDuration)) {
          _tmpPlayDuration = null;
        } else {
          _tmpPlayDuration = _cursor.getInt(_cursorIndexOfPlayDuration);
        }
        _item.setPlayDuration(_tmpPlayDuration);
        final String _tmpCrowdLevel;
        if (_cursor.isNull(_cursorIndexOfCrowdLevel)) {
          _tmpCrowdLevel = null;
        } else {
          _tmpCrowdLevel = _cursor.getString(_cursorIndexOfCrowdLevel);
        }
        _item.setCrowdLevel(_tmpCrowdLevel);
        final String _tmpCrowdIndex;
        if (_cursor.isNull(_cursorIndexOfCrowdIndex)) {
          _tmpCrowdIndex = null;
        } else {
          _tmpCrowdIndex = _cursor.getString(_cursorIndexOfCrowdIndex);
        }
        _item.setCrowdIndex(_tmpCrowdIndex);
        final String _tmpBestVisitTime;
        if (_cursor.isNull(_cursorIndexOfBestVisitTime)) {
          _tmpBestVisitTime = null;
        } else {
          _tmpBestVisitTime = _cursor.getString(_cursorIndexOfBestVisitTime);
        }
        _item.setBestVisitTime(_tmpBestVisitTime);
        final String _tmpImageUrl;
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _tmpImageUrl = null;
        } else {
          _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.setImageUrl(_tmpImageUrl);
        final Double _tmpRating;
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _tmpRating = null;
        } else {
          _tmpRating = _cursor.getDouble(_cursorIndexOfRating);
        }
        _item.setRating(_tmpRating);
        final Integer _tmpCommentCount;
        if (_cursor.isNull(_cursorIndexOfCommentCount)) {
          _tmpCommentCount = null;
        } else {
          _tmpCommentCount = _cursor.getInt(_cursorIndexOfCommentCount);
        }
        _item.setCommentCount(_tmpCommentCount);
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
        final String _tmpReservationUrl;
        if (_cursor.isNull(_cursorIndexOfReservationUrl)) {
          _tmpReservationUrl = null;
        } else {
          _tmpReservationUrl = _cursor.getString(_cursorIndexOfReservationUrl);
        }
        _item.setReservationUrl(_tmpReservationUrl);
        final String _tmpPhone;
        if (_cursor.isNull(_cursorIndexOfPhone)) {
          _tmpPhone = null;
        } else {
          _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        }
        _item.setPhone(_tmpPhone);
        final String _tmpFacilities;
        if (_cursor.isNull(_cursorIndexOfFacilities)) {
          _tmpFacilities = null;
        } else {
          _tmpFacilities = _cursor.getString(_cursorIndexOfFacilities);
        }
        _item.setFacilities(_tmpFacilities);
        final String _tmpTips;
        if (_cursor.isNull(_cursorIndexOfTips)) {
          _tmpTips = null;
        } else {
          _tmpTips = _cursor.getString(_cursorIndexOfTips);
        }
        _item.setTips(_tmpTips);
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
