package io.github.lambig.union;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.*;

/**
 * 右側に非nullの値を保持するUnionです。
 * {@link #right()}と{@link #get()}でその値を取得でき、左右別の関数・処理には右側のものが選ばれます。
 *
 * @param <L> 左側の値の型
 * @param <R> 右側の値の型
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
@Accessors(fluent = true)
public final class Right<L, R> implements Union<L, R>, Supplier<R> {
    /**
     * 右側に保持する値です。
     * @return 右側に保持する値
     */
    @NonNull
    @Getter
    private final R right;

    /**
     * 左側の値を要求したことを例外で通知します。
     * @return このメソッドは常に例外になります
     * @throws UnsupportedOperationException 常に発生
     * @deprecated 右側の値にはright()、共通の結果にはasJoined()を使います。
     */
    @Override
    @Deprecated
    public L left() {
        throw new UnsupportedOperationException("Right value of Union has been requested to return left value");
    }

    @Override
    public boolean hasLeft() {
        return false;
    }

    @Override
    public boolean hasRight() {
        return true;
    }

    public <O> O asJoined(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return rightResolver.apply(this.right());
    }

    @Override
    public <O> O asJoined(@NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return biResolver.apply(null, this.right());
    }

    @Override
    public void accept(@NonNull Consumer<? super L> leftResolver, @NonNull Consumer<? super R> rightResolver) {
        rightResolver.accept(this.right());
    }

    @Override
    public void accept(@NonNull BiConsumer<? super L, ? super R> biResolver) {
        biResolver.accept(null, this.right());
    }

    @Override
    public void acceptLeft(@NonNull Consumer<? super L> leftResolver) {//NOOP
    }

    @Override
    public void acceptRight(@NonNull Consumer<? super R> rightResolver) {
        rightResolver.accept(this.right());
    }


    /**
     * 右側の値をSupplierの結果として返します。
     * @return right()と同じ値
     */
    @Override
    public R get() {
        return this.right();
    }
}
