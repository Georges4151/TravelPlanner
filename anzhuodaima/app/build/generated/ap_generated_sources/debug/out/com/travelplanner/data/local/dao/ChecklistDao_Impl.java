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
import com.travelplanner.data.local.entity.ChecklistItemEntity;
import java.lang.Boolean;
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
public final class ChecklistDao_Impl implements ChecklistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChecklistItemEntity> __insertionAdapterOfChecklistItemEntity;

  private final EntityDeletionOrUpdateAdapter<ChecklistItemEntity> __deletionAdapterOfChecklistItemEntity;

  private final EntityDeletionOrUpdateAdapter<ChecklistItemEntity> __updateAdapterOfChecklistItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByItineraryId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateChecked;

  public ChecklistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChecklistItemEntity = new EntityInsertionAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `checklist_items` (`id`,`itineraryId`,`itemName`,`category`,`isChecked`,`notes`,`sortOrder`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChecklistItemEntity entity) {
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
        if (entity.getItemName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getItemName());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory());
        }
        final Integer _tmp = entity.getIsChecked() == null ? null : (entity.getIsChecked() ? 1 : 0);
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
        if (entity.getSortOrder() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSortOrder());
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
    this.__deletionAdapterOfChecklistItemEntity = new EntityDeletionOrUpdateAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `checklist_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChecklistItemEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfChecklistItemEntity = new EntityDeletionOrUpdateAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `checklist_items` SET `id` = ?,`itineraryId` = ?,`itemName` = ?,`category` = ?,`isChecked` = ?,`notes` = ?,`sortOrder` = ?,`createTime` = ?,`updateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChecklistItemEntity entity) {
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
        if (entity.getItemName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getItemName());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory());
        }
        final Integer _tmp = entity.getIsChecked() == null ? null : (entity.getIsChecked() ? 1 : 0);
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
        if (entity.getSortOrder() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSortOrder());
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
        final String _query = "DELETE FROM checklist_items WHERE itineraryId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateChecked = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE checklist_items SET isChecked = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final ChecklistItemEntity item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfChecklistItemEntity.insertAndReturnId(item);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<ChecklistItemEntity> items) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChecklistItemEntity.insert(items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ChecklistItemEntity item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfChecklistItemEntity.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ChecklistItemEntity item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfChecklistItemEntity.handle(item);
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
  public void updateChecked(final Long id, final boolean checked) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateChecked.acquire();
    int _argIndex = 1;
    final int _tmp = checked ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
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
      __preparedStmtOfUpdateChecked.release(_stmt);
    }
  }

  @Override
  public ChecklistItemEntity getById(final Long id) {
    final String _sql = "SELECT * FROM checklist_items WHERE id = ?";
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
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfIsChecked = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecked");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final ChecklistItemEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new ChecklistItemEntity();
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
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _result.setItemName(_tmpItemName);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _result.setCategory(_tmpCategory);
        final Boolean _tmpIsChecked;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfIsChecked)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfIsChecked);
        }
        _tmpIsChecked = _tmp == null ? null : _tmp != 0;
        _result.setIsChecked(_tmpIsChecked);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
        final Integer _tmpSortOrder;
        if (_cursor.isNull(_cursorIndexOfSortOrder)) {
          _tmpSortOrder = null;
        } else {
          _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        }
        _result.setSortOrder(_tmpSortOrder);
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
  public List<ChecklistItemEntity> getByItineraryId(final Long itineraryId) {
    final String _sql = "SELECT * FROM checklist_items WHERE itineraryId = ? ORDER BY sortOrder ASC";
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
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfIsChecked = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecked");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ChecklistItemEntity> _result = new ArrayList<ChecklistItemEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ChecklistItemEntity _item;
        _item = new ChecklistItemEntity();
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
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _item.setCategory(_tmpCategory);
        final Boolean _tmpIsChecked;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfIsChecked)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfIsChecked);
        }
        _tmpIsChecked = _tmp == null ? null : _tmp != 0;
        _item.setIsChecked(_tmpIsChecked);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final Integer _tmpSortOrder;
        if (_cursor.isNull(_cursorIndexOfSortOrder)) {
          _tmpSortOrder = null;
        } else {
          _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        }
        _item.setSortOrder(_tmpSortOrder);
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
  public List<ChecklistItemEntity> getByCategory(final Long itineraryId, final String category) {
    final String _sql = "SELECT * FROM checklist_items WHERE itineraryId = ? AND category = ? ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (itineraryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, itineraryId);
    }
    _argIndex = 2;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfItineraryId = CursorUtil.getColumnIndexOrThrow(_cursor, "itineraryId");
      final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfIsChecked = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecked");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfUpdateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "updateTime");
      final List<ChecklistItemEntity> _result = new ArrayList<ChecklistItemEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ChecklistItemEntity _item;
        _item = new ChecklistItemEntity();
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
        final String _tmpItemName;
        if (_cursor.isNull(_cursorIndexOfItemName)) {
          _tmpItemName = null;
        } else {
          _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        }
        _item.setItemName(_tmpItemName);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _item.setCategory(_tmpCategory);
        final Boolean _tmpIsChecked;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfIsChecked)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfIsChecked);
        }
        _tmpIsChecked = _tmp == null ? null : _tmp != 0;
        _item.setIsChecked(_tmpIsChecked);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final Integer _tmpSortOrder;
        if (_cursor.isNull(_cursorIndexOfSortOrder)) {
          _tmpSortOrder = null;
        } else {
          _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
        }
        _item.setSortOrder(_tmpSortOrder);
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
  public List<String> getCategories(final Long itineraryId) {
    final String _sql = "SELECT DISTINCT category FROM checklist_items WHERE itineraryId = ?";
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
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCountByItineraryId(final Long itineraryId) {
    final String _sql = "SELECT COUNT(*) FROM checklist_items WHERE itineraryId = ?";
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
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCheckedCount(final Long itineraryId) {
    final String _sql = "SELECT COUNT(*) FROM checklist_items WHERE itineraryId = ? AND isChecked = 1";
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
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
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
