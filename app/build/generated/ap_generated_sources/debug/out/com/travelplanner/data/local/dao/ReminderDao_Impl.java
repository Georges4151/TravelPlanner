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
import com.travelplanner.data.local.entity.ReminderEntity;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReminderDao_Impl implements ReminderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReminderEntity> __insertionAdapterOfReminderEntity;

  private final EntityDeletionOrUpdateAdapter<ReminderEntity> __deletionAdapterOfReminderEntity;

  private final EntityDeletionOrUpdateAdapter<ReminderEntity> __updateAdapterOfReminderEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByItineraryId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  public ReminderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReminderEntity = new EntityInsertionAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reminders` (`id`,`itineraryId`,`title`,`content`,`remindTime`,`type`,`status`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ReminderEntity entity) {
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
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContent());
        }
        if (entity.getRemindTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getRemindTime());
        }
        if (entity.getType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getType());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getStatus());
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
    this.__deletionAdapterOfReminderEntity = new EntityDeletionOrUpdateAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `reminders` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ReminderEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfReminderEntity = new EntityDeletionOrUpdateAdapter<ReminderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reminders` SET `id` = ?,`itineraryId` = ?,`title` = ?,`content` = ?,`remindTime` = ?,`type` = ?,`status` = ?,`createTime` = ?,`updateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ReminderEntity entity) {
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
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getContent());
        }
        if (entity.getRemindTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getRemindTime());
        }
        if (entity.getType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getType());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getStatus());
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
    this.__preparedStmtOfDeleteByItineraryId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reminders WHERE itineraryId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reminders SET status = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final ReminderEntity reminder) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfReminderEntity.insertAndReturnId(reminder);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<ReminderEntity> reminders) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfReminderEntity.insert(reminders);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ReminderEntity reminder) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfReminderEntity.handle(reminder);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ReminderEntity reminder) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfReminderEntity.handle(reminder);
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
  public ReminderEntity getById(final Long id) {
    final String _sql = "SELECT * FROM reminders WHERE id = ?";
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
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfRemindTime = CursorUtil.getColumnIndexOrThrow(_cursor, "remindTime");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final ReminderEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new ReminderEntity();
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
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _result.setContent(_tmpContent);
        final Long _tmpRemindTime;
        if (_cursor.isNull(_cursorIndexOfRemindTime)) {
          _tmpRemindTime = null;
        } else {
          _tmpRemindTime = _cursor.getLong(_cursorIndexOfRemindTime);
        }
        _result.setRemindTime(_tmpRemindTime);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _result.setType(_tmpType);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
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
  public List<ReminderEntity> getByItineraryId(final Long itineraryId) {
    final String _sql = "SELECT * FROM reminders WHERE itineraryId = ? ORDER BY remindTime ASC";
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
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfRemindTime = CursorUtil.getColumnIndexOrThrow(_cursor, "remindTime");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReminderEntity _item;
        _item = new ReminderEntity();
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
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final Long _tmpRemindTime;
        if (_cursor.isNull(_cursorIndexOfRemindTime)) {
          _tmpRemindTime = null;
        } else {
          _tmpRemindTime = _cursor.getLong(_cursorIndexOfRemindTime);
        }
        _item.setRemindTime(_tmpRemindTime);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
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
  public List<ReminderEntity> getByStatus(final int status) {
    final String _sql = "SELECT * FROM reminders WHERE status = ? ORDER BY remindTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, status);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfRemindTime = CursorUtil.getColumnIndexOrThrow(_cursor, "remindTime");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReminderEntity _item;
        _item = new ReminderEntity();
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
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final Long _tmpRemindTime;
        if (_cursor.isNull(_cursorIndexOfRemindTime)) {
          _tmpRemindTime = null;
        } else {
          _tmpRemindTime = _cursor.getLong(_cursorIndexOfRemindTime);
        }
        _item.setRemindTime(_tmpRemindTime);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
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
  public List<ReminderEntity> getPendingReminders(final long startTime, final long endTime) {
    final String _sql = "SELECT * FROM reminders WHERE remindTime BETWEEN ? AND ? AND status = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfRemindTime = CursorUtil.getColumnIndexOrThrow(_cursor, "remindTime");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReminderEntity _item;
        _item = new ReminderEntity();
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
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final Long _tmpRemindTime;
        if (_cursor.isNull(_cursorIndexOfRemindTime)) {
          _tmpRemindTime = null;
        } else {
          _tmpRemindTime = _cursor.getLong(_cursorIndexOfRemindTime);
        }
        _item.setRemindTime(_tmpRemindTime);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
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
  public List<ReminderEntity> getTriggeredReminders(final long currentTime) {
    final String _sql = "SELECT * FROM reminders WHERE remindTime <= ? AND status = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentTime);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfRemindTime = CursorUtil.getColumnIndexOrThrow(_cursor, "remindTime");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ReminderEntity> _result = new ArrayList<ReminderEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReminderEntity _item;
        _item = new ReminderEntity();
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
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final Long _tmpRemindTime;
        if (_cursor.isNull(_cursorIndexOfRemindTime)) {
          _tmpRemindTime = null;
        } else {
          _tmpRemindTime = _cursor.getLong(_cursorIndexOfRemindTime);
        }
        _item.setRemindTime(_tmpRemindTime);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final Integer _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
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
