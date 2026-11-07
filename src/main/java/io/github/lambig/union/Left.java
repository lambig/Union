package io.github.lambig.union;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.*;

/**
 * 左側に非nullの値を保持するUnionです。
 * {@link #left()}と{@link #get()}でその値を取得でき、左右別の関数・処理には左側のものが選ばれます。
 *
 * @param <L> 左側の値の型
 * @param <R> 右側の値の型
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
@Accessors(fluent = true)
public final class Left<L, R> implements Union<L, R>, Supplier<L> {
    /**
     * 左側に保持する値です。
     * @return 左側に保持する値
     */
    @NonNull
    @Getter
    private final L left;

    /**
     * 右側の値を要求したことを例外で通知します。
     * @return このメソッドは常に例外になります
     * @throws UnsupportedOperationException 常に発生
     * @deprecated 左側の値にはleft()、共通の結果にはasJoined()を使います。
     */
    @Override
    @Deprecated
    public R right() {
        throw new UnsupportedOperationException("Left value of Union has been requested to return right value");
    }

    @Override
    public boolean hasLeft() {
        return true;
    }

    @Override
    public boolean hasRight() {
        return false;
    }

    public <O> O asJoined(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return leftResolver.apply(this.left());
    }

    @Override
    public <O> O asJoined(@NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return biResolver.apply(this.left(), null);
    }

    @Override
    public void accept(@NonNull Consumer<? super L> leftResolver, @NonNull Consumer<? super R> rightResolver) {
        leftResolver.accept(this.left());
    }

    @Override
    public void accept(@NonNull BiConsumer<? super L, ? super R> biResolver) {
        biResolver.accept(this.left(), null);
    }

    @Override
    public void acceptLeft(@NonNull Consumer<? super L> leftResolver) {
        leftResolver.accept(this.left());
    }

    @Override
    public void acceptRight(@NonNull Consumer<? super R> rightResolver) { // NOOP
    }


    /**
     * 左側の値をSupplierの結果として返します。
     * @return left()と同じ値
     */
    @Override
    public L get() {
        return this.left();
    }
}
