package ch.heuscher.ad2cause.ui.screens;

/**
 * Stats Screen Fragment
 * Displays statistics about earnings and breakdown per cause.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J$\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\u001a\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\b\u0010\u0015\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lch/heuscher/ad2cause/ui/screens/StatsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "binding", "Lch/heuscher/ad2cause/databinding/FragmentStatsBinding;", "causeViewModel", "Lch/heuscher/ad2cause/viewmodel/CauseViewModel;", "statsAdapter", "Lch/heuscher/ad2cause/ui/screens/StatsFragment$CauseStatsAdapter;", "observeData", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "setupRecyclerView", "CauseStatsAdapter", "app_debug"})
public final class StatsFragment extends androidx.fragment.app.Fragment {
    private ch.heuscher.ad2cause.databinding.FragmentStatsBinding binding;
    private ch.heuscher.ad2cause.viewmodel.CauseViewModel causeViewModel;
    private ch.heuscher.ad2cause.ui.screens.StatsFragment.CauseStatsAdapter statsAdapter;
    
    public StatsFragment() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Setup the RecyclerView for cause stats.
     */
    private final void setupRecyclerView() {
    }
    
    /**
     * Observe and display statistics.
     */
    private final void observeData() {
    }
    
    /**
     * Simple adapter for cause statistics
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0013B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\bH\u0016J\u0018\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016J\u0014\u0010\u0011\u001a\u00020\n2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lch/heuscher/ad2cause/ui/screens/StatsFragment$CauseStatsAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lch/heuscher/ad2cause/ui/screens/StatsFragment$CauseStatsAdapter$StatsViewHolder;", "()V", "causes", "", "Lch/heuscher/ad2cause/data/models/Cause;", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "submitList", "newCauses", "StatsViewHolder", "app_debug"})
    static final class CauseStatsAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<ch.heuscher.ad2cause.ui.screens.StatsFragment.CauseStatsAdapter.StatsViewHolder> {
        @org.jetbrains.annotations.NotNull()
        private java.util.List<ch.heuscher.ad2cause.data.models.Cause> causes;
        
        public CauseStatsAdapter() {
            super();
        }
        
        public final void submitList(@org.jetbrains.annotations.NotNull()
        java.util.List<ch.heuscher.ad2cause.data.models.Cause> newCauses) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public ch.heuscher.ad2cause.ui.screens.StatsFragment.CauseStatsAdapter.StatsViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        ch.heuscher.ad2cause.ui.screens.StatsFragment.CauseStatsAdapter.StatsViewHolder holder, int position) {
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lch/heuscher/ad2cause/ui/screens/StatsFragment$CauseStatsAdapter$StatsViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lch/heuscher/ad2cause/databinding/ItemCauseStatBinding;", "(Lch/heuscher/ad2cause/databinding/ItemCauseStatBinding;)V", "bind", "", "cause", "Lch/heuscher/ad2cause/data/models/Cause;", "app_debug"})
        public static final class StatsViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull()
            private final ch.heuscher.ad2cause.databinding.ItemCauseStatBinding binding = null;
            
            public StatsViewHolder(@org.jetbrains.annotations.NotNull()
            ch.heuscher.ad2cause.databinding.ItemCauseStatBinding binding) {
                super(null);
            }
            
            public final void bind(@org.jetbrains.annotations.NotNull()
            ch.heuscher.ad2cause.data.models.Cause cause) {
            }
        }
    }
}