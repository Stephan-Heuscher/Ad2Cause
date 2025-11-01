package ch.heuscher.ad2cause.viewmodel;

/**
 * ViewModel for managing Cause-related data and logic.
 * Handles communication between the UI and data layer.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u00014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\t2\u0006\u0010%\u001a\u00020\tJ\u0006\u0010&\u001a\u00020\"J\u000e\u0010\'\u001a\u00020\"2\u0006\u0010(\u001a\u00020\u0007J\u0016\u0010)\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00122\u0006\u0010*\u001a\u00020+J\b\u0010,\u001a\u00020\"H\u0002J\b\u0010-\u001a\u00020\"H\u0002J\u001a\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00130\u00122\u0006\u0010/\u001a\u00020\tJ\u000e\u00100\u001a\u00020\"2\u0006\u0010(\u001a\u00020\u0007J\u000e\u00101\u001a\u00020\"2\u0006\u00102\u001a\u000203R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0010R\u0016\u0010\u001a\u001a\n \u001c*\u0004\u0018\u00010\u001b0\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\f0\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u00a8\u00065"}, d2 = {"Lch/heuscher/ad2cause/viewmodel/CauseViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_activeCause", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lch/heuscher/ad2cause/data/models/Cause;", "_searchQuery", "", "_uiEvent", "Landroidx/lifecycle/MutableLiveData;", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent;", "activeCause", "Lkotlinx/coroutines/flow/StateFlow;", "getActiveCause", "()Lkotlinx/coroutines/flow/StateFlow;", "allCauses", "Lkotlinx/coroutines/flow/Flow;", "", "getAllCauses", "()Lkotlinx/coroutines/flow/Flow;", "repository", "Lch/heuscher/ad2cause/data/repository/CauseRepository;", "searchQuery", "getSearchQuery", "sharedPreferences", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "uiEvent", "Landroidx/lifecycle/LiveData;", "getUiEvent", "()Landroidx/lifecycle/LiveData;", "addNewCause", "", "name", "description", "category", "clearSearch", "deleteCause", "cause", "getCauseById", "id", "", "initializeSampleData", "loadActiveCause", "searchCauses", "query", "setActiveCause", "updateActiveCauseEarnings", "amount", "", "UiEvent", "app_debug"})
public final class CauseViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final ch.heuscher.ad2cause.data.repository.CauseRepository repository = null;
    private final android.content.SharedPreferences sharedPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> allCauses = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<ch.heuscher.ad2cause.data.models.Cause> _activeCause = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<ch.heuscher.ad2cause.data.models.Cause> activeCause = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent> _uiEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent> uiEvent = null;
    
    public CauseViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> getAllCauses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<ch.heuscher.ad2cause.data.models.Cause> getActiveCause() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent> getUiEvent() {
        return null;
    }
    
    /**
     * Initialize the database with sample causes on first launch.
     */
    private final void initializeSampleData() {
    }
    
    /**
     * Load the active cause from SharedPreferences.
     */
    private final void loadActiveCause() {
    }
    
    /**
     * Set a cause as the active cause.
     */
    public final void setActiveCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause) {
    }
    
    /**
     * Get a cause by ID.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<ch.heuscher.ad2cause.data.models.Cause> getCauseById(int id) {
        return null;
    }
    
    /**
     * Insert a new cause (typically user-added).
     */
    public final void addNewCause(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.lang.String category) {
    }
    
    /**
     * Update the earnings for the active cause.
     */
    public final void updateActiveCauseEarnings(double amount) {
    }
    
    /**
     * Search for causes by name.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<ch.heuscher.ad2cause.data.models.Cause>> searchCauses(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    /**
     * Clear search query.
     */
    public final void clearSearch() {
    }
    
    /**
     * Delete a cause from the database.
     */
    public final void deleteCause(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.data.models.Cause cause) {
    }
    
    /**
     * Sealed class for UI events.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent;", "", "()V", "CauseAdded", "CauseDeleted", "CauseSelected", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent$CauseAdded;", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent$CauseDeleted;", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent$CauseSelected;", "app_debug"})
    public static abstract class UiEvent {
        
        private UiEvent() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent$CauseAdded;", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent;", "causeName", "", "(Ljava/lang/String;)V", "getCauseName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class CauseAdded extends ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String causeName = null;
            
            public CauseAdded(@org.jetbrains.annotations.NotNull()
            java.lang.String causeName) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getCauseName() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent.CauseAdded copy(@org.jetbrains.annotations.NotNull()
            java.lang.String causeName) {
                return null;
            }
            
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent$CauseDeleted;", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent;", "causeName", "", "(Ljava/lang/String;)V", "getCauseName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class CauseDeleted extends ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String causeName = null;
            
            public CauseDeleted(@org.jetbrains.annotations.NotNull()
            java.lang.String causeName) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getCauseName() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent.CauseDeleted copy(@org.jetbrains.annotations.NotNull()
            java.lang.String causeName) {
                return null;
            }
            
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent$CauseSelected;", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel$UiEvent;", "causeName", "", "(Ljava/lang/String;)V", "getCauseName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
        public static final class CauseSelected extends ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String causeName = null;
            
            public CauseSelected(@org.jetbrains.annotations.NotNull()
            java.lang.String causeName) {
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getCauseName() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final ch.heuscher.ad2cause.viewmodel.CauseViewModel.UiEvent.CauseSelected copy(@org.jetbrains.annotations.NotNull()
            java.lang.String causeName) {
                return null;
            }
            
            @java.lang.Override()
            public boolean equals(@org.jetbrains.annotations.Nullable()
            java.lang.Object other) {
                return false;
            }
            
            @java.lang.Override()
            public int hashCode() {
                return 0;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }
        }
    }
}