package io.github.lambig.either;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.*;

/**
 * Either with only right value
 *
 * @param <L> type of left value (doesn't exist)
 * @param <R> type of right value
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
@Accessors(fluent = true)
public final class Right<L, R> implements Either<L, R>, Supplier<R> {
    @NonNull
    @Getter
    private final R right;

    @Override
    @Deprecated
    public L left() {
        throw new UnsupportedOperationException("Right value of Either has been requested to return left value");
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
    public void acceptWith(@NonNull Consumer<? super L> leftResolver, @NonNull Consumer<? super R> rightResolver) {
        rightResolver.accept(this.right());
    }

    @Override
    public void acceptWith(@NonNull BiConsumer<? super L, ? super R> biResolver) {
        biResolver.accept(null, this.right());
    }

    @Override
    public void acceptLeftWith(@NonNull Consumer<? super L> leftResolver) {//NOOP
    }

    @Override
    public void acceptRightWith(@NonNull Consumer<? super R> rightResolver) {
        rightResolver.accept(this.right());
    }


    @Override
    public R get() {
        return this.right();
    }
}
