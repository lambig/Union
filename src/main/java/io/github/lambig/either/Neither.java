package io.github.lambig.either;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Either with no value
 *
 * @param <L> type of left value (doesn't exist)
 * @param <R> type of right value (doesn't exist)
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
public class Neither<L, R> implements Either<L, R> {
    @Override
    @Deprecated
    public L left() {
        throw new UnsupportedOperationException("Neither has been requested to return left value");
    }

    @Override
    @Deprecated
    public R right() {
        throw new UnsupportedOperationException("Neither has been requested to return right value");
    }

    @Override
    public boolean hasLeft() {
        return false;
    }

    @Override
    public boolean hasRight() {
        return false;
    }

    @Override
    public <O> O asJoined(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return null;
    }

    @Override
    public <O> O asJoined(@NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return null;
    }

    @Override
    @Deprecated
    public void acceptWith(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {//NOOP
    }

    @Override
    public void acceptLeftWith(@NonNull Consumer<? super L> leftResolver) {//NOOP
    }

    @Override
    public void acceptRightWith(@NonNull Consumer<? super R> rightResolver) {//NOOP
    }

    @Override
    public void acceptWith(@NonNull BiConsumer<? super L, ? super R> biResolver) {//NOOP
    }

}
