package io.github.lambig.union;

import lombok.experimental.UtilityClass;

/**
 * 左または右の値を保持するUnionを作ります。
 * <p>{@code left(value)}と{@code right(value)}では代入先から左右の型を推論できます。
 * 反対側の型を明示したい場合は{@code l(value).<String>r()}のように指定します。</p>
 */
@UtilityClass
public final class UnionOf {

    /**
     * 左側に指定した値を保持するUnionを返します。
     *
     * @param leftValue 保持する左側の値（非null）
     * @param <A> 左側の値の型
     * @param <B> 右側の値の型
     * @return left()とget()で指定した値を得られるLeft
     * @throws NullPointerException leftValueがnullの場合
     */
    public static <A, B> Left<A, B> left(A leftValue) {
        return Left.of(leftValue);
    }

    /**
     * 右側に指定した値を保持するUnionを返します。
     *
     * @param rightValue 保持する右側の値（非null）
     * @param <A> 左側の値の型
     * @param <B> 右側の値の型
     * @return right()とget()で指定した値を得られるRight
     * @throws NullPointerException rightValueがnullの場合
     */
    public static <A, B> Right<A, B> right(B rightValue) {
        return Right.of(rightValue);
    }

    /**
     * 左側の値を決め、右側の型をあとから明示できる生成元を返します。
     * たとえば{@code UnionOf.l(1).<String>r()}は{@code Left<Integer, String>}です。
     *
     * @param leftValue Leftに保持させる左側の値
     * @param <A> 左側の値の型
     * @return r()で右側の型を指定してLeftを得る生成元
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
     * 右側の値を決め、左側の型をあとから明示できる生成元を返します。
     * たとえば{@code UnionOf.r("text").<Integer>l()}は{@code Right<Integer, String>}です。
     *
     * @param rightValue Rightに保持させる右側の値
     * @param <B> 右側の値の型
     * @return l()で左側の型を指定してRightを得る生成元
     */
    public static <B> RightProvider<B> r(B rightValue) {
        return new RightProvider<B>() {
            @Override
            public <A> Right<A, B> l() {
                return Right.of(rightValue);
            }
        };
    }

    /**
     * 左側の値をもとに、右側の型を指定してLeftを得る生成元です。
     * @param <A> 左側の値の型
     */
    public interface LeftProvider<A> {
        /**
         * 指定した右側の型と、生成元の左側の値を持つLeftを返します。
         *
         * @param <B> 右側の値の型
         * @return 生成元の値を保持するLeft
         * @throws NullPointerException 生成元に渡した左側の値がnullの場合
         */
        <B> Left<A, B> r();
    }

    /**
     * 右側の値をもとに、左側の型を指定してRightを得る生成元です。
     * @param <B> 右側の値の型
     */
    public interface RightProvider<B> {
        /**
         * 指定した左側の型と、生成元の右側の値を持つRightを返します。
         *
         * @param <A> 左側の値の型
         * @return 生成元の値を保持するRight
         * @throws NullPointerException 生成元に渡した右側の値がnullの場合
         */
        <A> Right<A, B> l();
    }

}
