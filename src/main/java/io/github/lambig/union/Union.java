package io.github.lambig.union;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.*;

/**
 * 左の型Lまたは右の型Rの、どちらか一方の値を表します。
 * <p>{@link UnionOf}で作るUnionは非nullの値をひとつ保持します。
 * たとえば成功値とエラー理由を左右に割り当て、{@code asJoined}で共通の結果へ変換できます。
 * 左右の意味は利用側で決めます。{@code leftOptional}/{@code rightOptional}では、
 * 保持する側が値を含むOptional、反対側が空のOptionalになります。</p>
 * <p>左右別の関数・処理にはどちらも非nullを指定し、保持する側の処理だけが呼ばれます。
 * BiFunction/BiConsumerには、左なら(左値, null)、右なら(null, 右値)が渡ります。
 * 関数・処理が投げた例外は呼出元へ伝わります。</p>
 * <p>保持する値への参照は共有されます。複数スレッドで使う場合は、その値と指定する処理を
 * 利用するスレッド間で安全に扱えるようにします。</p>
 *
 * @param <L> 左側に保持する値の型
 * @param <R> 右側に保持する値の型
 */
public interface Union<L, R> {

    /**
     * Unionが保持する側の値を、左右それぞれの関数で共通の結果へ変換する関数を返します。
     * 変換結果のnullもそのまま結果になります。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param <O> 変換結果の型
     * @param leftResolver 左側の値を受け取る変換関数（非null）
     * @param rightResolver 右側の値を受け取る変換関数（非null）
     * @return Unionから変換結果を得る関数
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R, O> Function<Union<L, R>, O> toJoinedWith(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return union -> union.asJoined(leftResolver, rightResolver);
    }

    /**
     * 左右の値と位置を表す引数から共通の結果を得る関数を返します。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     * 変換結果のnullもそのまま結果になります。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param <O> 変換結果の型
     * @param biResolver 左右の値と位置を受け取る変換関数（非null）
     * @return Unionから変換結果を得る関数
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R, O> Function<Union<L, R>, O> toJoinedWith(
            @NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return union -> union.asJoined(biResolver);
    }

    /**
     * Unionが保持する側の値を、左右それぞれの関数で共通の結果へ変換する関数を返します。
     * 変換結果が非nullなら値を含むOptional、nullなら空のOptionalになります。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param <O> 変換結果の型
     * @param leftResolver 左側の値を受け取る変換関数（非null）
     * @param rightResolver 右側の値を受け取る変換関数（非null）
     * @return Unionから変換結果を得る関数
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R, O> Function<Union<L, R>, Optional<O>> toJoinedOptionalWith(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return union -> union.asJoinedOptional(leftResolver, rightResolver);
    }

    /**
     * 左右の値と位置を表す引数から共通の結果を得る関数を返します。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     * 変換結果が非nullなら値を含むOptional、nullなら空のOptionalになります。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param <O> 変換結果の型
     * @param biResolver 左右の値と位置を受け取る変換関数（非null）
     * @return Unionから変換結果を得る関数
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R, O> Function<Union<L, R>, Optional<O>> toJoinedOptionalWith(
            @NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return union -> union.asJoinedOptional(biResolver);
    }

    /**
     * 保持する側の値を使った処理を行うConsumerを返します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @return Unionの値を処理するConsumer
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> Consumer<Union<L, R>> acceptWith(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {
        return union -> union.accept(leftResolver, rightResolver);
    }

    /**
     * 左右の値と位置を表す引数を使った処理を行うConsumerを返します。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param biResolver 左右の値と位置を受け取る処理（非null）
     * @return Unionの値を処理するConsumer
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> Consumer<Union<L, R>> acceptWith(
            @NonNull BiConsumer<? super L, ? super R> biResolver) {
        return union -> union.accept(biResolver);
    }

    /**
     * 左側のUnionに対して、その値を使った処理を行うConsumerを返します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @return Unionの値を処理するConsumer
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> Consumer<Union<L, R>> acceptLeftWith(
            @NonNull Consumer<? super L> leftResolver) {
        return union -> union.acceptLeft(leftResolver);
    }

    /**
     * 右側のUnionに対して、その値を使った処理を行うConsumerを返します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @return Unionの値を処理するConsumer
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> Consumer<Union<L, R>> acceptRightWith(
            @NonNull Consumer<? super R> rightResolver) {
        return union -> union.acceptRight(rightResolver);
    }

    /**
     * 保持する側の値を使った処理を行う演算子を返します。
     * 返した演算子は処理後に入力と同じUnionを返します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @return 入力Unionを処理後にそのまま返す演算子
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> UnaryOperator<Union<L, R>> peekWith(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {
        return union -> union.peek(leftResolver, rightResolver);
    }

    /**
     * 左右の値と位置を表す引数を使った処理を行う演算子を返します。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     * 返した演算子は処理後に入力と同じUnionを返します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param biResolver 左右の値と位置を受け取る処理（非null）
     * @return 入力Unionを処理後にそのまま返す演算子
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> UnaryOperator<Union<L, R>> peekWith(
            @NonNull BiConsumer<? super L, ? super R> biResolver) {
        return union -> union.peek(biResolver);
    }

    /**
     * 左側のUnionに対して、その値を使った処理を行う演算子を返します。
     * 返した演算子は処理後に入力と同じUnionを返します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @return 入力Unionを処理後にそのまま返す演算子
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> UnaryOperator<Union<L, R>> peekLeftWith(
            @NonNull Consumer<? super L> leftResolver) {
        return union -> union.peekLeft(leftResolver);
    }

    /**
     * 右側のUnionに対して、その値を使った処理を行う演算子を返します。
     * 返した演算子は処理後に入力と同じUnionを返します。
     * 返した関数・処理に渡すUnionは非nullです。
     *
     * @param <L> 左側の値の型
     * @param <R> 右側の値の型
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @return 入力Unionを処理後にそのまま返す演算子
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    static <L, R> UnaryOperator<Union<L, R>> peekRightWith(
            @NonNull Consumer<? super R> rightResolver) {
        return union -> union.peekRight(rightResolver);
    }

    /**
     * 左側に保持する値を返します。
     *
     * @return 保持する値
     * @throws UnsupportedOperationException 右側のUnionの場合
     */
    L left();

    /**
     * 右側に保持する値を返します。
     *
     * @return 保持する値
     * @throws UnsupportedOperationException 左側のUnionの場合
     */
    R right();

    /**
     * 左側の値を含むOptionalを返し、右側のUnionでは空のOptionalを返します。
     *
     * @return 指定した側の値を含むOptional、反対側なら空のOptional
     */
    default Optional<L> leftOptional() {
        return this.hasLeft() ? Optional.of(this.left()) : Optional.empty();
    }

    /**
     * 右側の値を含むOptionalを返し、左側のUnionでは空のOptionalを返します。
     *
     * @return 指定した側の値を含むOptional、反対側なら空のOptional
     */
    default Optional<R> rightOptional() {
        return this.hasRight() ? Optional.of(this.right()) : Optional.empty();
    }

    /**
     * 左側の値を保持しているかを返します。
     *
     * @return 指定した側の値を保持する場合にtrue
     */
    boolean hasLeft();

    /**
     * 右側の値を保持しているかを返します。
     *
     * @return 指定した側の値を保持する場合にtrue
     */
    boolean hasRight();

    /**
     * 保持する側の値を、対応する関数で共通の結果へ変換します。
     * 変換結果のnullもそのまま結果になります。
     *
     * @param <O> 変換結果の型
     * @param leftResolver 左側の値を受け取る変換関数（非null）
     * @param rightResolver 右側の値を受け取る変換関数（非null）
     * @return 保持する側の値から得た変換結果
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    <O> O asJoined(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver);

    /**
     * 左右の値と位置を表す引数から共通の結果を返します。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     * 変換結果のnullもそのまま結果になります。
     *
     * @param <O> 変換結果の型
     * @param biResolver 左右の値と位置を受け取る変換関数（非null）
     * @return 保持する側の値から得た変換結果
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    <O> O asJoined(@NonNull BiFunction<? super L, ? super R, ? extends O> biResolver);

    /**
     * 保持する側の値を、対応する関数で共通の結果へ変換します。
     * 変換結果が非nullなら値を含むOptional、nullなら空のOptionalになります。
     *
     * @param <O> 変換結果の型
     * @param leftResolver 左側の値を受け取る変換関数（非null）
     * @param rightResolver 右側の値を受け取る変換関数（非null）
     * @return 変換結果を含むOptional、結果がnullなら空のOptional
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    default <O> Optional<O> asJoinedOptional(
            @NonNull Function<? super L, ? extends O> leftResolver,
            @NonNull Function<? super R, ? extends O> rightResolver) {
        return Optional.ofNullable(this.asJoined(leftResolver, rightResolver));
    }

    /**
     * 左右の値と位置を表す引数から共通の結果を返します。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     * 変換結果が非nullなら値を含むOptional、nullなら空のOptionalになります。
     *
     * @param <O> 変換結果の型
     * @param biResolver 左右の値と位置を受け取る変換関数（非null）
     * @return 変換結果を含むOptional、結果がnullなら空のOptional
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    default <O> Optional<O> asJoinedOptional(@NonNull BiFunction<? super L, ? super R, ? extends O> biResolver) {
        return Optional.ofNullable(this.asJoined(biResolver));
    }

    /**
     * 保持する側の値を使った処理を行います。
     *
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    void accept(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver);

    /**
     * 左右の値と位置を表す引数を使った処理を行います。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     *
     * @param biResolver 左右の値と位置を受け取る処理（非null）
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    void accept(@NonNull BiConsumer<? super L, ? super R> biResolver);

    /**
     * 左側のUnionに対して、その値を使った処理を行います。
     *
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    void acceptLeft(@NonNull Consumer<? super L> leftResolver);

    /**
     * 右側のUnionに対して、その値を使った処理を行います。
     *
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    void acceptRight(@NonNull Consumer<? super R> rightResolver);

    /**
     * 保持する側の値を使った処理を行います。
     * 処理後にこのUnion自身を返すので、続けて値を変換できます。
     *
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @return このUnion自身
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    default Union<L, R> peek(
            @NonNull Consumer<? super L> leftResolver,
            @NonNull Consumer<? super R> rightResolver) {
        this.accept(leftResolver, rightResolver);
        return this;
    }

    /**
     * 左右の値と位置を表す引数を使った処理を行います。
     * 左側のUnionでは(左値, null)、右側では(null, 右値)を指定した関数・処理へ渡します。
     * 処理後にこのUnion自身を返すので、続けて値を変換できます。
     *
     * @param biResolver 左右の値と位置を受け取る処理（非null）
     * @return このUnion自身
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    default Union<L, R> peek(@NonNull BiConsumer<? super L, ? super R> biResolver) {
        this.accept(biResolver);
        return this;
    }

    /**
     * 左側のUnionに対して、その値を使った処理を行います。
     * 処理後にこのUnion自身を返すので、続けて値を変換できます。
     *
     * @param leftResolver 左側の値を受け取る処理（非null）
     * @return このUnion自身
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    default Union<L, R> peekLeft(@NonNull Consumer<? super L> leftResolver) {
        this.acceptLeft(leftResolver);
        return this;
    }

    /**
     * 右側のUnionに対して、その値を使った処理を行います。
     * 処理後にこのUnion自身を返すので、続けて値を変換できます。
     *
     * @param rightResolver 右側の値を受け取る処理（非null）
     * @return このUnion自身
     * @throws NullPointerException 指定した関数・処理がnullの場合
     */
    default Union<L, R> peekRight(@NonNull Consumer<? super R> rightResolver) {
        this.acceptRight(rightResolver);
        return this;
    }
}
