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
import com.travelplanner.data.local.entity.TodoEntity;
import java.lang.Class;
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
public final class TodoDao_Impl implements TodoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TodoEntity> __insertionAdapterOfTodoEntity;

  private final EntityDeletionOrUpdateAdapter<TodoEntity> __deletionAdapterOfTodoEntity;

  private final EntityDeletionOrUpdateAdapter<TodoEntity> __updateAdapterOfTodoEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByTeamId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAssignee;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePriority;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDueDate;

  public TodoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTodoEntity = new EntityInsertionAdapter<TodoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `team_todos` (`id`,`serverId`,`teamId`,`title`,`content`,`priority`,`dueDate`,`assigneeId`,`assigneeName`,`creatorId`,`creatorName`,`status`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TodoEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getServerId());
        }
        if (entity.getTeamId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getTeamId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getContent());
        }
        if (entity.getPriority() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPriority());
        }
        if (entity.getDueDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDueDate());
        }
        if (entity.getAssigneeId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getAssigneeId());
        }
        if (entity.getAssigneeName() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAssigneeName());
        }
        if (entity.getCreatorId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCreatorId());
        }
        if (entity.getCreatorName() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCreatorName());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getStatus());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getUpdateTime());
        }
      }
    };
    this.__deletionAdapterOfTodoEntity = new EntityDeletionOrUpdateAdapter<TodoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `team_todos` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TodoEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfTodoEntity = new EntityDeletionOrUpdateAdapter<TodoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `team_todos` SET `id` = ?,`serverId` = ?,`teamId` = ?,`title` = ?,`content` = ?,`priority` = ?,`dueDate` = ?,`assigneeId` = ?,`assigneeName` = ?,`creatorId` = ?,`creatorName` = ?,`status` = ?,`createTime` = ?,`updateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TodoEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getServerId());
        }
        if (entity.getTeamId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getTeamId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTitle());
        }
        if (entity.getContent() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getContent());
        }
        if (entity.getPriority() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPriority());
        }
        if (entity.getDueDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDueDate());
        }
        if (entity.getAssigneeId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getAssigneeId());
        }
        if (entity.getAssigneeName() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAssigneeName());
        }
        if (entity.getCreatorId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCreatorId());
        }
        if (entity.getCreatorName() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCreatorName());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getStatus());
        }
        if (entity.getCreateTime() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getCreateTime());
        }
        if (entity.getUpdateTime() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getUpdateTime());
        }
        if (entity.getId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM team_todos WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByTeamId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM team_todos WHERE teamId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE team_todos SET status = ?, updateTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAssignee = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE team_todos SET assigneeId = ?, assigneeName = ?, updateTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePriority = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE team_todos SET priority = ?, updateTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDueDate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE team_todos SET dueDate = ?, updateTime = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final TodoEntity todo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfTodoEntity.insertAndReturnId(todo);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<TodoEntity> todos) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTodoEntity.insert(todos);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final TodoEntity todo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTodoEntity.handle(todo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final TodoEntity todo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTodoEntity.handle(todo);
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
  public void deleteByTeamId(final Long teamId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByTeamId.acquire();
    int _argIndex = 1;
    if (teamId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, teamId);
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
      __preparedStmtOfDeleteByTeamId.release(_stmt);
    }
  }

  @Override
  public void updateStatus(final Long id, final String status, final Long updateTime) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
    int _argIndex = 1;
    if (status == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, status);
    }
    _argIndex = 2;
    if (updateTime == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, updateTime);
    }
    _argIndex = 3;
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
  public void updateAssignee(final Long id, final Long assigneeId, final String assigneeName,
      final Long updateTime) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAssignee.acquire();
    int _argIndex = 1;
    if (assigneeId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, assigneeId);
    }
    _argIndex = 2;
    if (assigneeName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, assigneeName);
    }
    _argIndex = 3;
    if (updateTime == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, updateTime);
    }
    _argIndex = 4;
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
      __preparedStmtOfUpdateAssignee.release(_stmt);
    }
  }

  @Override
  public void updatePriority(final Long id, final String priority, final Long updateTime) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePriority.acquire();
    int _argIndex = 1;
    if (priority == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, priority);
    }
    _argIndex = 2;
    if (updateTime == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, updateTime);
    }
    _argIndex = 3;
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
      __preparedStmtOfUpdatePriority.release(_stmt);
    }
  }

  @Override
  public void updateDueDate(final Long id, final String dueDate, final Long updateTime) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDueDate.acquire();
    int _argIndex = 1;
    if (dueDate == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, dueDate);
    }
    _argIndex = 2;
    if (updateTime == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, updateTime);
    }
    _argIndex = 3;
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
      __preparedStmtOfUpdateDueDate.release(_stmt);
    }
  }

  @Override
  public TodoEntity getById(final Long id) {
    final String _sql = "SELECT * FROM team_todos WHERE id = ?";
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
      final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
      final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
      final int _cursorIndexOfAssigneeId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeId");
      final int _cursorIndexOfAssigneeName = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeName");
      final int _cursorIndexOfCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorId");
      final int _cursorIndexOfCreatorName = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorName");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final TodoEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new TodoEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final Long _tmpServerId;
        if (_cursor.isNull(_cursorIndexOfServerId)) {
          _tmpServerId = null;
        } else {
          _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
        }
        _result.setServerId(_tmpServerId);
        final Long _tmpTeamId;
        if (_cursor.isNull(_cursorIndexOfTeamId)) {
          _tmpTeamId = null;
        } else {
          _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
        }
        _result.setTeamId(_tmpTeamId);
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
        final String _tmpPriority;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmpPriority = null;
        } else {
          _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
        }
        _result.setPriority(_tmpPriority);
        final String _tmpDueDate;
        if (_cursor.isNull(_cursorIndexOfDueDate)) {
          _tmpDueDate = null;
        } else {
          _tmpDueDate = _cursor.getString(_cursorIndexOfDueDate);
        }
        _result.setDueDate(_tmpDueDate);
        final Long _tmpAssigneeId;
        if (_cursor.isNull(_cursorIndexOfAssigneeId)) {
          _tmpAssigneeId = null;
        } else {
          _tmpAssigneeId = _cursor.getLong(_cursorIndexOfAssigneeId);
        }
        _result.setAssigneeId(_tmpAssigneeId);
        final String _tmpAssigneeName;
        if (_cursor.isNull(_cursorIndexOfAssigneeName)) {
          _tmpAssigneeName = null;
        } else {
          _tmpAssigneeName = _cursor.getString(_cursorIndexOfAssigneeName);
        }
        _result.setAssigneeName(_tmpAssigneeName);
        final Long _tmpCreatorId;
        if (_cursor.isNull(_cursorIndexOfCreatorId)) {
          _tmpCreatorId = null;
        } else {
          _tmpCreatorId = _cursor.getLong(_cursorIndexOfCreatorId);
        }
        _result.setCreatorId(_tmpCreatorId);
        final String _tmpCreatorName;
        if (_cursor.isNull(_cursorIndexOfCreatorName)) {
          _tmpCreatorName = null;
        } else {
          _tmpCreatorName = _cursor.getString(_cursorIndexOfCreatorName);
        }
        _result.setCreatorName(_tmpCreatorName);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
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
  public List<TodoEntity> getByTeamId(final Long teamId) {
    final String _sql = "SELECT * FROM team_todos WHERE teamId = ? ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, teamId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
      final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
      final int _cursorIndexOfAssigneeId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeId");
      final int _cursorIndexOfAssigneeName = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeName");
      final int _cursorIndexOfCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorId");
      final int _cursorIndexOfCreatorName = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorName");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<TodoEntity> _result = new ArrayList<TodoEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TodoEntity _item;
        _item = new TodoEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Long _tmpServerId;
        if (_cursor.isNull(_cursorIndexOfServerId)) {
          _tmpServerId = null;
        } else {
          _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
        }
        _item.setServerId(_tmpServerId);
        final Long _tmpTeamId;
        if (_cursor.isNull(_cursorIndexOfTeamId)) {
          _tmpTeamId = null;
        } else {
          _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
        }
        _item.setTeamId(_tmpTeamId);
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
        final String _tmpPriority;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmpPriority = null;
        } else {
          _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
        }
        _item.setPriority(_tmpPriority);
        final String _tmpDueDate;
        if (_cursor.isNull(_cursorIndexOfDueDate)) {
          _tmpDueDate = null;
        } else {
          _tmpDueDate = _cursor.getString(_cursorIndexOfDueDate);
        }
        _item.setDueDate(_tmpDueDate);
        final Long _tmpAssigneeId;
        if (_cursor.isNull(_cursorIndexOfAssigneeId)) {
          _tmpAssigneeId = null;
        } else {
          _tmpAssigneeId = _cursor.getLong(_cursorIndexOfAssigneeId);
        }
        _item.setAssigneeId(_tmpAssigneeId);
        final String _tmpAssigneeName;
        if (_cursor.isNull(_cursorIndexOfAssigneeName)) {
          _tmpAssigneeName = null;
        } else {
          _tmpAssigneeName = _cursor.getString(_cursorIndexOfAssigneeName);
        }
        _item.setAssigneeName(_tmpAssigneeName);
        final Long _tmpCreatorId;
        if (_cursor.isNull(_cursorIndexOfCreatorId)) {
          _tmpCreatorId = null;
        } else {
          _tmpCreatorId = _cursor.getLong(_cursorIndexOfCreatorId);
        }
        _item.setCreatorId(_tmpCreatorId);
        final String _tmpCreatorName;
        if (_cursor.isNull(_cursorIndexOfCreatorName)) {
          _tmpCreatorName = null;
        } else {
          _tmpCreatorName = _cursor.getString(_cursorIndexOfCreatorName);
        }
        _item.setCreatorName(_tmpCreatorName);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
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
  public List<TodoEntity> getByTeamIdAndAssignee(final Long teamId, final Long assigneeId) {
    final String _sql = "SELECT * FROM team_todos WHERE teamId = ? AND assigneeId = ? ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, teamId);
    }
    _argIndex = 2;
    if (assigneeId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, assigneeId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
      final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
      final int _cursorIndexOfAssigneeId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeId");
      final int _cursorIndexOfAssigneeName = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeName");
      final int _cursorIndexOfCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorId");
      final int _cursorIndexOfCreatorName = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorName");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<TodoEntity> _result = new ArrayList<TodoEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TodoEntity _item;
        _item = new TodoEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Long _tmpServerId;
        if (_cursor.isNull(_cursorIndexOfServerId)) {
          _tmpServerId = null;
        } else {
          _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
        }
        _item.setServerId(_tmpServerId);
        final Long _tmpTeamId;
        if (_cursor.isNull(_cursorIndexOfTeamId)) {
          _tmpTeamId = null;
        } else {
          _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
        }
        _item.setTeamId(_tmpTeamId);
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
        final String _tmpPriority;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmpPriority = null;
        } else {
          _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
        }
        _item.setPriority(_tmpPriority);
        final String _tmpDueDate;
        if (_cursor.isNull(_cursorIndexOfDueDate)) {
          _tmpDueDate = null;
        } else {
          _tmpDueDate = _cursor.getString(_cursorIndexOfDueDate);
        }
        _item.setDueDate(_tmpDueDate);
        final Long _tmpAssigneeId;
        if (_cursor.isNull(_cursorIndexOfAssigneeId)) {
          _tmpAssigneeId = null;
        } else {
          _tmpAssigneeId = _cursor.getLong(_cursorIndexOfAssigneeId);
        }
        _item.setAssigneeId(_tmpAssigneeId);
        final String _tmpAssigneeName;
        if (_cursor.isNull(_cursorIndexOfAssigneeName)) {
          _tmpAssigneeName = null;
        } else {
          _tmpAssigneeName = _cursor.getString(_cursorIndexOfAssigneeName);
        }
        _item.setAssigneeName(_tmpAssigneeName);
        final Long _tmpCreatorId;
        if (_cursor.isNull(_cursorIndexOfCreatorId)) {
          _tmpCreatorId = null;
        } else {
          _tmpCreatorId = _cursor.getLong(_cursorIndexOfCreatorId);
        }
        _item.setCreatorId(_tmpCreatorId);
        final String _tmpCreatorName;
        if (_cursor.isNull(_cursorIndexOfCreatorName)) {
          _tmpCreatorName = null;
        } else {
          _tmpCreatorName = _cursor.getString(_cursorIndexOfCreatorName);
        }
        _item.setCreatorName(_tmpCreatorName);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
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
  public List<TodoEntity> getByTeamIdAndStatus(final Long teamId, final String status) {
    final String _sql = "SELECT * FROM team_todos WHERE teamId = ? AND status = ? ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, teamId);
    }
    _argIndex = 2;
    if (status == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, status);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
      final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
      final int _cursorIndexOfAssigneeId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeId");
      final int _cursorIndexOfAssigneeName = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeName");
      final int _cursorIndexOfCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorId");
      final int _cursorIndexOfCreatorName = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorName");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<TodoEntity> _result = new ArrayList<TodoEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TodoEntity _item;
        _item = new TodoEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Long _tmpServerId;
        if (_cursor.isNull(_cursorIndexOfServerId)) {
          _tmpServerId = null;
        } else {
          _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
        }
        _item.setServerId(_tmpServerId);
        final Long _tmpTeamId;
        if (_cursor.isNull(_cursorIndexOfTeamId)) {
          _tmpTeamId = null;
        } else {
          _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
        }
        _item.setTeamId(_tmpTeamId);
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
        final String _tmpPriority;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmpPriority = null;
        } else {
          _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
        }
        _item.setPriority(_tmpPriority);
        final String _tmpDueDate;
        if (_cursor.isNull(_cursorIndexOfDueDate)) {
          _tmpDueDate = null;
        } else {
          _tmpDueDate = _cursor.getString(_cursorIndexOfDueDate);
        }
        _item.setDueDate(_tmpDueDate);
        final Long _tmpAssigneeId;
        if (_cursor.isNull(_cursorIndexOfAssigneeId)) {
          _tmpAssigneeId = null;
        } else {
          _tmpAssigneeId = _cursor.getLong(_cursorIndexOfAssigneeId);
        }
        _item.setAssigneeId(_tmpAssigneeId);
        final String _tmpAssigneeName;
        if (_cursor.isNull(_cursorIndexOfAssigneeName)) {
          _tmpAssigneeName = null;
        } else {
          _tmpAssigneeName = _cursor.getString(_cursorIndexOfAssigneeName);
        }
        _item.setAssigneeName(_tmpAssigneeName);
        final Long _tmpCreatorId;
        if (_cursor.isNull(_cursorIndexOfCreatorId)) {
          _tmpCreatorId = null;
        } else {
          _tmpCreatorId = _cursor.getLong(_cursorIndexOfCreatorId);
        }
        _item.setCreatorId(_tmpCreatorId);
        final String _tmpCreatorName;
        if (_cursor.isNull(_cursorIndexOfCreatorName)) {
          _tmpCreatorName = null;
        } else {
          _tmpCreatorName = _cursor.getString(_cursorIndexOfCreatorName);
        }
        _item.setCreatorName(_tmpCreatorName);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
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
  public List<TodoEntity> getByTeamIdSortedByPriority(final Long teamId) {
    final String _sql = "SELECT * FROM team_todos WHERE teamId = ? ORDER BY CASE priority WHEN 'high' THEN 1 WHEN 'medium' THEN 2 WHEN 'low' THEN 3 ELSE 4 END, createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, teamId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
      final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
      final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
      final int _cursorIndexOfAssigneeId = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeId");
      final int _cursorIndexOfAssigneeName = CursorUtil.getColumnIndexOrThrow(_cursor, "assigneeName");
      final int _cursorIndexOfCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorId");
      final int _cursorIndexOfCreatorName = CursorUtil.getColumnIndexOrThrow(_cursor, "creatorName");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<TodoEntity> _result = new ArrayList<TodoEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TodoEntity _item;
        _item = new TodoEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Long _tmpServerId;
        if (_cursor.isNull(_cursorIndexOfServerId)) {
          _tmpServerId = null;
        } else {
          _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
        }
        _item.setServerId(_tmpServerId);
        final Long _tmpTeamId;
        if (_cursor.isNull(_cursorIndexOfTeamId)) {
          _tmpTeamId = null;
        } else {
          _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
        }
        _item.setTeamId(_tmpTeamId);
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
        final String _tmpPriority;
        if (_cursor.isNull(_cursorIndexOfPriority)) {
          _tmpPriority = null;
        } else {
          _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
        }
        _item.setPriority(_tmpPriority);
        final String _tmpDueDate;
        if (_cursor.isNull(_cursorIndexOfDueDate)) {
          _tmpDueDate = null;
        } else {
          _tmpDueDate = _cursor.getString(_cursorIndexOfDueDate);
        }
        _item.setDueDate(_tmpDueDate);
        final Long _tmpAssigneeId;
        if (_cursor.isNull(_cursorIndexOfAssigneeId)) {
          _tmpAssigneeId = null;
        } else {
          _tmpAssigneeId = _cursor.getLong(_cursorIndexOfAssigneeId);
        }
        _item.setAssigneeId(_tmpAssigneeId);
        final String _tmpAssigneeName;
        if (_cursor.isNull(_cursorIndexOfAssigneeName)) {
          _tmpAssigneeName = null;
        } else {
          _tmpAssigneeName = _cursor.getString(_cursorIndexOfAssigneeName);
        }
        _item.setAssigneeName(_tmpAssigneeName);
        final Long _tmpCreatorId;
        if (_cursor.isNull(_cursorIndexOfCreatorId)) {
          _tmpCreatorId = null;
        } else {
          _tmpCreatorId = _cursor.getLong(_cursorIndexOfCreatorId);
        }
        _item.setCreatorId(_tmpCreatorId);
        final String _tmpCreatorName;
        if (_cursor.isNull(_cursorIndexOfCreatorName)) {
          _tmpCreatorName = null;
        } else {
          _tmpCreatorName = _cursor.getString(_cursorIndexOfCreatorName);
        }
        _item.setCreatorName(_tmpCreatorName);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
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
