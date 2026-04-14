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
import com.travelplanner.data.local.entity.ItineraryDayEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ItineraryDayDao_Impl implements ItineraryDayDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ItineraryDayEntity> __insertionAdapterOfItineraryDayEntity;

  private final EntityDeletionOrUpdateAdapter<ItineraryDayEntity> __deletionAdapterOfItineraryDayEntity;

  private final EntityDeletionOrUpdateAdapter<ItineraryDayEntity> __updateAdapterOfItineraryDayEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByItineraryId;

  public ItineraryDayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItineraryDayEntity = new EntityInsertionAdapter<ItineraryDayEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `itinerary_days` (`id`,`itineraryId`,`dayNumber`,`date`,`notes`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ItineraryDayEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getItineraryId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getItineraryId());
        }
        if (entity.getDayNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getDayNumber());
        }
        if (entity.getDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDate());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotes());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getUpdateTime());
        }
      }
    };
    this.__deletionAdapterOfItineraryDayEntity = new EntityDeletionOrUpdateAdapter<ItineraryDayEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `itinerary_days` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ItineraryDayEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfItineraryDayEntity = new EntityDeletionOrUpdateAdapter<ItineraryDayEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `itinerary_days` SET `id` = ?,`itineraryId` = ?,`dayNumber` = ?,`date` = ?,`notes` = ?,`createTime` = ?,`updateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ItineraryDayEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getItineraryId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getItineraryId());
        }
        if (entity.getDayNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getDayNumber());
        }
        if (entity.getDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDate());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotes());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getUpdateTime());
        }
        if (entity.getId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteByItineraryId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM itinerary_days WHERE itineraryId = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final ItineraryDayEntity itineraryDay) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfItineraryDayEntity.insertAndReturnId(itineraryDay);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ItineraryDayEntity itineraryDay) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfItineraryDayEntity.handle(itineraryDay);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ItineraryDayEntity itineraryDay) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfItineraryDayEntity.handle(itineraryDay);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
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
  public LiveData<ItineraryDayEntity> getById(final Long id) {
    final String _sql = "SELECT * FROM itinerary_days WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, id);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"itinerary_days"}, false, new Callable<ItineraryDayEntity>() {
      @Override
      @Nullable
      public ItineraryDayEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
          final int _cursorIndexOfDayNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "dayNumber");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final ItineraryDayEntity _result;
          if (_cursor.moveToFirst()) {
            _result = new ItineraryDayEntity();
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _result.setId(_tmpId);
            final Long _tmpItineraryId;
            if (_cursor.isNull(_cursorIndexOfItineraryId)) {
              _tmpItineraryId = null;
            } else {
              _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
            }
            _result.setItineraryId(_tmpItineraryId);
            final Integer _tmpDayNumber;
            if (_cursor.isNull(_cursorIndexOfDayNumber)) {
              _tmpDayNumber = null;
            } else {
              _tmpDayNumber = _cursor.getInt(_cursorIndexOfDayNumber);
            }
            _result.setDayNumber(_tmpDayNumber);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _result.setDate(_tmpDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result.setNotes(_tmpNotes);
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
  public ItineraryDayEntity getByIdSync(final Long id) {
    final String _sql = "SELECT * FROM itinerary_days WHERE id = ?";
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
      final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
      final int _cursorIndexOfDayNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "dayNumber");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final ItineraryDayEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new ItineraryDayEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final Long _tmpItineraryId;
        if (_cursor.isNull(_cursorIndexOfItineraryId)) {
          _tmpItineraryId = null;
        } else {
          _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
        }
        _result.setItineraryId(_tmpItineraryId);
        final Integer _tmpDayNumber;
        if (_cursor.isNull(_cursorIndexOfDayNumber)) {
          _tmpDayNumber = null;
        } else {
          _tmpDayNumber = _cursor.getInt(_cursorIndexOfDayNumber);
        }
        _result.setDayNumber(_tmpDayNumber);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _result.setDate(_tmpDate);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
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
  public LiveData<List<ItineraryDayEntity>> getByItineraryId(final Long itineraryId) {
    final String _sql = "SELECT * FROM itinerary_days WHERE itineraryId = ? ORDER BY dayNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itineraryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, itineraryId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"itinerary_days"}, false, new Callable<List<ItineraryDayEntity>>() {
      @Override
      @Nullable
      public List<ItineraryDayEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
          final int _cursorIndexOfDayNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "dayNumber");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final List<ItineraryDayEntity> _result = new ArrayList<ItineraryDayEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ItineraryDayEntity _item;
            _item = new ItineraryDayEntity();
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final Long _tmpItineraryId;
            if (_cursor.isNull(_cursorIndexOfItineraryId)) {
              _tmpItineraryId = null;
            } else {
              _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
            }
            _item.setItineraryId(_tmpItineraryId);
            final Integer _tmpDayNumber;
            if (_cursor.isNull(_cursorIndexOfDayNumber)) {
              _tmpDayNumber = null;
            } else {
              _tmpDayNumber = _cursor.getInt(_cursorIndexOfDayNumber);
            }
            _item.setDayNumber(_tmpDayNumber);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _item.setDate(_tmpDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
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
  public List<ItineraryDayEntity> getByItineraryIdSync(final Long itineraryId) {
    final String _sql = "SELECT * FROM itinerary_days WHERE itineraryId = ? ORDER BY dayNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itineraryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, itineraryId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
      final int _cursorIndexOfDayNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "dayNumber");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ItineraryDayEntity> _result = new ArrayList<ItineraryDayEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ItineraryDayEntity _item;
        _item = new ItineraryDayEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Long _tmpItineraryId;
        if (_cursor.isNull(_cursorIndexOfItineraryId)) {
          _tmpItineraryId = null;
        } else {
          _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
        }
        _item.setItineraryId(_tmpItineraryId);
        final Integer _tmpDayNumber;
        if (_cursor.isNull(_cursorIndexOfDayNumber)) {
          _tmpDayNumber = null;
        } else {
          _tmpDayNumber = _cursor.getInt(_cursorIndexOfDayNumber);
        }
        _item.setDayNumber(_tmpDayNumber);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _item.setDate(_tmpDate);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
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
  public LiveData<ItineraryDayEntity> getByItineraryIdAndDayNumber(final Long itineraryId,
      final Integer dayNumber) {
    final String _sql = "SELECT * FROM itinerary_days WHERE itineraryId = ? AND dayNumber = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (itineraryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, itineraryId);
    }
    _argIndex = 2;
    if (dayNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, dayNumber);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"itinerary_days"}, false, new Callable<ItineraryDayEntity>() {
      @Override
      @Nullable
      public ItineraryDayEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
          final int _cursorIndexOfDayNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "dayNumber");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final ItineraryDayEntity _result;
          if (_cursor.moveToFirst()) {
            _result = new ItineraryDayEntity();
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _result.setId(_tmpId);
            final Long _tmpItineraryId;
            if (_cursor.isNull(_cursorIndexOfItineraryId)) {
              _tmpItineraryId = null;
            } else {
              _tmpItineraryId = _cursor.getLong(_cursorIndexOfItineraryId);
            }
            _result.setItineraryId(_tmpItineraryId);
            final Integer _tmpDayNumber;
            if (_cursor.isNull(_cursorIndexOfDayNumber)) {
              _tmpDayNumber = null;
            } else {
              _tmpDayNumber = _cursor.getInt(_cursorIndexOfDayNumber);
            }
            _result.setDayNumber(_tmpDayNumber);
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            _result.setDate(_tmpDate);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result.setNotes(_tmpNotes);
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
  public LiveData<Integer> getCountByItineraryId(final Long itineraryId) {
    final String _sql = "SELECT COUNT(*) FROM itinerary_days WHERE itineraryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (itineraryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, itineraryId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"itinerary_days"}, false, new Callable<Integer>() {
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
