package io.github.lambig.union;

import lombok.experimental.UtilityClass;

/**
 * Instance provider of Union.
 */
@UtilityClass
public final class UnionOf {

    /**
     * Returns Union with left value.
     *
     * @param leftValue left value
     * @param <A>       Type of left value
     * @param <B>       Type of right value (doesn't exist)
     * @return Left instance
     */
    public static <A, B> Left<A, B> left(A leftValue) {
        return Left.of(leftValue);
    }

    /**
     * Returns Union with right value.
     *
     * @param rightValue right value
     * @param <A>        Type of left value (doesn't exist)
     * @param <B>        Type of right value
     * @return Right instance
     */
    public static <A, B> Right<A, B> right(B rightValue) {
        return Right.of(rightValue);
    }

    /**
     * Returns Union with no value.
     *
     * @param <A> Type of left value (doesn't exist)
     * @param <B> Type of right value (doesn't exist)
     * @return Neither instance
     */
    public static <A, B> Neither<A, B> none() {
        return Neither.of();
    }

    /**
     * Returns Union provider with left value.
     * Call this method when you have to explicitly give type argument of right value.
     *
     * @param leftValue left value
     * @param <A>       Type of left value
     * @return Left instance
     */
    public static <A> LeftProvider<A> l(A leftValue) {
        return new LeftProvider<A>() {
            @Override
            public <B> Left<A, B> r() {
                return Left.of(leftValue);
            }
        };
    }

    /**
     * Returns Union provider with right value.
     * Call this method when you have to explicitly give type argument of left value.
     *
     * @param rightValue right value
     * @param <B>        Type of right value
     * @return Right instance
     */
    public static <B> RightProvider<B> r(B rightValue) {
        return new RightProvider<B>() {
            @Override
            public <A> Right<A, B> l() {
                return Right.of(rightValue);
            }
        };
    }

    public interface LeftProvider<A> {
        /**
         * Returns Left.Call this method to give type argument of right value.
         *
         * @param <B> Type of right value (doesn't exist)
         * @return Left instance
         */
        <B> Left<A, B> r();
    }

    public interface RightProvider<B> {
        /**
         * Returns Right.Call this method to give type argument of left value.
         *
         * @param <A> Type of left value (doesn't exist)
         * @return Right instance
         */
        <A> Right<A, B> l();
    }

}
