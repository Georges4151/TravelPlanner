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
import com.travelplanner.data.local.entity.ItineraryEntity;
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
public final class ItineraryDao_Impl implements ItineraryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ItineraryEntity> __insertionAdapterOfItineraryEntity;

  private final EntityDeletionOrUpdateAdapter<ItineraryEntity> __deletionAdapterOfItineraryEntity;

  private final EntityDeletionOrUpdateAdapter<ItineraryEntity> __updateAdapterOfItineraryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  public ItineraryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItineraryEntity = new EntityInsertionAdapter<ItineraryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `itineraries` (`id`,`title`,`destination`,`startDate`,`endDate`,`days`,`personCount`,`budget`,`style`,`status`,`departureAddress`,`departureLatitude`,`departureLongitude`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ItineraryEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getDestination() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDestination());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEndDate());
        }
        if (entity.getDays() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDays());
        }
        if (entity.getPersonCount() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPersonCount());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getBudget());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        if (entity.getStyle() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getStyle());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getStatus());
        }
        if (entity.getDepartureAddress() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDepartureAddress());
        }
        if (entity.getDepartureLatitude() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getDepartureLatitude());
        }
        if (entity.getDepartureLongitude() == null) {
          statement.bindNull(13);
        } else {
          statement.bindDouble(13, entity.getDepartureLongitude());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getUpdateTime());
        }
      }
    };
    this.__deletionAdapterOfItineraryEntity = new EntityDeletionOrUpdateAdapter<ItineraryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `itineraries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ItineraryEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfItineraryEntity = new EntityDeletionOrUpdateAdapter<ItineraryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `itineraries` SET `id` = ?,`title` = ?,`destination` = ?,`startDate` = ?,`endDate` = ?,`days` = ?,`personCount` = ?,`budget` = ?,`style` = ?,`status` = ?,`departureAddress` = ?,`departureLatitude` = ?,`departureLongitude` = ?,`createTime` = ?,`updateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ItineraryEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getDestination() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDestination());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEndDate());
        }
        if (entity.getDays() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDays());
        }
        if (entity.getPersonCount() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPersonCount());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getBudget());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        if (entity.getStyle() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getStyle());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getStatus());
        }
        if (entity.getDepartureAddress() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDepartureAddress());
        }
        if (entity.getDepartureLatitude() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getDepartureLatitude());
        }
        if (entity.getDepartureLongitude() == null) {
          statement.bindNull(13);
        } else {
          statement.bindDouble(13, entity.getDepartureLongitude());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getUpdateTime());
        }
        if (entity.getId() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM itineraries WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE itineraries SET status = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final ItineraryEntity itinerary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfItineraryEntity.insertAndReturnId(itinerary);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<ItineraryEntity> itineraries) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfItineraryEntity.insert(itineraries);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ItineraryEntity itinerary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfItineraryEntity.handle(itinerary);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ItineraryEntity itinerary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfItineraryEntity.handle(itinerary);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final Long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, id);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public void updateStatus(final Long id, final int status) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, status);
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, id);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateStatus.release(_stmt);
    }
  }

  @Override
  public ItineraryEntity getById(final Long id) {
    final String _sql = "SELECT * FROM itineraries WHERE id = ?";
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
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDestination = CursorUtil.getColumnIndexOrThrow(_cursor, "destination");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfDays = CursorUtil.getColumnIndexOrThrow(_cursor, "days");
      final int _cursorIndexOfPersonCount = CursorUtil.getColumnIndexOrThrow(_cursor, "personCount");
      final int _cursorIndexOfBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "budget");
      final int _cursorIndexOfStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "style");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDepartureAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "departureAddress");
      final int _cursorIndexOfDepartureLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLatitude");
      final int _cursorIndexOfDepartureLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLongitude");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final ItineraryEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new ItineraryEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpDestination;
        if (_cursor.isNull(_cursorIndexOfDestination)) {
          _tmpDestination = null;
        } else {
          _tmpDestination = _cursor.getString(_cursorIndexOfDestination);
        }
        _result.setDestination(_tmpDestination);
        final String _tmpStartDate;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmpStartDate = null;
        } else {
          _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        }
        _result.setStartDate(_tmpStartDate);
        final String _tmpEndDate;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmpEndDate = null;
        } else {
          _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        }
        _result.setEndDate(_tmpEndDate);
        final Integer _tmpDays;
        if (_cursor.isNull(_cursorIndexOfDays)) {
          _tmpDays = null;
        } else {
          _tmpDays = _cursor.getInt(_cursorIndexOfDays);
        }
        _result.setDays(_tmpDays);
        final Integer _tmpPersonCount;
        if (_cursor.isNull(_cursorIndexOfPersonCount)) {
          _tmpPersonCount = null;
        } else {
          _tmpPersonCount = _cursor.getInt(_cursorIndexOfPersonCount);
        }
        _result.setPersonCount(_tmpPersonCount);
        final BigDecimal _tmpBudget;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfBudget)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfBudget);
        }
        _tmpBudget = BigDecimalConverter.fromString(_tmp);
        _result.setBudget(_tmpBudget);
        final String _tmpStyle;
        if (_cursor.isNull(_cursorIndexOfStyle)) {
          _tmpStyle = null;
        } else {
          _tmpStyle = _cursor.getString(_cursorIndexOfStyle);
        }
        _result.setStyle(_tmpStyle);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpDepartureAddress;
        if (_cursor.isNull(_cursorIndexOfDepartureAddress)) {
          _tmpDepartureAddress = null;
        } else {
          _tmpDepartureAddress = _cursor.getString(_cursorIndexOfDepartureAddress);
        }
        _result.setDepartureAddress(_tmpDepartureAddress);
        final Double _tmpDepartureLatitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLatitude)) {
          _tmpDepartureLatitude = null;
        } else {
          _tmpDepartureLatitude = _cursor.getDouble(_cursorIndexOfDepartureLatitude);
        }
        _result.setDepartureLatitude(_tmpDepartureLatitude);
        final Double _tmpDepartureLongitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLongitude)) {
          _tmpDepartureLongitude = null;
        } else {
          _tmpDepartureLongitude = _cursor.getDouble(_cursorIndexOfDepartureLongitude);
        }
        _result.setDepartureLongitude(_tmpDepartureLongitude);
        final Long _tmpCreateTime;
        if (_cursor.isNull(_cursorIndexOfCreateTime)) {
          _tmpCreateTime = null;
        } else {
          _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        }
        _result.setCreateTime(_tmpCreateTime);
        final Long _tmpUpdateTime;
        if (_cursor.isNull(_cursorIndexOfUpdateTime)) {
          _tmpUpdateTime = null;
        } else {
          _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        }
        _result.setUpdateTime(_tmpUpdateTime);
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
  public ItineraryEntity getByIdSync(final Long id) {
    final String _sql = "SELECT * FROM itineraries WHERE id = ?";
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
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDestination = CursorUtil.getColumnIndexOrThrow(_cursor, "destination");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfDays = CursorUtil.getColumnIndexOrThrow(_cursor, "days");
      final int _cursorIndexOfPersonCount = CursorUtil.getColumnIndexOrThrow(_cursor, "personCount");
      final int _cursorIndexOfBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "budget");
      final int _cursorIndexOfStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "style");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDepartureAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "departureAddress");
      final int _cursorIndexOfDepartureLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLatitude");
      final int _cursorIndexOfDepartureLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLongitude");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final ItineraryEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new ItineraryEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpDestination;
        if (_cursor.isNull(_cursorIndexOfDestination)) {
          _tmpDestination = null;
        } else {
          _tmpDestination = _cursor.getString(_cursorIndexOfDestination);
        }
        _result.setDestination(_tmpDestination);
        final String _tmpStartDate;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmpStartDate = null;
        } else {
          _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        }
        _result.setStartDate(_tmpStartDate);
        final String _tmpEndDate;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmpEndDate = null;
        } else {
          _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        }
        _result.setEndDate(_tmpEndDate);
        final Integer _tmpDays;
        if (_cursor.isNull(_cursorIndexOfDays)) {
          _tmpDays = null;
        } else {
          _tmpDays = _cursor.getInt(_cursorIndexOfDays);
        }
        _result.setDays(_tmpDays);
        final Integer _tmpPersonCount;
        if (_cursor.isNull(_cursorIndexOfPersonCount)) {
          _tmpPersonCount = null;
        } else {
          _tmpPersonCount = _cursor.getInt(_cursorIndexOfPersonCount);
        }
        _result.setPersonCount(_tmpPersonCount);
        final BigDecimal _tmpBudget;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfBudget)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfBudget);
        }
        _tmpBudget = BigDecimalConverter.fromString(_tmp);
        _result.setBudget(_tmpBudget);
        final String _tmpStyle;
        if (_cursor.isNull(_cursorIndexOfStyle)) {
          _tmpStyle = null;
        } else {
          _tmpStyle = _cursor.getString(_cursorIndexOfStyle);
        }
        _result.setStyle(_tmpStyle);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpDepartureAddress;
        if (_cursor.isNull(_cursorIndexOfDepartureAddress)) {
          _tmpDepartureAddress = null;
        } else {
          _tmpDepartureAddress = _cursor.getString(_cursorIndexOfDepartureAddress);
        }
        _result.setDepartureAddress(_tmpDepartureAddress);
        final Double _tmpDepartureLatitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLatitude)) {
          _tmpDepartureLatitude = null;
        } else {
          _tmpDepartureLatitude = _cursor.getDouble(_cursorIndexOfDepartureLatitude);
        }
        _result.setDepartureLatitude(_tmpDepartureLatitude);
        final Double _tmpDepartureLongitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLongitude)) {
          _tmpDepartureLongitude = null;
        } else {
          _tmpDepartureLongitude = _cursor.getDouble(_cursorIndexOfDepartureLongitude);
        }
        _result.setDepartureLongitude(_tmpDepartureLongitude);
        final Long _tmpCreateTime;
        if (_cursor.isNull(_cursorIndexOfCreateTime)) {
          _tmpCreateTime = null;
        } else {
          _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        }
        _result.setCreateTime(_tmpCreateTime);
        final Long _tmpUpdateTime;
        if (_cursor.isNull(_cursorIndexOfUpdateTime)) {
          _tmpUpdateTime = null;
        } else {
          _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        }
        _result.setUpdateTime(_tmpUpdateTime);
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
  public List<ItineraryEntity> getAll() {
    final String _sql = "SELECT * FROM itineraries ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDestination = CursorUtil.getColumnIndexOrThrow(_cursor, "destination");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfDays = CursorUtil.getColumnIndexOrThrow(_cursor, "days");
      final int _cursorIndexOfPersonCount = CursorUtil.getColumnIndexOrThrow(_cursor, "personCount");
      final int _cursorIndexOfBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "budget");
      final int _cursorIndexOfStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "style");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDepartureAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "departureAddress");
      final int _cursorIndexOfDepartureLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLatitude");
      final int _cursorIndexOfDepartureLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLongitude");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ItineraryEntity> _result = new ArrayList<ItineraryEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ItineraryEntity _item;
        _item = new ItineraryEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpDestination;
        if (_cursor.isNull(_cursorIndexOfDestination)) {
          _tmpDestination = null;
        } else {
          _tmpDestination = _cursor.getString(_cursorIndexOfDestination);
        }
        _item.setDestination(_tmpDestination);
        final String _tmpStartDate;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmpStartDate = null;
        } else {
          _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        }
        _item.setStartDate(_tmpStartDate);
        final String _tmpEndDate;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmpEndDate = null;
        } else {
          _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        }
        _item.setEndDate(_tmpEndDate);
        final Integer _tmpDays;
        if (_cursor.isNull(_cursorIndexOfDays)) {
          _tmpDays = null;
        } else {
          _tmpDays = _cursor.getInt(_cursorIndexOfDays);
        }
        _item.setDays(_tmpDays);
        final Integer _tmpPersonCount;
        if (_cursor.isNull(_cursorIndexOfPersonCount)) {
          _tmpPersonCount = null;
        } else {
          _tmpPersonCount = _cursor.getInt(_cursorIndexOfPersonCount);
        }
        _item.setPersonCount(_tmpPersonCount);
        final BigDecimal _tmpBudget;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfBudget)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfBudget);
        }
        _tmpBudget = BigDecimalConverter.fromString(_tmp);
        _item.setBudget(_tmpBudget);
        final String _tmpStyle;
        if (_cursor.isNull(_cursorIndexOfStyle)) {
          _tmpStyle = null;
        } else {
          _tmpStyle = _cursor.getString(_cursorIndexOfStyle);
        }
        _item.setStyle(_tmpStyle);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpDepartureAddress;
        if (_cursor.isNull(_cursorIndexOfDepartureAddress)) {
          _tmpDepartureAddress = null;
        } else {
          _tmpDepartureAddress = _cursor.getString(_cursorIndexOfDepartureAddress);
        }
        _item.setDepartureAddress(_tmpDepartureAddress);
        final Double _tmpDepartureLatitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLatitude)) {
          _tmpDepartureLatitude = null;
        } else {
          _tmpDepartureLatitude = _cursor.getDouble(_cursorIndexOfDepartureLatitude);
        }
        _item.setDepartureLatitude(_tmpDepartureLatitude);
        final Double _tmpDepartureLongitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLongitude)) {
          _tmpDepartureLongitude = null;
        } else {
          _tmpDepartureLongitude = _cursor.getDouble(_cursorIndexOfDepartureLongitude);
        }
        _item.setDepartureLongitude(_tmpDepartureLongitude);
        final Long _tmpCreateTime;
        if (_cursor.isNull(_cursorIndexOfCreateTime)) {
          _tmpCreateTime = null;
        } else {
          _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        }
        _item.setCreateTime(_tmpCreateTime);
        final Long _tmpUpdateTime;
        if (_cursor.isNull(_cursorIndexOfUpdateTime)) {
          _tmpUpdateTime = null;
        } else {
          _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        }
        _item.setUpdateTime(_tmpUpdateTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ItineraryEntity> getByStatus(final int status) {
    final String _sql = "SELECT * FROM itineraries WHERE status = ? ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, status);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDestination = CursorUtil.getColumnIndexOrThrow(_cursor, "destination");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfDays = CursorUtil.getColumnIndexOrThrow(_cursor, "days");
      final int _cursorIndexOfPersonCount = CursorUtil.getColumnIndexOrThrow(_cursor, "personCount");
      final int _cursorIndexOfBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "budget");
      final int _cursorIndexOfStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "style");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDepartureAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "departureAddress");
      final int _cursorIndexOfDepartureLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLatitude");
      final int _cursorIndexOfDepartureLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLongitude");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ItineraryEntity> _result = new ArrayList<ItineraryEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ItineraryEntity _item;
        _item = new ItineraryEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpDestination;
        if (_cursor.isNull(_cursorIndexOfDestination)) {
          _tmpDestination = null;
        } else {
          _tmpDestination = _cursor.getString(_cursorIndexOfDestination);
        }
        _item.setDestination(_tmpDestination);
        final String _tmpStartDate;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmpStartDate = null;
        } else {
          _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        }
        _item.setStartDate(_tmpStartDate);
        final String _tmpEndDate;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmpEndDate = null;
        } else {
          _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        }
        _item.setEndDate(_tmpEndDate);
        final Integer _tmpDays;
        if (_cursor.isNull(_cursorIndexOfDays)) {
          _tmpDays = null;
        } else {
          _tmpDays = _cursor.getInt(_cursorIndexOfDays);
        }
        _item.setDays(_tmpDays);
        final Integer _tmpPersonCount;
        if (_cursor.isNull(_cursorIndexOfPersonCount)) {
          _tmpPersonCount = null;
        } else {
          _tmpPersonCount = _cursor.getInt(_cursorIndexOfPersonCount);
        }
        _item.setPersonCount(_tmpPersonCount);
        final BigDecimal _tmpBudget;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfBudget)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfBudget);
        }
        _tmpBudget = BigDecimalConverter.fromString(_tmp);
        _item.setBudget(_tmpBudget);
        final String _tmpStyle;
        if (_cursor.isNull(_cursorIndexOfStyle)) {
          _tmpStyle = null;
        } else {
          _tmpStyle = _cursor.getString(_cursorIndexOfStyle);
        }
        _item.setStyle(_tmpStyle);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpDepartureAddress;
        if (_cursor.isNull(_cursorIndexOfDepartureAddress)) {
          _tmpDepartureAddress = null;
        } else {
          _tmpDepartureAddress = _cursor.getString(_cursorIndexOfDepartureAddress);
        }
        _item.setDepartureAddress(_tmpDepartureAddress);
        final Double _tmpDepartureLatitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLatitude)) {
          _tmpDepartureLatitude = null;
        } else {
          _tmpDepartureLatitude = _cursor.getDouble(_cursorIndexOfDepartureLatitude);
        }
        _item.setDepartureLatitude(_tmpDepartureLatitude);
        final Double _tmpDepartureLongitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLongitude)) {
          _tmpDepartureLongitude = null;
        } else {
          _tmpDepartureLongitude = _cursor.getDouble(_cursorIndexOfDepartureLongitude);
        }
        _item.setDepartureLongitude(_tmpDepartureLongitude);
        final Long _tmpCreateTime;
        if (_cursor.isNull(_cursorIndexOfCreateTime)) {
          _tmpCreateTime = null;
        } else {
          _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        }
        _item.setCreateTime(_tmpCreateTime);
        final Long _tmpUpdateTime;
        if (_cursor.isNull(_cursorIndexOfUpdateTime)) {
          _tmpUpdateTime = null;
        } else {
          _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        }
        _item.setUpdateTime(_tmpUpdateTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ItineraryEntity> search(final String keyword) {
    final String _sql = "SELECT * FROM itineraries WHERE destination LIKE '%' || ? || '%' OR title LIKE '%' || ? || '%'";
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
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDestination = CursorUtil.getColumnIndexOrThrow(_cursor, "destination");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
      final int _cursorIndexOfDays = CursorUtil.getColumnIndexOrThrow(_cursor, "days");
      final int _cursorIndexOfPersonCount = CursorUtil.getColumnIndexOrThrow(_cursor, "personCount");
      final int _cursorIndexOfBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "budget");
      final int _cursorIndexOfStyle = CursorUtil.getColumnIndexOrThrow(_cursor, "style");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDepartureAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "departureAddress");
      final int _cursorIndexOfDepartureLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLatitude");
      final int _cursorIndexOfDepartureLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "departureLongitude");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ItineraryEntity> _result = new ArrayList<ItineraryEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ItineraryEntity _item;
        _item = new ItineraryEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpDestination;
        if (_cursor.isNull(_cursorIndexOfDestination)) {
          _tmpDestination = null;
        } else {
          _tmpDestination = _cursor.getString(_cursorIndexOfDestination);
        }
        _item.setDestination(_tmpDestination);
        final String _tmpStartDate;
        if (_cursor.isNull(_cursorIndexOfStartDate)) {
          _tmpStartDate = null;
        } else {
          _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        }
        _item.setStartDate(_tmpStartDate);
        final String _tmpEndDate;
        if (_cursor.isNull(_cursorIndexOfEndDate)) {
          _tmpEndDate = null;
        } else {
          _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        }
        _item.setEndDate(_tmpEndDate);
        final Integer _tmpDays;
        if (_cursor.isNull(_cursorIndexOfDays)) {
          _tmpDays = null;
        } else {
          _tmpDays = _cursor.getInt(_cursorIndexOfDays);
        }
        _item.setDays(_tmpDays);
        final Integer _tmpPersonCount;
        if (_cursor.isNull(_cursorIndexOfPersonCount)) {
          _tmpPersonCount = null;
        } else {
          _tmpPersonCount = _cursor.getInt(_cursorIndexOfPersonCount);
        }
        _item.setPersonCount(_tmpPersonCount);
        final BigDecimal _tmpBudget;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfBudget)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfBudget);
        }
        _tmpBudget = BigDecimalConverter.fromString(_tmp);
        _item.setBudget(_tmpBudget);
        final String _tmpStyle;
        if (_cursor.isNull(_cursorIndexOfStyle)) {
          _tmpStyle = null;
        } else {
          _tmpStyle = _cursor.getString(_cursorIndexOfStyle);
        }
        _item.setStyle(_tmpStyle);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpDepartureAddress;
        if (_cursor.isNull(_cursorIndexOfDepartureAddress)) {
          _tmpDepartureAddress = null;
        } else {
          _tmpDepartureAddress = _cursor.getString(_cursorIndexOfDepartureAddress);
        }
        _item.setDepartureAddress(_tmpDepartureAddress);
        final Double _tmpDepartureLatitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLatitude)) {
          _tmpDepartureLatitude = null;
        } else {
          _tmpDepartureLatitude = _cursor.getDouble(_cursorIndexOfDepartureLatitude);
        }
        _item.setDepartureLatitude(_tmpDepartureLatitude);
        final Double _tmpDepartureLongitude;
        if (_cursor.isNull(_cursorIndexOfDepartureLongitude)) {
          _tmpDepartureLongitude = null;
        } else {
          _tmpDepartureLongitude = _cursor.getDouble(_cursorIndexOfDepartureLongitude);
        }
        _item.setDepartureLongitude(_tmpDepartureLongitude);
        final Long _tmpCreateTime;
        if (_cursor.isNull(_cursorIndexOfCreateTime)) {
          _tmpCreateTime = null;
        } else {
          _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        }
        _item.setCreateTime(_tmpCreateTime);
        final Long _tmpUpdateTime;
        if (_cursor.isNull(_cursorIndexOfUpdateTime)) {
          _tmpUpdateTime = null;
        } else {
          _tmpUpdateTime = _cursor.getLong(_cursorIndexOfUpdateTime);
        }
        _item.setUpdateTime(_tmpUpdateTime);
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
