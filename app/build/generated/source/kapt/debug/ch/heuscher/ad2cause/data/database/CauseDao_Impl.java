package ch.heuscher.ad2cause.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import ch.heuscher.ad2cause.data.models.Cause;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CauseDao_Impl implements CauseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Cause> __insertionAdapterOfCause;

  private final EntityDeletionOrUpdateAdapter<Cause> __deletionAdapterOfCause;

  private final EntityDeletionOrUpdateAdapter<Cause> __updateAdapterOfCause;

  public CauseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCause = new EntityInsertionAdapter<Cause>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `causes` (`id`,`name`,`description`,`category`,`imageUrl`,`isUserAdded`,`totalEarned`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Cause entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getImageUrl());
        }
        final int _tmp = entity.isUserAdded() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindDouble(7, entity.getTotalEarned());
      }
    };
    this.__deletionAdapterOfCause = new EntityDeletionOrUpdateAdapter<Cause>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `causes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Cause entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCause = new EntityDeletionOrUpdateAdapter<Cause>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `causes` SET `id` = ?,`name` = ?,`description` = ?,`category` = ?,`imageUrl` = ?,`isUserAdded` = ?,`totalEarned` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Cause entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCategory());
        }
        if (entity.getImageUrl() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getImageUrl());
        }
        final int _tmp = entity.isUserAdded() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindDouble(7, entity.getTotalEarned());
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertCause(final Cause cause, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCause.insertAndReturnId(cause);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCause(final Cause cause, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCause.handle(cause);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCause(final Cause cause, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCause.handle(cause);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Cause>> getAllCauses() {
    final String _sql = "SELECT * FROM causes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"causes"}, new Callable<List<Cause>>() {
      @Override
      @NonNull
      public List<Cause> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfIsUserAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isUserAdded");
          final int _cursorIndexOfTotalEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEarned");
          final List<Cause> _result = new ArrayList<Cause>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Cause _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final boolean _tmpIsUserAdded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUserAdded);
            _tmpIsUserAdded = _tmp != 0;
            final double _tmpTotalEarned;
            _tmpTotalEarned = _cursor.getDouble(_cursorIndexOfTotalEarned);
            _item = new Cause(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpImageUrl,_tmpIsUserAdded,_tmpTotalEarned);
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
  public Object getAllCausesSync(final Continuation<? super List<Cause>> $completion) {
    final String _sql = "SELECT * FROM causes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Cause>>() {
      @Override
      @NonNull
      public List<Cause> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfIsUserAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isUserAdded");
          final int _cursorIndexOfTotalEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEarned");
          final List<Cause> _result = new ArrayList<Cause>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Cause _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final boolean _tmpIsUserAdded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUserAdded);
            _tmpIsUserAdded = _tmp != 0;
            final double _tmpTotalEarned;
            _tmpTotalEarned = _cursor.getDouble(_cursorIndexOfTotalEarned);
            _item = new Cause(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpImageUrl,_tmpIsUserAdded,_tmpTotalEarned);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCauseById(final int id, final Continuation<? super Cause> $completion) {
    final String _sql = "SELECT * FROM causes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Cause>() {
      @Override
      @Nullable
      public Cause call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfIsUserAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isUserAdded");
          final int _cursorIndexOfTotalEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEarned");
          final Cause _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final boolean _tmpIsUserAdded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUserAdded);
            _tmpIsUserAdded = _tmp != 0;
            final double _tmpTotalEarned;
            _tmpTotalEarned = _cursor.getDouble(_cursorIndexOfTotalEarned);
            _result = new Cause(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpImageUrl,_tmpIsUserAdded,_tmpTotalEarned);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Cause>> searchCausesByName(final String searchQuery) {
    final String _sql = "SELECT * FROM causes WHERE name LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"causes"}, new Callable<List<Cause>>() {
      @Override
      @NonNull
      public List<Cause> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfIsUserAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isUserAdded");
          final int _cursorIndexOfTotalEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEarned");
          final List<Cause> _result = new ArrayList<Cause>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Cause _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final boolean _tmpIsUserAdded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUserAdded);
            _tmpIsUserAdded = _tmp != 0;
            final double _tmpTotalEarned;
            _tmpTotalEarned = _cursor.getDouble(_cursorIndexOfTotalEarned);
            _item = new Cause(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpImageUrl,_tmpIsUserAdded,_tmpTotalEarned);
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
  public Flow<List<Cause>> getUserAddedCauses() {
    final String _sql = "SELECT * FROM causes WHERE isUserAdded = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"causes"}, new Callable<List<Cause>>() {
      @Override
      @NonNull
      public List<Cause> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfIsUserAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isUserAdded");
          final int _cursorIndexOfTotalEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEarned");
          final List<Cause> _result = new ArrayList<Cause>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Cause _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final boolean _tmpIsUserAdded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUserAdded);
            _tmpIsUserAdded = _tmp != 0;
            final double _tmpTotalEarned;
            _tmpTotalEarned = _cursor.getDouble(_cursorIndexOfTotalEarned);
            _item = new Cause(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpImageUrl,_tmpIsUserAdded,_tmpTotalEarned);
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
  public Flow<List<Cause>> getPredefinedCauses() {
    final String _sql = "SELECT * FROM causes WHERE isUserAdded = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"causes"}, new Callable<List<Cause>>() {
      @Override
      @NonNull
      public List<Cause> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfIsUserAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isUserAdded");
          final int _cursorIndexOfTotalEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "totalEarned");
          final List<Cause> _result = new ArrayList<Cause>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Cause _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final boolean _tmpIsUserAdded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUserAdded);
            _tmpIsUserAdded = _tmp != 0;
            final double _tmpTotalEarned;
            _tmpTotalEarned = _cursor.getDouble(_cursorIndexOfTotalEarned);
            _item = new Cause(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpImageUrl,_tmpIsUserAdded,_tmpTotalEarned);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
