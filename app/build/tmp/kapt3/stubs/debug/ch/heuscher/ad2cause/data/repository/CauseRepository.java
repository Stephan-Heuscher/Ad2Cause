package ch.heuscher.ad2cause.data.repository;

/**
 * Repository for managing Cause-related data operations.
 * Acts as a single source of truth for cause data, abstracting database operations.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0012\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bJ\u0018\u0010\r\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0012\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bJ\u0012\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bJ\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u001a\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000b2\u0006\u0010\u0016\u001a\u00020\u0017J\u0016\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010\u001dR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lch/heuscher/ad2cause/data/repository/CauseRepository;", "", "causeDao", "Lch/heuscher/ad2cause/data/database/CauseDao;", "(Lch/heuscher/ad2cause/data/database/CauseDao;)V", "deleteCause", "", "cause", "Lch/heuscher/ad2cause/data/models/Cause;", "(Lch/heuscher/ad2cause/data/models/Cause;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCauses", "Lkotlinx/coroutines/flow/Flow;", "", "getCauseById", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPredefinedCauses", "getUserAddedCauses", "insertCause", "", "searchCausesByName", "query", "", "updateCause", "updateCauseEarnings", "causeId", "earnedAmount", "", "(IDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class CauseRepository {
    @org.jetbrains.annotations.NotNull()
    private final ch.heuscher.ad2cause.data.database.CauseDao causeDao = null;
    
    public CauseRepository(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.database.CauseDao causeDao) {
        super();
    }
    
    /**
     * Get all causes as a Flow for reactive updates.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> getAllCauses() {
        return null;
    }
    
    /**
     * Get a specific cause by its ID.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCauseById(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super ch.heuscher.ad2cause.data.models.Cause> $completion) {
        return null;
    }
    
    /**
     * Insert a new cause into the database.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * Update an existing cause.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Delete a cause from the database.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Search for causes by name.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> searchCausesByName(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    /**
     * Get all user-added causes.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> getUserAddedCauses() {
        return null;
    }
    
    /**
     * Get all pre-defined causes.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> getPredefinedCauses() {
        return null;
    }
    
    /**
     * Update the earnings for a specific cause.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCauseEarnings(int causeId, double earnedAmount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}