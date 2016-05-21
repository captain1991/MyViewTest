package anim.pulltorefresh;

public interface Pullable {
    /**
     * 判断是否可以下拉
     */
    boolean canPullDown();

    /**
     * 判断是否可以上拉
     */
    boolean canPullUp();
}
