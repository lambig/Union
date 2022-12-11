package io.github.lambig.either;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.*;

/**
 * Either with only left value.
 *
 * @param <L> type of left value
 * @param <R> type of right value (doesn't exist)
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
@Accessors(fluent = true)
public final class Left<L, R> implements Either<L, R>, Supplier<L> {
    @NonNull
    @Getter
    private final L left;

    @Override
    @Deprecated
    public R right() {
        throw new UnsupportedOperationException("Left value of Either has been requested to return right value");
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
    public void acceptWith(@NonNull Consumer<? super L> leftResolver, @NonNull Consumer<? super R> rightResolver) {
        leftResolver.accept(this.left());
    }

    @Override
    public void acceptWith(@NonNull BiConsumer<? super L, ? super R> biResolver) {
        biResolver.accept(this.left(), null);
    }

    @Override
    public void acceptLeftWith(@NonNull Consumer<? super L> leftResolver) {
        leftResolver.accept(this.left());
    }

    @Override
    public void acceptRightWith(@NonNull Consumer<? super R> rightResolver) { // NOOP
    }


    @Override
    public L get() {
        return this.left();
    }
}
