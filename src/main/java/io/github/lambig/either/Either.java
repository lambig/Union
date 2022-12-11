package io.github.lambig.either;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An easy implementation of L | R
 *
 * @param <L> class of Left value
 * @param <R> class of Right value
 */
public interface Either<L, R> {
    /**
     * Returns left value
     *
     * @return left value
     * @throws UnsupportedOperationException when this object has no left value
     */
    L left();

    /**
     * Returns right value
     *
     * @return right value
     * @throws UnsupportedOperationException when this object has no right value
     */
    R right();

    /**
     * Returns left value as an Optional.
     * NOTE: No exception will be thrown even if the value does not exist(returns Optional.empty())
     *
     * @return option of the left value
     */
    default Optional<L> leftOptional() {
        return this.hasLeft() ? Optional.of(this.left()) : Optional.empty();
    }

    /**
     * Returns right value as an Optional.
     * NOTE: No exception will be thrown even if the value does not exist(returns empty optional instance)
     *
     * @return option of the right value
     */
    default Optional<R> rightOptional() {
        return this.hasRight() ? Optional.of(this.right()) : Optional.empty();
    }

    /**
     * Returns if this has left value.
     *
     * @return true if the value exists
     */
    boolean hasLeft();

    /**
     * Returns if this has right value.
     *
     * @return true if the value exists
     */
    boolean hasRight();

    /**
     * Join 2 candidate to an instance of single class.
     *
     * @param leftResolver  function maps left value to output
     * @param rightResolver function maps right value to output
     * @param <O>           output type
     * @return Resolved value by either argument
     */
    <O> O asJoined(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver);

    /**
     * Join 2 candidate to an instance of single class.
     * NOTE: At least one argument of function is always null.
     *
     * @param biResolver function maps both value to output
     * @param <O>        output type
     * @return Resolved value by argument
     */
    <O> O asJoined(@NonNull BiFunction<? super L, ? super R, ? extends O> biResolver);

    /**
     * Join 2 candidate to an instance of single class (Optionally).
     *
     * @param leftResolver  function maps left value to output
     * @param rightResolver function maps right value to output
     * @param <O>           output type
     * @return Optional of resolved value by either argument
     */
    default <O> Optional<O> asJoinedOptional(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return Optional.ofNullable(this.asJoined(leftResolver, rightResolver));
    }

    /**
     * Join 2 candidate to an instance of single class (Optionally).
     * NOTE : At least one argument of function is always null.
     *
     * @param biResolver function maps both value to output
     * @param <O>        output type
     * @return Resolved value by argument
     */
    default <O> Optional<O> asJoinedOptional(@NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return Optional.ofNullable(this.asJoined(biResolver));
    }

    /**
     * Make either consumer accept correspondent value.
     *
     * @param leftResolver  consumer accepts left value
     * @param rightResolver consumer accepts right value
     */
    void acceptWith(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver);

    /**
     * Make consumer accept both values.
     * NOTE: At least one argument of consumer is always null.
     *
     * @param biResolver consumer accepts both value
     */
    void acceptWith(@NonNull BiConsumer<? super L, ? super R> biResolver);

    /**
     * Make consumer accept left value if it exists.
     *
     * @param leftResolver consumer accepts left value
     */
    void acceptLeftWith(@NonNull Consumer<? super L> leftResolver);

    /**
     * Make consumer accept right value if it exists.
     *
     * @param rightResolver consumer accepts right value
     */
    void acceptRightWith(@NonNull Consumer<? super R> rightResolver);

    /**
     * Make either consumer accept correspondent value and returns self.
     *
     * @param leftResolver  consumer accepts left value
     * @param rightResolver consumer accepts right value
     * @return self
     */
    default Either<L, R> peekWith(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {
        this.acceptWith(leftResolver, rightResolver);
        return this;
    }

    /**
     * Make consumer accept values and returns self.
     * NOTE: At least one argument of consumer is always null.
     *
     * @param biResolver consumer accepts left value
     * @return self
     */
    default Either<L, R> peekWith(@NonNull BiConsumer<? super L, ? super R> biResolver) {
        this.acceptWith(biResolver);
        return this;
    }

    /**
     * Make consumer accept left value if it exists and returns self anyway.
     *
     * @param leftResolver consumer accepts left value
     * @return self
     */
    default Either<L, R> peekLeftWith(@NonNull Consumer<? super L> leftResolver) {
        this.acceptLeftWith(leftResolver);
        return this;
    }

    /**
     * Make consumer accept right value if it exists and returns self anyway.
     *
     * @param rightResolver consumer accepts left value
     * @return self
     */
    default Either<L, R> peekRightWith(@NonNull Consumer<? super R> rightResolver) {
        this.acceptRightWith(rightResolver);
        return this;
    }
}
