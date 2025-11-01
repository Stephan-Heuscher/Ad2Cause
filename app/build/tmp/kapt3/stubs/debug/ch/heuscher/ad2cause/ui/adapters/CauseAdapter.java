package ch.heuscher.ad2cause.ui.adapters;

/**
 * RecyclerView Adapter for displaying a list of causes.
 * Uses ListAdapter with DiffUtil for efficient list updates.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0010\u0011B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lch/heuscher/ad2cause/ui/adapters/CauseAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lch/heuscher/ad2cause/data/models/Cause;", "Lch/heuscher/ad2cause/ui/adapters/CauseAdapter$CauseViewHolder;", "onCauseClick", "Lkotlin/Function1;", "", "(Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "CauseDiffCallback", "CauseViewHolder", "app_debug"})
public final class CauseAdapter extends androidx.recyclerview.widget.ListAdapter<ch.heuscher.ad2cause.data.models.Cause, ch.heuscher.ad2cause.ui.adapters.CauseAdapter.CauseViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<ch.heuscher.ad2cause.data.models.Cause, kotlin.Unit> onCauseClick = null;
    
    public CauseAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super ch.heuscher.ad2cause.data.models.Cause, kotlin.Unit> onCauseClick) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public ch.heuscher.ad2cause.ui.adapters.CauseAdapter.CauseViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    ch.heuscher.ad2cause.ui.adapters.CauseAdapter.CauseViewHolder holder, int position) {
    }
    
    /**
     * DiffUtil callback for efficient list updates.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lch/heuscher/ad2cause/ui/adapters/CauseAdapter$CauseDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lch/heuscher/ad2cause/data/models/Cause;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    static final class CauseDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<ch.heuscher.ad2cause.data.models.Cause> {
        
        public CauseDiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        ch.heuscher.ad2cause.data.models.Cause oldItem, @org.jetbrains.annotations.NotNull()
        ch.heuscher.ad2cause.data.models.Cause newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        ch.heuscher.ad2cause.data.models.Cause oldItem, @org.jetbrains.annotations.NotNull()
        ch.heuscher.ad2cause.data.models.Cause newItem) {
            return false;
        }
    }
    
    /**
     * ViewHolder for individual cause items.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lch/heuscher/ad2cause/ui/adapters/CauseAdapter$CauseViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lch/heuscher/ad2cause/databinding/ItemCauseCardBinding;", "onCauseClick", "Lkotlin/Function1;", "Lch/heuscher/ad2cause/data/models/Cause;", "", "(Lch/heuscher/ad2cause/databinding/ItemCauseCardBinding;Lkotlin/jvm/functions/Function1;)V", "bind", "cause", "app_debug"})
    public static final class CauseViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final ch.heuscher.ad2cause.databinding.ItemCauseCardBinding binding = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<ch.heuscher.ad2cause.data.models.Cause, kotlin.Unit> onCauseClick = null;
        
        public CauseViewHolder(@org.jetbrains.annotations.NotNull()
        ch.heuscher.ad2cause.databinding.ItemCauseCardBinding binding, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super ch.heuscher.ad2cause.data.models.Cause, kotlin.Unit> onCauseClick) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        ch.heuscher.ad2cause.data.models.Cause cause) {
        }
    }
}