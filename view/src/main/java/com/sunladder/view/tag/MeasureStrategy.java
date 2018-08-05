package com.sunladder.view.tag;

/**
 * Description:
 * Created by Sun Yaozong on 2018/8/3
 */
public final class MeasureStrategy {

    private MeasureStrategy(int overWidthState, boolean stretchToLineHeight, int alignMode) {
        this.overWidthState = overWidthState;
        this.stretchToLineHeight = stretchToLineHeight;
        this.alignMode = alignMode;

        if (this.stretchToLineHeight) {
            this.alignMode = ALIGN_MODE_TOP;
        }
    }

    /**
     * 超出边界换行处理
     * 0 换行
     * 1 对齐到末尾
     * 2 直接layout 不管超出
     */
    public static final int OVER_WIDTH_NEW_LINE = 0;
    public static final int OVER_WIDTH_RIGHT_TO_END = 1;
    public static final int OVER_WIDTH_JUST_LAYOUT = 2;

    int overWidthState;

    /**
     * 是否拉伸至行高 暂时不支持等比拉伸，只能纵向拉伸
     */
    boolean stretchToLineHeight;

    /**
     * 对齐方式
     * 0 顶部对齐
     * 1 中心对齐
     * 2 底部对齐
     */
    public static final int ALIGN_MODE_TOP = 0;
    public static final int ALIGN_MODE_CENTER = 1;
    public static final int ALIGN_MODE_BOTTOM = 2;

    int alignMode;

    public static class Builder {

        int overWidthState = OVER_WIDTH_NEW_LINE;
        boolean stretchToLineHeight = true;
        int alignMode = ALIGN_MODE_TOP;

        public Builder setOverWidthState(int overWidthState) {
            this.overWidthState = overWidthState;
            return this;
        }

        /**
         * 暂时不支持 等比拉伸
         */
        public Builder setStretchToLineHeight(boolean stretchToLineHeight) {
            this.stretchToLineHeight = stretchToLineHeight;
            return this;
        }

        public Builder setAlignMode(int alignMode) {
            this.alignMode = alignMode;
            return this;
        }

        public MeasureStrategy build() {
            return new MeasureStrategy(this.overWidthState, this.stretchToLineHeight,
                    this.alignMode);
        }

        public static MeasureStrategy getDefault() {
            return new MeasureStrategy(OVER_WIDTH_NEW_LINE, false, ALIGN_MODE_CENTER);
        }
    }

    /**
     * 测量策略 item > line > global
     */
    public interface MeasureStrategyGroup {

        MeasureStrategy getItemStrategy(int indexInAdapter);

        MeasureStrategy getLineStrategy(int lineNum);

        MeasureStrategy getGlobalStrategy();
    }

    /**
     * 默认实现
     */
    public static class DefaultMeasureStrategyGroup implements MeasureStrategyGroup {

        MeasureStrategy globalStrategy;

        public DefaultMeasureStrategyGroup() {
            this(MeasureStrategy.Builder.getDefault());
        }

        public DefaultMeasureStrategyGroup(MeasureStrategy globalStrategy) {
            this.globalStrategy = globalStrategy;
        }

        @Override
        public MeasureStrategy getItemStrategy(int indexInAdapter) {
            return null;
        }

        @Override
        public MeasureStrategy getLineStrategy(int lineNum) {
            return null;
        }

        @Override
        public MeasureStrategy getGlobalStrategy() {
            return globalStrategy;
        }
    }
}
