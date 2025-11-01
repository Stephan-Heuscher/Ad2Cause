package ch.heuscher.ad2cause.ads;

/**
 * Manager class for handling Google AdMob rewarded ads.
 * Handles ad loading, displaying, and reward callbacks.
 *
 * To use this class:
 * 1. Replace the placeholder Ad Unit IDs with your actual IDs from Google AdMob console
 * 2. Initialize with context in your Application or Activity
 * 3. Call loadRewardedAd() to load an ad
 * 4. Call showRewardedAd() to display the ad
 *
 * IMPORTANT: Replace these placeholder IDs:
 * - APP_ID: Your AdMob App ID
 * - REWARDED_AD_UNIT_ID: Your Rewarded Ad Unit ID
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 +2\u00020\u0001:\u0002*+B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010 \u001a\u00020\u0006J\u0006\u0010!\u001a\u00020\u000bJ\u0006\u0010\"\u001a\u00020\bJ\u0006\u0010#\u001a\u00020\bJ\u0010\u0010$\u001a\u00020\u000b2\b\b\u0002\u0010%\u001a\u00020\u0006J\u000e\u0010&\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020\u0006J\u000e\u0010\'\u001a\u00020\u000b2\u0006\u0010(\u001a\u00020)R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR(\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\"\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u000fR(\u0010\u001a\u001a\u0010\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0014\"\u0004\b\u001d\u0010\u0016R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lch/heuscher/ad2cause/ads/AdManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "adType", "Lch/heuscher/ad2cause/ads/AdManager$AdType;", "isLoading", "", "onAdClosed", "Lkotlin/Function0;", "", "getOnAdClosed", "()Lkotlin/jvm/functions/Function0;", "setOnAdClosed", "(Lkotlin/jvm/functions/Function0;)V", "onAdFailedToLoad", "Lkotlin/Function1;", "Lcom/google/android/gms/ads/LoadAdError;", "getOnAdFailedToLoad", "()Lkotlin/jvm/functions/Function1;", "setOnAdFailedToLoad", "(Lkotlin/jvm/functions/Function1;)V", "onAdLoaded", "getOnAdLoaded", "setOnAdLoaded", "onRewardEarned", "", "getOnRewardEarned", "setOnRewardEarned", "rewardedAd", "Lcom/google/android/gms/ads/rewarded/RewardedAd;", "getCurrentAdType", "initializeMobileAds", "isAdLoading", "isAdReady", "loadRewardedAd", "type", "setAdType", "showRewardedAd", "activity", "Landroid/app/Activity;", "AdType", "Companion", "app_debug"})
public final class AdManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AdManager";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String APP_ID = "ca-app-pub-5567609971256551~4548078693";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String REWARDED_AD_UNIT_ID_INTERACTIVE = "ca-app-pub-3940256099942544/5224354917";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String REWARDED_AD_UNIT_ID_NON_INTERACTIVE = "ca-app-pub-3940256099942544/5224354917";
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.ads.rewarded.RewardedAd rewardedAd;
    private boolean isLoading = false;
    @org.jetbrains.annotations.NotNull()
    private ch.heuscher.ad2cause.ads.AdManager.AdType adType = ch.heuscher.ad2cause.ads.AdManager.AdType.NON_INTERACTIVE;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function0<kotlin.Unit> onAdLoaded;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super com.google.android.gms.ads.LoadAdError, kotlin.Unit> onAdFailedToLoad;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function0<kotlin.Unit> onAdClosed;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super java.lang.Double, kotlin.Unit> onRewardEarned;
    @org.jetbrains.annotations.NotNull()
    public static final ch.heuscher.ad2cause.ads.AdManager.Companion Companion = null;
    
    public AdManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnAdLoaded() {
        return null;
    }
    
    public final void setOnAdLoaded(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function1<com.google.android.gms.ads.LoadAdError, kotlin.Unit> getOnAdFailedToLoad() {
        return null;
    }
    
    public final void setOnAdFailedToLoad(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super com.google.android.gms.ads.LoadAdError, kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnAdClosed() {
        return null;
    }
    
    public final void setOnAdClosed(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function1<java.lang.Double, kotlin.Unit> getOnRewardEarned() {
        return null;
    }
    
    public final void setOnRewardEarned(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Double, kotlin.Unit> p0) {
    }
    
    /**
     * Initialize Mobile Ads SDK.
     * Should be called once when the app starts.
     */
    public final void initializeMobileAds() {
    }
    
    /**
     * Load a rewarded ad of the specified type.
     * Can be called multiple times; new ads will replace previous ones.
     *
     * @param type AdType to load (INTERACTIVE for higher earnings, NON_INTERACTIVE for standard)
     */
    public final void loadRewardedAd(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.ads.AdManager.AdType type) {
    }
    
    /**
     * Display the loaded rewarded ad.
     * Make sure to call loadRewardedAd() first and wait for the ad to load.
     */
    public final void showRewardedAd(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
    }
    
    /**
     * Get the current ad type being used.
     */
    @org.jetbrains.annotations.NotNull()
    public final ch.heuscher.ad2cause.ads.AdManager.AdType getCurrentAdType() {
        return null;
    }
    
    /**
     * Set the ad type preference for future ads.
     * This will be used when loadRewardedAd() is called without parameters.
     */
    public final void setAdType(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.ads.AdManager.AdType type) {
    }
    
    /**
     * Check if a rewarded ad is currently loaded and ready to show.
     */
    public final boolean isAdReady() {
        return false;
    }
    
    /**
     * Get the current loading state.
     */
    public final boolean isAdLoading() {
        return false;
    }
    
    /**
     * Enum to define ad types with their characteristics
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lch/heuscher/ad2cause/ads/AdManager$AdType;", "", "(Ljava/lang/String;I)V", "INTERACTIVE", "NON_INTERACTIVE", "app_debug"})
    public static enum AdType {
        /*public static final*/ INTERACTIVE /* = new INTERACTIVE() */,
        /*public static final*/ NON_INTERACTIVE /* = new NON_INTERACTIVE() */;
        
        AdType() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<ch.heuscher.ad2cause.ads.AdManager.AdType> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lch/heuscher/ad2cause/ads/AdManager$Companion;", "", "()V", "APP_ID", "", "REWARDED_AD_UNIT_ID_INTERACTIVE", "REWARDED_AD_UNIT_ID_NON_INTERACTIVE", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}