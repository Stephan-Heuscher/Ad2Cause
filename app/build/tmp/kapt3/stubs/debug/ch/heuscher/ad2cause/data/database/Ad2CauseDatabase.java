package ch.heuscher.ad2cause.data.database;

/**
 * Room Database for the Ad2Cause application.
 * Manages all local data persistence.
 *
 * Database Schema:
 * - causes: Stores information about all causes (pre-defined and user-added)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lch/heuscher/ad2cause/data/database/Ad2CauseDatabase;", "Landroidx/room/RoomDatabase;", "()V", "causeDao", "Lch/heuscher/ad2cause/data/database/CauseDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {ch.heuscher.ad2cause.data.models.Cause.class}, version = 1, exportSchema = false)
public abstract class Ad2CauseDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile ch.heuscher.ad2cause.data.database.Ad2CauseDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final ch.heuscher.ad2cause.data.database.Ad2CauseDatabase.Companion Companion = null;
    
    public Ad2CauseDatabase() {
        super();
    }
    
    /**
     * Abstract method to access the CauseDao.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract ch.heuscher.ad2cause.data.database.CauseDao causeDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lch/heuscher/ad2cause/data/database/Ad2CauseDatabase$Companion;", "", "()V", "INSTANCE", "Lch/heuscher/ad2cause/data/database/Ad2CauseDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Singleton pattern to get database instance.
         * Uses double-checked locking for thread safety.
         */
        @org.jetbrains.annotations.NotNull()
        public final ch.heuscher.ad2cause.data.database.Ad2CauseDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}