package ch.heuscher.ad2cause.data.database;

/**
 * Data Access Object (DAO) for the Cause entity.
 * Provides methods for database operations on causes.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\u0015\u001a\u00020\u0016H\'J\u0016\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0018"}, d2 = {"Lch/heuscher/ad2cause/data/database/CauseDao;", "", "deleteCause", "", "cause", "Lch/heuscher/ad2cause/data/models/Cause;", "(Lch/heuscher/ad2cause/data/models/Cause;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCauses", "Lkotlinx/coroutines/flow/Flow;", "", "getAllCausesSync", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCauseById", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPredefinedCauses", "getUserAddedCauses", "insertCause", "", "searchCausesByName", "searchQuery", "", "updateCause", "app_debug"})
@androidx.room.Dao()
public abstract interface CauseDao {
    
    /**
     * Insert a new cause into the database.
     */
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * Update an existing cause in the database.
     */
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Delete a cause from the database.
     */
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Retrieve all causes from the database.
     * Returns a Flow for reactive updates.
     */
    @androidx.room.Query(value = "SELECT * FROM causes")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> getAllCauses();
    
    /**
     * Retrieve all causes synchronously (blocking).
     * Used for initialization checks.
     */
    @androidx.room.Query(value = "SELECT * FROM causes")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllCausesSync(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<ch.heuscher.ad2cause.data.models.Cause>> $completion);
    
    /**
     * Retrieve a specific cause by its ID.
     */
    @androidx.room.Query(value = "SELECT * FROM causes WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCauseById(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super ch.heuscher.ad2cause.data.models.Cause> $completion);
    
    /**
     * Search for causes by name (case-insensitive).
     */
    @androidx.room.Query(value = "SELECT * FROM causes WHERE name LIKE \'%\' || :searchQuery || \'%\'")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> searchCausesByName(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery);
    
    /**
     * Get all user-added causes.
     */
    @androidx.room.Query(value = "SELECT * FROM causes WHERE isUserAdded = 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> getUserAddedCauses();
    
    /**
     * Get all pre-defined causes.
     */
    @androidx.room.Query(value = "SELECT * FROM causes WHERE isUserAdded = 0")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> getPredefinedCauses();
}