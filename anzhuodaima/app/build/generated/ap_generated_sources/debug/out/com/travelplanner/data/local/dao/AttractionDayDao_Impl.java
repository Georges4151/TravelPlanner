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
import com.travelplanner.data.local.entity.AttractionDayEntity;
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
public final class AttractionDayDao_Impl implements AttractionDayDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AttractionDayEntity> __insertionAdapterOfAttractionDayEntity;

  private final EntityDeletionOrUpdateAdapter<AttractionDayEntity> __deletionAdapterOfAttractionDayEntity;

  private final EntityDeletionOrUpdateAdapter<AttractionDayEntity> __updateAdapterOfAttractionDayEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByDayId;

  public AttractionDayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAttractionDayEntity = new EntityInsertionAdapter<AttractionDayEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `attraction_days` (`id`,`dayId`,`attractionId`,`visitOrder`,`plannedStartTime`,`plannedEndTime`,`notes`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AttractionDayEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getDayId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getDayId());
        }
        if (entity.getAttractionId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getAttractionId());
        }
        if (entity.getVisitOrder() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getVisitOrder());
        }
        if (entity.getPlannedStartTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPlannedStartTime());
        }
        if (entity.getPlannedEndTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPlannedEndTime());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getUpdateTime());
        }
      }
    };
    this.__deletionAdapterOfAttractionDayEntity = new EntityDeletionOrUpdateAdapter<AttractionDayEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `attraction_days` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AttractionDayEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfAttractionDayEntity = new EntityDeletionOrUpdateAdapter<AttractionDayEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `attraction_days` SET `id` = ?,`dayId` = ?,`attractionId` = ?,`visitOrder` = ?,`plannedStartTime` = ?,`plannedEndTime` = ?,`notes` = ?,`createTime` = ?,`updateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AttractionDayEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getDayId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getDayId());
        }
        if (entity.getAttractionId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getAttractionId());
        }
        if (entity.getVisitOrder() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getVisitOrder());
        }
        if (entity.getPlannedStartTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPlannedStartTime());
        }
        if (entity.getPlannedEndTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPlannedEndTime());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getUpdateTime());
        }
        if (entity.getId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteByDayId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attraction_days WHERE dayId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final AttractionDayEntity attractionDay) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAttractionDayEntity.insert(attractionDay);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final AttractionDayEntity attractionDay) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAttractionDayEntity.handle(attractionDay);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final AttractionDayEntity attractionDay) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfAttractionDayEntity.handle(attractionDay);
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
  public LiveData<AttractionDayEntity> getById(final Long id) {
    final String _sql = "SELECT * FROM attraction_days WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, id);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"attraction_days"}, false, new Callable<AttractionDayEntity>() {
      @Override
      @Nullable
      public AttractionDayEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfVisitOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "visitOrder");
          final int _cursorIndexOfPlannedStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedStartTime");
          final int _cursorIndexOfPlannedEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedEndTime");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final AttractionDayEntity _result;
          if (_cursor.moveToFirst()) {
            _result = new AttractionDayEntity();
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _result.setId(_tmpId);
            final Long _tmpDayId;
            if (_cursor.isNull(_cursorIndexOfDayId)) {
              _tmpDayId = null;
            } else {
              _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
            }
            _result.setDayId(_tmpDayId);
            final Long _tmpAttractionId;
            if (_cursor.isNull(_cursorIndexOfAttractionId)) {
              _tmpAttractionId = null;
            } else {
              _tmpAttractionId = _cursor.getLong(_cursorIndexOfAttractionId);
            }
            _result.setAttractionId(_tmpAttractionId);
            final Integer _tmpVisitOrder;
            if (_cursor.isNull(_cursorIndexOfVisitOrder)) {
              _tmpVisitOrder = null;
            } else {
              _tmpVisitOrder = _cursor.getInt(_cursorIndexOfVisitOrder);
            }
            _result.setVisitOrder(_tmpVisitOrder);
            final String _tmpPlannedStartTime;
            if (_cursor.isNull(_cursorIndexOfPlannedStartTime)) {
              _tmpPlannedStartTime = null;
            } else {
              _tmpPlannedStartTime = _cursor.getString(_cursorIndexOfPlannedStartTime);
            }
            _result.setPlannedStartTime(_tmpPlannedStartTime);
            final String _tmpPlannedEndTime;
            if (_cursor.isNull(_cursorIndexOfPlannedEndTime)) {
              _tmpPlannedEndTime = null;
            } else {
              _tmpPlannedEndTime = _cursor.getString(_cursorIndexOfPlannedEndTime);
            }
            _result.setPlannedEndTime(_tmpPlannedEndTime);
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
  public LiveData<List<AttractionDayEntity>> getByDayId(final Long dayId) {
    final String _sql = "SELECT * FROM attraction_days WHERE dayId = ? ORDER BY visitOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dayId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, dayId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"attraction_days"}, false, new Callable<List<AttractionDayEntity>>() {
      @Override
      @Nullable
      public List<AttractionDayEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
          final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
          final int _cursorIndexOfVisitOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "visitOrder");
          final int _cursorIndexOfPlannedStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedStartTime");
          final int _cursorIndexOfPlannedEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedEndTime");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
          final List<AttractionDayEntity> _result = new ArrayList<AttractionDayEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttractionDayEntity _item;
            _item = new AttractionDayEntity();
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            final Long _tmpDayId;
            if (_cursor.isNull(_cursorIndexOfDayId)) {
              _tmpDayId = null;
            } else {
              _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
            }
            _item.setDayId(_tmpDayId);
            final Long _tmpAttractionId;
            if (_cursor.isNull(_cursorIndexOfAttractionId)) {
              _tmpAttractionId = null;
            } else {
              _tmpAttractionId = _cursor.getLong(_cursorIndexOfAttractionId);
            }
            _item.setAttractionId(_tmpAttractionId);
            final Integer _tmpVisitOrder;
            if (_cursor.isNull(_cursorIndexOfVisitOrder)) {
              _tmpVisitOrder = null;
            } else {
              _tmpVisitOrder = _cursor.getInt(_cursorIndexOfVisitOrder);
            }
            _item.setVisitOrder(_tmpVisitOrder);
            final String _tmpPlannedStartTime;
            if (_cursor.isNull(_cursorIndexOfPlannedStartTime)) {
              _tmpPlannedStartTime = null;
            } else {
              _tmpPlannedStartTime = _cursor.getString(_cursorIndexOfPlannedStartTime);
            }
            _item.setPlannedStartTime(_tmpPlannedStartTime);
            final String _tmpPlannedEndTime;
            if (_cursor.isNull(_cursorIndexOfPlannedEndTime)) {
              _tmpPlannedEndTime = null;
            } else {
              _tmpPlannedEndTime = _cursor.getString(_cursorIndexOfPlannedEndTime);
            }
            _item.setPlannedEndTime(_tmpPlannedEndTime);
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
  public List<AttractionDayEntity> getByDayIdSync(final Long dayId) {
    final String _sql = "SELECT * FROM attraction_days WHERE dayId = ? ORDER BY visitOrder ASC";
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
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayId");
      final int _cursorIndexOfAttractionId = CursorUtil.getColumnIndexOrThrow(_cursor, "attractionId");
      final int _cursorIndexOfVisitOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "visitOrder");
      final int _cursorIndexOfPlannedStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedStartTime");
      final int _cursorIndexOfPlannedEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedEndTime");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<AttractionDayEntity> _result = new ArrayList<AttractionDayEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AttractionDayEntity _item;
        _item = new AttractionDayEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Long _tmpDayId;
        if (_cursor.isNull(_cursorIndexOfDayId)) {
          _tmpDayId = null;
        } else {
          _tmpDayId = _cursor.getLong(_cursorIndexOfDayId);
        }
        _item.setDayId(_tmpDayId);
        final Long _tmpAttractionId;
        if (_cursor.isNull(_cursorIndexOfAttractionId)) {
          _tmpAttractionId = null;
        } else {
          _tmpAttractionId = _cursor.getLong(_cursorIndexOfAttractionId);
        }
        _item.setAttractionId(_tmpAttractionId);
        final Integer _tmpVisitOrder;
        if (_cursor.isNull(_cursorIndexOfVisitOrder)) {
          _tmpVisitOrder = null;
        } else {
          _tmpVisitOrder = _cursor.getInt(_cursorIndexOfVisitOrder);
        }
        _item.setVisitOrder(_tmpVisitOrder);
        final String _tmpPlannedStartTime;
        if (_cursor.isNull(_cursorIndexOfPlannedStartTime)) {
          _tmpPlannedStartTime = null;
        } else {
          _tmpPlannedStartTime = _cursor.getString(_cursorIndexOfPlannedStartTime);
        }
        _item.setPlannedStartTime(_tmpPlannedStartTime);
        final String _tmpPlannedEndTime;
        if (_cursor.isNull(_cursorIndexOfPlannedEndTime)) {
          _tmpPlannedEndTime = null;
        } else {
          _tmpPlannedEndTime = _cursor.getString(_cursorIndexOfPlannedEndTime);
        }
        _item.setPlannedEndTime(_tmpPlannedEndTime);
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
  public LiveData<Integer> getCountByDayId(final Long dayId) {
    final String _sql = "SELECT COUNT(*) FROM attraction_days WHERE dayId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (dayId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, dayId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"attraction_days"}, false, new Callable<Integer>() {
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
