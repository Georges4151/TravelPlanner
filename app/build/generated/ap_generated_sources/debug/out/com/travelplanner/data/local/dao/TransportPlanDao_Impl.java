package com.travelplanner.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.travelplanner.data.local.BigDecimalConverter;
import com.travelplanner.data.local.entity.TransportPlanEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TransportPlanDao_Impl implements TransportPlanDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransportPlanEntity> __insertionAdapterOfTransportPlanEntity;

  private final EntityDeletionOrUpdateAdapter<TransportPlanEntity> __deletionAdapterOfTransportPlanEntity;

  private final EntityDeletionOrUpdateAdapter<TransportPlanEntity> __updateAdapterOfTransportPlanEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByDayId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByItineraryId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByPlanId;

  public TransportPlanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransportPlanEntity = new EntityInsertionAdapter<TransportPlanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `transport_plans` (`planId`,`itineraryId`,`dayId`,`fromAddress`,`toAddress`,`fromLatitude`,`fromLongitude`,`toLatitude`,`toLongitude`,`transportMode`,`distance`,`duration`,`cost`,`trafficStatus`,`routeData`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TransportPlanEntity entity) {
        if (entity.getPlanId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPlanId());
        }
        if (entity.getItineraryId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getItineraryId());
        }
        if (entity.getDayId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getDayId());
        }
        if (entity.getFromAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFromAddress());
        }
        if (entity.getToAddress() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getToAddress());
        }
        if (entity.getFromLatitude() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFromLatitude());
        }
        if (entity.getFromLongitude() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFromLongitude());
        }
        if (entity.getToLatitude() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getToLatitude());
        }
        if (entity.getToLongitude() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getToLongitude());
        }
        if (entity.getTransportMode() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTransportMode());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getDistance());
        }
        if (entity.getDuration() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getDuration());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getCost());
        if (_tmp == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, _tmp);
        }
        if (entity.getTrafficStatus() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTrafficStatus());
        }
        if (entity.getRouteData() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getRouteData());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getUpdateTime());
        }
      }
    };
    this.__deletionAdapterOfTransportPlanEntity = new EntityDeletionOrUpdateAdapter<TransportPlanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transport_plans` WHERE `planId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TransportPlanEntity entity) {
        if (entity.getPlanId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPlanId());
        }
      }
    };
    this.__updateAdapterOfTransportPlanEntity = new EntityDeletionOrUpdateAdapter<TransportPlanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transport_plans` SET `planId` = ?,`itineraryId` = ?,`dayId` = ?,`fromAddress` = ?,`toAddress` = ?,`fromLatitude` = ?,`fromLongitude` = ?,`toLatitude` = ?,`toLongitude` = ?,`transportMode` = ?,`distance` = ?,`duration` = ?,`cost` = ?,`trafficStatus` = ?,`routeData` = ?,`createTime` = ?,`updateTime` = ? WHERE `planId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TransportPlanEntity entity) {
        if (entity.getPlanId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPlanId());
        }
        if (entity.getItineraryId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getItineraryId());
        }
        if (entity.getDayId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getDayId());
        }
        if (entity.getFromAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFromAddress());
        }
        if (entity.getToAddress() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getToAddress());
        }
        if (entity.getFromLatitude() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFromLatitude());
        }
        if (entity.getFromLongitude() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFromLongitude());
        }
        if (entity.getToLatitude() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getToLatitude());
        }
        if (entity.getToLongitude() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getToLongitude());
        }
        if (entity.getTransportMode() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTransportMode());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getDistance());
        }
        if (entity.getDuration() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getDuration());
        }
        final String _tmp = BigDecimalConverter.toString(entity.getCost());
        if (_tmp == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, _tmp);
        }
        if (entity.getTrafficStatus() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTrafficStatus());
        }
        if (entity.getRouteData() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getRouteData());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getUpdateTime());
        }
        if (entity.getPlanId() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getPlanId());
        }
      }
    };
    this.__preparedStmtOfDeleteByDayId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transport_plans WHERE dayId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByItineraryId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transport_plans WHERE itineraryId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByPlanId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transport_plans WHERE planId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final TransportPlanEntity transportPlan) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTransportPlanEntity.insert(transportPlan);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final TransportPlanEntity transportPlan) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTransportPlanEntity.handle(transportPlan);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final TransportPlanEntity transportPlan) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTransportPlanEntity.handle(transportPlan);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteByDayId(final Long dayId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByDayId.acquire();
    int _argIndex = 1;
    if (dayId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, dayId);
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
      __preparedStmtOfDeleteByDayId.release(_stmt);
    }
  }

  @Override
  public void deleteByItineraryId(final Long itineraryId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByItineraryId.acquire();
    int _argIndex = 1;
    if (itineraryId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, itineraryId);
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
      __preparedStmtOfDeleteByItineraryId.release(_stmt);
    }
  }

  @Override
  public void deleteByPlanId(final String planId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByPlanId.acquire();
    int _argIndex = 1;
    if (planId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, planId);
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
      __preparedStmtOfDeleteByPlanId.release(_stmt);
    }
  }

  @Override
  public LiveData<TransportPlanEntity> getByPlanId(final String planId) {
    final String _sql = "SELECT * FROM transport_plans WHERE planId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (planId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, planId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transport_plans"}, false, new Callable<TransportPlanEntity>() {
      @Override
      @Nullable
      public TransportPlanEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
          final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
          final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
          final int _cursorIndexOfFromAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "fromAddress");
          final int _cursorIndexOfToAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "toAddress");
          final int _cursorIndexOfFromLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLatitude");
          final int _cursorIndexOfFromLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLongitude");
          final int _cursorIndexOfToLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLatitude");
          final int _cursorIndexOfToLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLongitude");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
          final int _cursorIndexOfTrafficStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "trafficStatus");
          final int _cursorIndexOfRouteData = CursorUtil.getColumnIndexOrThrow(_cursor, "routeData");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final TransportPlanEntity _result;
          if (_cursor.moveToFirst()) {
            _result = new TransportPlanEntity();
            final String _tmpPlanId;
            if (_cursor.isNull(_cursorIndexOfPlanId)) {
              _tmpPlanId = null;
            } else {
              _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
            }
            _result.setPlanId(_tmpPlanId);
            final Long _tmpItineraryId;
            if (_cursor.isNull(_cursorIndexOfItineraryId)) {
              _tmpItineraryId = null;
            } else {
              _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
            }
            _result.setItineraryId(_tmpItineraryId);
            final Long _tmpDayId;
            if (_cursor.isNull(_cursorIndexOfDayId)) {
              _tmpDayId = null;
            } else {
              _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
            }
            _result.setDayId(_tmpDayId);
            final String _tmpFromAddress;
            if (_cursor.isNull(_cursorIndexOfFromAddress)) {
              _tmpFromAddress = null;
            } else {
              _tmpFromAddress = _cursor.getString(_cursorIndexOfFromAddress);
            }
            _result.setFromAddress(_tmpFromAddress);
            final String _tmpToAddress;
            if (_cursor.isNull(_cursorIndexOfToAddress)) {
              _tmpToAddress = null;
            } else {
              _tmpToAddress = _cursor.getString(_cursorIndexOfToAddress);
            }
            _result.setToAddress(_tmpToAddress);
            final String _tmpFromLatitude;
            if (_cursor.isNull(_cursorIndexOfFromLatitude)) {
              _tmpFromLatitude = null;
            } else {
              _tmpFromLatitude = _cursor.getString(_cursorIndexOfFromLatitude);
            }
            _result.setFromLatitude(_tmpFromLatitude);
            final String _tmpFromLongitude;
            if (_cursor.isNull(_cursorIndexOfFromLongitude)) {
              _tmpFromLongitude = null;
            } else {
              _tmpFromLongitude = _cursor.getString(_cursorIndexOfFromLongitude);
            }
            _result.setFromLongitude(_tmpFromLongitude);
            final String _tmpToLatitude;
            if (_cursor.isNull(_cursorIndexOfToLatitude)) {
              _tmpToLatitude = null;
            } else {
              _tmpToLatitude = _cursor.getString(_cursorIndexOfToLatitude);
            }
            _result.setToLatitude(_tmpToLatitude);
            final String _tmpToLongitude;
            if (_cursor.isNull(_cursorIndexOfToLongitude)) {
              _tmpToLongitude = null;
            } else {
              _tmpToLongitude = _cursor.getString(_cursorIndexOfToLongitude);
            }
            _result.setToLongitude(_tmpToLongitude);
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            _result.setTransportMode(_tmpTransportMode);
            final Double _tmpDistance;
            if (_cursor.isNull(_cursorIndexOfDistance)) {
              _tmpDistance = null;
            } else {
              _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            }
            _result.setDistance(_tmpDistance);
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            _result.setDuration(_tmpDuration);
            final BigDecimal _tmpCost;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCost)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCost);
            }
            _tmpCost = BigDecimalConverter.fromString(_tmp);
            _result.setCost(_tmpCost);
            final String _tmpTrafficStatus;
            if (_cursor.isNull(_cursorIndexOfTrafficStatus)) {
              _tmpTrafficStatus = null;
            } else {
              _tmpTrafficStatus = _cursor.getString(_cursorIndexOfTrafficStatus);
            }
            _result.setTrafficStatus(_tmpTrafficStatus);
            final String _tmpRouteData;
            if (_cursor.isNull(_cursorIndexOfRouteData)) {
              _tmpRouteData = null;
            } else {
              _tmpRouteData = _cursor.getString(_cursorIndexOfRouteData);
            }
            _result.setRouteData(_tmpRouteData);
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<TransportPlanEntity>> getByItineraryId(final Long itineraryId) {
    final String _sql = "SELECT * FROM transport_plans WHERE itineraryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itineraryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, itineraryId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transport_plans"}, false, new Callable<List<TransportPlanEntity>>() {
      @Override
      @Nullable
      public List<TransportPlanEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
          final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
          final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
          final int _cursorIndexOfFromAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "fromAddress");
          final int _cursorIndexOfToAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "toAddress");
          final int _cursorIndexOfFromLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLatitude");
          final int _cursorIndexOfFromLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLongitude");
          final int _cursorIndexOfToLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLatitude");
          final int _cursorIndexOfToLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLongitude");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
          final int _cursorIndexOfTrafficStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "trafficStatus");
          final int _cursorIndexOfRouteData = CursorUtil.getColumnIndexOrThrow(_cursor, "routeData");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final List<TransportPlanEntity> _result = new ArrayList<TransportPlanEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransportPlanEntity _item;
            _item = new TransportPlanEntity();
            final String _tmpPlanId;
            if (_cursor.isNull(_cursorIndexOfPlanId)) {
              _tmpPlanId = null;
            } else {
              _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
            }
            _item.setPlanId(_tmpPlanId);
            final Long _tmpItineraryId;
            if (_cursor.isNull(_cursorIndexOfItineraryId)) {
              _tmpItineraryId = null;
            } else {
              _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
            }
            _item.setItineraryId(_tmpItineraryId);
            final Long _tmpDayId;
            if (_cursor.isNull(_cursorIndexOfDayId)) {
              _tmpDayId = null;
            } else {
              _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
            }
            _item.setDayId(_tmpDayId);
            final String _tmpFromAddress;
            if (_cursor.isNull(_cursorIndexOfFromAddress)) {
              _tmpFromAddress = null;
            } else {
              _tmpFromAddress = _cursor.getString(_cursorIndexOfFromAddress);
            }
            _item.setFromAddress(_tmpFromAddress);
            final String _tmpToAddress;
            if (_cursor.isNull(_cursorIndexOfToAddress)) {
              _tmpToAddress = null;
            } else {
              _tmpToAddress = _cursor.getString(_cursorIndexOfToAddress);
            }
            _item.setToAddress(_tmpToAddress);
            final String _tmpFromLatitude;
            if (_cursor.isNull(_cursorIndexOfFromLatitude)) {
              _tmpFromLatitude = null;
            } else {
              _tmpFromLatitude = _cursor.getString(_cursorIndexOfFromLatitude);
            }
            _item.setFromLatitude(_tmpFromLatitude);
            final String _tmpFromLongitude;
            if (_cursor.isNull(_cursorIndexOfFromLongitude)) {
              _tmpFromLongitude = null;
            } else {
              _tmpFromLongitude = _cursor.getString(_cursorIndexOfFromLongitude);
            }
            _item.setFromLongitude(_tmpFromLongitude);
            final String _tmpToLatitude;
            if (_cursor.isNull(_cursorIndexOfToLatitude)) {
              _tmpToLatitude = null;
            } else {
              _tmpToLatitude = _cursor.getString(_cursorIndexOfToLatitude);
            }
            _item.setToLatitude(_tmpToLatitude);
            final String _tmpToLongitude;
            if (_cursor.isNull(_cursorIndexOfToLongitude)) {
              _tmpToLongitude = null;
            } else {
              _tmpToLongitude = _cursor.getString(_cursorIndexOfToLongitude);
            }
            _item.setToLongitude(_tmpToLongitude);
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            _item.setTransportMode(_tmpTransportMode);
            final Double _tmpDistance;
            if (_cursor.isNull(_cursorIndexOfDistance)) {
              _tmpDistance = null;
            } else {
              _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            }
            _item.setDistance(_tmpDistance);
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            _item.setDuration(_tmpDuration);
            final BigDecimal _tmpCost;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCost)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCost);
            }
            _tmpCost = BigDecimalConverter.fromString(_tmp);
            _item.setCost(_tmpCost);
            final String _tmpTrafficStatus;
            if (_cursor.isNull(_cursorIndexOfTrafficStatus)) {
              _tmpTrafficStatus = null;
            } else {
              _tmpTrafficStatus = _cursor.getString(_cursorIndexOfTrafficStatus);
            }
            _item.setTrafficStatus(_tmpTrafficStatus);
            final String _tmpRouteData;
            if (_cursor.isNull(_cursorIndexOfRouteData)) {
              _tmpRouteData = null;
            } else {
              _tmpRouteData = _cursor.getString(_cursorIndexOfRouteData);
            }
            _item.setRouteData(_tmpRouteData);
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<TransportPlanEntity>> getByDayId(final Long dayId) {
    final String _sql = "SELECT * FROM transport_plans WHERE dayId = ? ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dayId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, dayId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transport_plans"}, false, new Callable<List<TransportPlanEntity>>() {
      @Override
      @Nullable
      public List<TransportPlanEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
          final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
          final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
          final int _cursorIndexOfFromAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "fromAddress");
          final int _cursorIndexOfToAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "toAddress");
          final int _cursorIndexOfFromLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLatitude");
          final int _cursorIndexOfFromLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLongitude");
          final int _cursorIndexOfToLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLatitude");
          final int _cursorIndexOfToLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLongitude");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
          final int _cursorIndexOfTrafficStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "trafficStatus");
          final int _cursorIndexOfRouteData = CursorUtil.getColumnIndexOrThrow(_cursor, "routeData");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final List<TransportPlanEntity> _result = new ArrayList<TransportPlanEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransportPlanEntity _item;
            _item = new TransportPlanEntity();
            final String _tmpPlanId;
            if (_cursor.isNull(_cursorIndexOfPlanId)) {
              _tmpPlanId = null;
            } else {
              _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
            }
            _item.setPlanId(_tmpPlanId);
            final Long _tmpItineraryId;
            if (_cursor.isNull(_cursorIndexOfItineraryId)) {
              _tmpItineraryId = null;
            } else {
              _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
            }
            _item.setItineraryId(_tmpItineraryId);
            final Long _tmpDayId;
            if (_cursor.isNull(_cursorIndexOfDayId)) {
              _tmpDayId = null;
            } else {
              _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
            }
            _item.setDayId(_tmpDayId);
            final String _tmpFromAddress;
            if (_cursor.isNull(_cursorIndexOfFromAddress)) {
              _tmpFromAddress = null;
            } else {
              _tmpFromAddress = _cursor.getString(_cursorIndexOfFromAddress);
            }
            _item.setFromAddress(_tmpFromAddress);
            final String _tmpToAddress;
            if (_cursor.isNull(_cursorIndexOfToAddress)) {
              _tmpToAddress = null;
            } else {
              _tmpToAddress = _cursor.getString(_cursorIndexOfToAddress);
            }
            _item.setToAddress(_tmpToAddress);
            final String _tmpFromLatitude;
            if (_cursor.isNull(_cursorIndexOfFromLatitude)) {
              _tmpFromLatitude = null;
            } else {
              _tmpFromLatitude = _cursor.getString(_cursorIndexOfFromLatitude);
            }
            _item.setFromLatitude(_tmpFromLatitude);
            final String _tmpFromLongitude;
            if (_cursor.isNull(_cursorIndexOfFromLongitude)) {
              _tmpFromLongitude = null;
            } else {
              _tmpFromLongitude = _cursor.getString(_cursorIndexOfFromLongitude);
            }
            _item.setFromLongitude(_tmpFromLongitude);
            final String _tmpToLatitude;
            if (_cursor.isNull(_cursorIndexOfToLatitude)) {
              _tmpToLatitude = null;
            } else {
              _tmpToLatitude = _cursor.getString(_cursorIndexOfToLatitude);
            }
            _item.setToLatitude(_tmpToLatitude);
            final String _tmpToLongitude;
            if (_cursor.isNull(_cursorIndexOfToLongitude)) {
              _tmpToLongitude = null;
            } else {
              _tmpToLongitude = _cursor.getString(_cursorIndexOfToLongitude);
            }
            _item.setToLongitude(_tmpToLongitude);
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            _item.setTransportMode(_tmpTransportMode);
            final Double _tmpDistance;
            if (_cursor.isNull(_cursorIndexOfDistance)) {
              _tmpDistance = null;
            } else {
              _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            }
            _item.setDistance(_tmpDistance);
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            _item.setDuration(_tmpDuration);
            final BigDecimal _tmpCost;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCost)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCost);
            }
            _tmpCost = BigDecimalConverter.fromString(_tmp);
            _item.setCost(_tmpCost);
            final String _tmpTrafficStatus;
            if (_cursor.isNull(_cursorIndexOfTrafficStatus)) {
              _tmpTrafficStatus = null;
            } else {
              _tmpTrafficStatus = _cursor.getString(_cursorIndexOfTrafficStatus);
            }
            _item.setTrafficStatus(_tmpTrafficStatus);
            final String _tmpRouteData;
            if (_cursor.isNull(_cursorIndexOfRouteData)) {
              _tmpRouteData = null;
            } else {
              _tmpRouteData = _cursor.getString(_cursorIndexOfRouteData);
            }
            _item.setRouteData(_tmpRouteData);
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<TransportPlanEntity> getByDayIdSync(final Long dayId) {
    final String _sql = "SELECT * FROM transport_plans WHERE dayId = ? ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dayId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, dayId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
      final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
      final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
      final int _cursorIndexOfFromAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "fromAddress");
      final int _cursorIndexOfToAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "toAddress");
      final int _cursorIndexOfFromLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLatitude");
      final int _cursorIndexOfFromLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLongitude");
      final int _cursorIndexOfToLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLatitude");
      final int _cursorIndexOfToLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLongitude");
      final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
      final int _cursorIndexOfTrafficStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "trafficStatus");
      final int _cursorIndexOfRouteData = CursorUtil.getColumnIndexOrThrow(_cursor, "routeData");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<TransportPlanEntity> _result = new ArrayList<TransportPlanEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TransportPlanEntity _item;
        _item = new TransportPlanEntity();
        final String _tmpPlanId;
        if (_cursor.isNull(_cursorIndexOfPlanId)) {
          _tmpPlanId = null;
        } else {
          _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
        }
        _item.setPlanId(_tmpPlanId);
        final Long _tmpItineraryId;
        if (_cursor.isNull(_cursorIndexOfItineraryId)) {
          _tmpItineraryId = null;
        } else {
          _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
        }
        _item.setItineraryId(_tmpItineraryId);
        final Long _tmpDayId;
        if (_cursor.isNull(_cursorIndexOfDayId)) {
          _tmpDayId = null;
        } else {
          _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
        }
        _item.setDayId(_tmpDayId);
        final String _tmpFromAddress;
        if (_cursor.isNull(_cursorIndexOfFromAddress)) {
          _tmpFromAddress = null;
        } else {
          _tmpFromAddress = _cursor.getString(_cursorIndexOfFromAddress);
        }
        _item.setFromAddress(_tmpFromAddress);
        final String _tmpToAddress;
        if (_cursor.isNull(_cursorIndexOfToAddress)) {
          _tmpToAddress = null;
        } else {
          _tmpToAddress = _cursor.getString(_cursorIndexOfToAddress);
        }
        _item.setToAddress(_tmpToAddress);
        final String _tmpFromLatitude;
        if (_cursor.isNull(_cursorIndexOfFromLatitude)) {
          _tmpFromLatitude = null;
        } else {
          _tmpFromLatitude = _cursor.getString(_cursorIndexOfFromLatitude);
        }
        _item.setFromLatitude(_tmpFromLatitude);
        final String _tmpFromLongitude;
        if (_cursor.isNull(_cursorIndexOfFromLongitude)) {
          _tmpFromLongitude = null;
        } else {
          _tmpFromLongitude = _cursor.getString(_cursorIndexOfFromLongitude);
        }
        _item.setFromLongitude(_tmpFromLongitude);
        final String _tmpToLatitude;
        if (_cursor.isNull(_cursorIndexOfToLatitude)) {
          _tmpToLatitude = null;
        } else {
          _tmpToLatitude = _cursor.getString(_cursorIndexOfToLatitude);
        }
        _item.setToLatitude(_tmpToLatitude);
        final String _tmpToLongitude;
        if (_cursor.isNull(_cursorIndexOfToLongitude)) {
          _tmpToLongitude = null;
        } else {
          _tmpToLongitude = _cursor.getString(_cursorIndexOfToLongitude);
        }
        _item.setToLongitude(_tmpToLongitude);
        final String _tmpTransportMode;
        if (_cursor.isNull(_cursorIndexOfTransportMode)) {
          _tmpTransportMode = null;
        } else {
          _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
        }
        _item.setTransportMode(_tmpTransportMode);
        final Double _tmpDistance;
        if (_cursor.isNull(_cursorIndexOfDistance)) {
          _tmpDistance = null;
        } else {
          _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        }
        _item.setDistance(_tmpDistance);
        final Long _tmpDuration;
        if (_cursor.isNull(_cursorIndexOfDuration)) {
          _tmpDuration = null;
        } else {
          _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
        }
        _item.setDuration(_tmpDuration);
        final BigDecimal _tmpCost;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfCost)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfCost);
        }
        _tmpCost = BigDecimalConverter.fromString(_tmp);
        _item.setCost(_tmpCost);
        final String _tmpTrafficStatus;
        if (_cursor.isNull(_cursorIndexOfTrafficStatus)) {
          _tmpTrafficStatus = null;
        } else {
          _tmpTrafficStatus = _cursor.getString(_cursorIndexOfTrafficStatus);
        }
        _item.setTrafficStatus(_tmpTrafficStatus);
        final String _tmpRouteData;
        if (_cursor.isNull(_cursorIndexOfRouteData)) {
          _tmpRouteData = null;
        } else {
          _tmpRouteData = _cursor.getString(_cursorIndexOfRouteData);
        }
        _item.setRouteData(_tmpRouteData);
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
  public LiveData<List<TransportPlanEntity>> getByDayIdAndTransportMode(final Long dayId,
      final String transportMode) {
    final String _sql = "SELECT * FROM transport_plans WHERE dayId = ? AND transportMode = ? ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (dayId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, dayId);
    }
    _argIndex = 2;
    if (transportMode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, transportMode);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transport_plans"}, false, new Callable<List<TransportPlanEntity>>() {
      @Override
      @Nullable
      public List<TransportPlanEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "planId");
          final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
          final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
          final int _cursorIndexOfFromAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "fromAddress");
          final int _cursorIndexOfToAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "toAddress");
          final int _cursorIndexOfFromLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLatitude");
          final int _cursorIndexOfFromLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLongitude");
          final int _cursorIndexOfToLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLatitude");
          final int _cursorIndexOfToLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "toLongitude");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
          final int _cursorIndexOfTrafficStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "trafficStatus");
          final int _cursorIndexOfRouteData = CursorUtil.getColumnIndexOrThrow(_cursor, "routeData");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final List<TransportPlanEntity> _result = new ArrayList<TransportPlanEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransportPlanEntity _item;
            _item = new TransportPlanEntity();
            final String _tmpPlanId;
            if (_cursor.isNull(_cursorIndexOfPlanId)) {
              _tmpPlanId = null;
            } else {
              _tmpPlanId = _cursor.getString(_cursorIndexOfPlanId);
            }
            _item.setPlanId(_tmpPlanId);
            final Long _tmpItineraryId;
            if (_cursor.isNull(_cursorIndexOfItineraryId)) {
              _tmpItineraryId = null;
            } else {
              _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
            }
            _item.setItineraryId(_tmpItineraryId);
            final Long _tmpDayId;
            if (_cursor.isNull(_cursorIndexOfDayId)) {
              _tmpDayId = null;
            } else {
              _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
            }
            _item.setDayId(_tmpDayId);
            final String _tmpFromAddress;
            if (_cursor.isNull(_cursorIndexOfFromAddress)) {
              _tmpFromAddress = null;
            } else {
              _tmpFromAddress = _cursor.getString(_cursorIndexOfFromAddress);
            }
            _item.setFromAddress(_tmpFromAddress);
            final String _tmpToAddress;
            if (_cursor.isNull(_cursorIndexOfToAddress)) {
              _tmpToAddress = null;
            } else {
              _tmpToAddress = _cursor.getString(_cursorIndexOfToAddress);
            }
            _item.setToAddress(_tmpToAddress);
            final String _tmpFromLatitude;
            if (_cursor.isNull(_cursorIndexOfFromLatitude)) {
              _tmpFromLatitude = null;
            } else {
              _tmpFromLatitude = _cursor.getString(_cursorIndexOfFromLatitude);
            }
            _item.setFromLatitude(_tmpFromLatitude);
            final String _tmpFromLongitude;
            if (_cursor.isNull(_cursorIndexOfFromLongitude)) {
              _tmpFromLongitude = null;
            } else {
              _tmpFromLongitude = _cursor.getString(_cursorIndexOfFromLongitude);
            }
            _item.setFromLongitude(_tmpFromLongitude);
            final String _tmpToLatitude;
            if (_cursor.isNull(_cursorIndexOfToLatitude)) {
              _tmpToLatitude = null;
            } else {
              _tmpToLatitude = _cursor.getString(_cursorIndexOfToLatitude);
            }
            _item.setToLatitude(_tmpToLatitude);
            final String _tmpToLongitude;
            if (_cursor.isNull(_cursorIndexOfToLongitude)) {
              _tmpToLongitude = null;
            } else {
              _tmpToLongitude = _cursor.getString(_cursorIndexOfToLongitude);
            }
            _item.setToLongitude(_tmpToLongitude);
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            _item.setTransportMode(_tmpTransportMode);
            final Double _tmpDistance;
            if (_cursor.isNull(_cursorIndexOfDistance)) {
              _tmpDistance = null;
            } else {
              _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            }
            _item.setDistance(_tmpDistance);
            final Long _tmpDuration;
            if (_cursor.isNull(_cursorIndexOfDuration)) {
              _tmpDuration = null;
            } else {
              _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            }
            _item.setDuration(_tmpDuration);
            final BigDecimal _tmpCost;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCost)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCost);
            }
            _tmpCost = BigDecimalConverter.fromString(_tmp);
            _item.setCost(_tmpCost);
            final String _tmpTrafficStatus;
            if (_cursor.isNull(_cursorIndexOfTrafficStatus)) {
              _tmpTrafficStatus = null;
            } else {
              _tmpTrafficStatus = _cursor.getString(_cursorIndexOfTrafficStatus);
            }
            _item.setTrafficStatus(_tmpTrafficStatus);
            final String _tmpRouteData;
            if (_cursor.isNull(_cursorIndexOfRouteData)) {
              _tmpRouteData = null;
            } else {
              _tmpRouteData = _cursor.getString(_cursorIndexOfRouteData);
            }
            _item.setRouteData(_tmpRouteData);
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getCountByDayId(final Long dayId) {
    final String _sql = "SELECT COUNT(*) FROM transport_plans WHERE dayId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dayId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, dayId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transport_plans"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
