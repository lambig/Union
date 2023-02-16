package io.github.lambig.either;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.*;

/**
 * An easy implementation of L | R
 *
 * @param <L> class of Left value
 * @param <R> class of Right value
 */
public interface Either<L, R> {

    /**
     * returns Function to call asJoined without lambda.
     *
     * @param leftResolver  function maps left value to output
     * @param rightResolver function maps right value to output
     * @param <L>           class of Left value
     * @param <R>           class of Right value
     * @param <O>           output type
     * @return Function to resolve value by either argument
     */
    static <L, R, O> Function<Either<L, R>, O> toJoinedWith(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return either -> either.asJoined(leftResolver, rightResolver);
    }

    /**
     * returns Function to call asJoined without lambda.
     *
     * @param biResolver function maps both value to output
     * @param <L>        class of Left value
     * @param <R>        class of Right value
     * @param <O>        output type
     * @return Function to resolve value by biResolver
     */
    static <L, R, O> Function<Either<L, R>, O> toJoinedWith(
            @NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return either -> either.asJoined(biResolver);
    }

    /**
     * returns Function to call asJoinedOptional without lambda.
     *
     * @param leftResolver  function maps left value to output
     * @param rightResolver function maps right value to output
     * @param <L>           class of Left value
     * @param <R>           class of Right value
     * @param <O>           output type
     * @return Function to resolve value by either argument
     */
    static <L, R, O> Function<Either<L, R>, Optional<O>> toJoinedOptionalWith(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return either -> either.asJoinedOptional(leftResolver, rightResolver);
    }

    /**
     * returns Function to call asJoinedOptional without lambda.
     *
     * @param biResolver function maps both value to output
     * @param <L>        class of Left value
     * @param <R>        class of Right value
     * @param <O>        output type
     * @return Function to resolve value by biResolver
     */
    static <L, R, O> Function<Either<L, R>, Optional<O>> toJoinedOptionalWith(
            @NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return either -> either.asJoinedOptional(biResolver);
    }

    /**
     * returns Consumer to call accept without lambda.
     *
     * @param leftResolver  consumer accepts left value
     * @param rightResolver consumer accepts right value
     * @param <L>           class of Left value
     * @param <R>           class of Right value
     * @return Consumer to resolve value by either argument
     */
    static <L, R> Consumer<Either<L, R>> acceptWith(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {
        return either -> either.accept(leftResolver, rightResolver);
    }

    /**
     * returns Consumer to call accept without lambda.
     *
     * @param biResolver consumer accepts both value
     * @param <L>        class of Left value
     * @param <R>        class of Right value
     * @return Consumer to resolve value by biResolver
     */
    static <L, R> Consumer<Either<L, R>> acceptWith(
            @NonNull BiConsumer<? super L, ? super R> biResolver) {
        return either -> either.accept(biResolver);
    }

    /**
     * returns Consumer to call acceptLeft without lambda.
     *
     * @param leftResolver consumer accepts left value
     * @param <L>          class of Left value
     * @param <R>          class of Right value
     * @return Consumer to resolve value by leftResolver
     */
    static <L, R> Consumer<Either<L, R>> acceptLeftWith(
            @NonNull Consumer<? super L> leftResolver) {
        return either -> either.acceptLeft(leftResolver);
    }

    /**
     * returns Consumer to call acceptRight without lambda.
     *
     * @param rightResolver consumer accepts right value
     * @param <L>           class of Left value
     * @param <R>           class of Right value
     * @return Consumer to resolve value by rightResolver
     */
    static <L, R> Consumer<Either<L, R>> acceptRightWith(
            @NonNull Consumer<? super R> rightResolver) {
        return either -> either.acceptRight(rightResolver);
    }

    /**
     * returns UnaryOperator to call peek without lambda.
     *
     * @param leftResolver  consumer accepts left value
     * @param rightResolver consumer accepts right value
     * @param <L>           class of Left value
     * @param <R>           class of Right value
     * @return Consumer to resolve value by either argument
     */
    static <L, R> UnaryOperator<Either<L, R>> peekWith(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {
        return either -> either.peek(leftResolver, rightResolver);
    }

    /**
     * returns UnaryOperator to call peek without lambda.
     *
     * @param biResolver consumer accepts both value
     * @param <L>        class of Left value
     * @param <R>        class of Right value
     * @return Consumer to resolve value by biResolver
     */
    static <L, R> UnaryOperator<Either<L, R>> peekWith(
            @NonNull BiConsumer<? super L, ? super R> biResolver) {
        return either -> either.peek(biResolver);
    }

    /**
     * returns UnaryOperator to call peekLeft without lambda.
     *
     * @param leftResolver consumer accepts left value
     * @param <L>          class of Left value
     * @param <R>          class of Right value
     * @return Consumer to resolve value by leftResolver
     */
    static <L, R> UnaryOperator<Either<L, R>> peekLeftWith(
            @NonNull Consumer<? super L> leftResolver) {
        return either -> either.peekLeft(leftResolver);
    }

    /**
     * returns UnaryOperator to call peekRight without lambda.
     *
     * @param rightResolver consumer accepts right value
     * @param <L>           class of Left value
     * @param <R>           class of Right value
     * @return Consumer to resolve value by rightResolver
     */
    static <L, R> UnaryOperator<Either<L, R>> peekRightWith(
            @NonNull Consumer<? super R> rightResolver) {
        return either -> either.peekRight(rightResolver);
    }

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
    void accept(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver);

    /**
     * Make consumer accept both values.
     * NOTE: At least one argument of consumer is always null.
     *
     * @param biResolver consumer accepts both value
     */
    void accept(@NonNull BiConsumer<? super L, ? super R> biResolver);

    /**
     * Make consumer accept left value if it exists.
     *
     * @param leftResolver consumer accepts left value
     */
    void acceptLeft(@NonNull Consumer<? super L> leftResolver);

    /**
     * Make consumer accept right value if it exists.
     *
     * @param rightResolver consumer accepts right value
     */
    void acceptRight(@NonNull Consumer<? super R> rightResolver);

    /**
     * Make either consumer accept correspondent value and returns self.
     *
     * @param leftResolver  consumer accepts left value
     * @param rightResolver consumer accepts right value
     * @return self
     */
    default Either<L, R> peek(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {
        this.accept(leftResolver, rightResolver);
        return this;
    }

    /**
     * Make consumer accept values and returns self.
     * NOTE: At least one argument of consumer is always null.
     *
     * @param biResolver consumer accepts left value
     * @return self
     */
    default Either<L, R> peek(@NonNull BiConsumer<? super L, ? super R> biResolver) {
        this.accept(biResolver);
        return this;
    }

    /**
     * Make consumer accept left value if it exists and returns self anyway.
     *
     * @param leftResolver consumer accepts left value
     * @return self
     */
    default Either<L, R> peekLeft(@NonNull Consumer<? super L> leftResolver) {
        this.acceptLeft(leftResolver);
        return this;
    }

    /**
     * Make consumer accept right value if it exists and returns self anyway.
     *
     * @param rightResolver consumer accepts left value
     * @return self
     */
    default Either<L, R> peekRight(@NonNull Consumer<? super R> rightResolver) {
        this.acceptRight(rightResolver);
        return this;
    }
}
