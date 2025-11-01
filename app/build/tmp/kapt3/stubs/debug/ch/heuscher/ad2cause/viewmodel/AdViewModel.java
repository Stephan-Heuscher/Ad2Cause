package ch.heuscher.ad2cause.viewmodel;

/**
 * ViewModel for managing AdMob-related logic and state.
 * Handles rewarded ad interactions and reward callbacks.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0007J\u0006\u0010\u0017\u001a\u00020\u0015J\u0006\u0010\u0018\u001a\u00020\u0015J\u0006\u0010\u0019\u001a\u00020\u0015J\u0006\u0010\u001a\u001a\u00020\u0015R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010\t0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010\u00a8\u0006\u001c"}, d2 = {"Lch/heuscher/ad2cause/viewmodel/AdViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_adErrorEvent", "Landroidx/lifecycle/MutableLiveData;", "", "_isAdLoading", "", "kotlin.jvm.PlatformType", "_rewardEarnedEvent", "", "adErrorEvent", "Landroidx/lifecycle/LiveData;", "getAdErrorEvent", "()Landroidx/lifecycle/LiveData;", "isAdLoading", "rewardEarnedEvent", "getRewardEarnedEvent", "onAdError", "", "errorMessage", "onAdLoadingCompleted", "onAdLoadingStarted", "onRewardEarned", "resetEvents", "Companion", "app_debug"})
public final class AdViewModel extends androidx.lifecycle.AndroidViewModel {
    public static final double REWARD_AMOUNT = 0.01;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Double> _rewardEarnedEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Double> rewardEarnedEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> _isAdLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Boolean> isAdLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _adErrorEvent = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> adErrorEvent = null;
    @org.jetbrains.annotations.NotNull()
    public static final ch.heuscher.ad2cause.viewmodel.AdViewModel.Companion Companion = null;
    
    public AdViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Double> getRewardEarnedEvent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Boolean> isAdLoading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getAdErrorEvent() {
        return null;
    }
    
    /**
     * Called when a rewarded ad is completed successfully.
     * This triggers the reward event with the reward amount.
     */
    public final void onRewardEarned() {
    }
    
    /**
     * Called when starting to load an ad.
     */
    public final void onAdLoadingStarted() {
    }
    
    /**
     * Called when ad loading completes (success or failure).
     */
    public final void onAdLoadingCompleted() {
    }
    
    /**
     * Called when an ad error occurs.
     */
    public final void onAdError(@org.jetbrains.annotations.NotNull()
    java.lang.String errorMessage) {
    }
    
    /**
     * Resets the event values for reuse.
     */
    public final void resetEvents() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lch/heuscher/ad2cause/viewmodel/AdViewModel$Companion;", "", "()V", "REWARD_AMOUNT", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}