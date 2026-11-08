[![CI](https://github.com/lambig/Union/actions/workflows/ci.yml/badge.svg)](https://github.com/lambig/Union/actions/workflows/ci.yml)

# Union

二種類の値のうち、どちらか一方を結果として返したいときに使うJavaライブラリです。
`Union<L, R>`は左側の`L`または右側の`R`を保持します。
たとえば「読み取れた数量」と「入力エラーの理由」をひとつの戻り値で表せます。
左右にどの意味を割り当てるかは利用側で決めます。

## 導入

Java 11以上が対象です。0.1.1はMaven Centralから以下の依存宣言で利用できます。
導入例の実行方法は[開発・検証手順](DEVELOPING.md)にあります。

Gradle:

```groovy
repositories { mavenCentral() }
dependencies { implementation 'io.github.lambig:Union:0.1.1' }
```

Maven:

```xml
<dependency>
  <groupId>io.github.lambig</groupId>
  <artifactId>Union</artifactId>
  <version>0.1.1</version>
</dependency>
```

実行に必要なのはUnionのJARとJava標準ライブラリです。

## 成功値とエラー理由を同じ戻り値で扱う

```java
import io.github.lambig.union.Union;
import io.github.lambig.union.UnionOf;
import java.util.List;
import java.util.stream.Collectors;

public class QuantityExample {
    static Union<Integer, String> quantity(String input) {
        try {
            return UnionOf.left(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return UnionOf.right("invalid: " + input);
        }
    }

    public static void main(String[] args) {
        Union<Integer, String> result = quantity("42");
        System.out.println(result.leftOptional());
        System.out.println(quantity("abc").rightOptional());
        System.out.println(List.of("42", "abc").stream()
                .map(QuantityExample::quantity)
                .map(Union.toJoinedWith(n -> "quantity=" + n, reason -> reason))
                .collect(Collectors.toList()));
    }
}
```

出力:

```text
Optional[42]
Optional[invalid: abc]
[quantity=42, invalid: abc]
```

`asJoined(leftFunction, rightFunction)`は、保持する側に対応した関数の結果を返します。
この例では、左右のどちらも表示用のStringへ変換しています。
`Union.toJoinedWith(...)`はこの変換を`Stream.map`などへ渡せるFunctionにします。

## 値を取り出す・共通の結果に変える

| 呼び出し | 左側のUnion | 右側のUnion |
| --- | --- | --- |
| `left()` | 左側の値 | `UnsupportedOperationException` |
| `right()` | `UnsupportedOperationException` | 右側の値 |
| `leftOptional()` | 左側の値を含むOptional | 空のOptional |
| `rightOptional()` | 空のOptional | 右側の値を含むOptional |
| `hasLeft()` / `hasRight()` | true / false | false / true |
| `asJoined(leftFunction, rightFunction)` | 左の関数の結果 | 右の関数の結果 |
| `asJoined(biFunction)` | `(左値, null)`から得た結果 | `(null, 右値)`から得た結果 |

`asJoined`の結果はnullも含めてそのまま返ります。
`asJoinedOptional`は同じ変換結果をOptionalにし、結果がnullなら空になります。
`Union.toJoinedOptionalWith(...)`は、OptionalやStreamの変換に渡すFunctionを返します。

## 値に応じた処理を行う

`accept(leftConsumer, rightConsumer)`は、保持する側のConsumerへ値を渡します。
`acceptLeft`は左側のUnion、`acceptRight`は右側のUnionに対して処理を行います。
`accept(biConsumer)`は左右を表す二引数を受け取り、値を保持する側が非null、反対側がnullです。

`peek`・`peekLeft`・`peekRight`は同じ処理を行い、元のUnion自身を返します。
たとえば記録処理を挟んでから`asJoined`で表示用の値へ変換できます。
`Union.acceptWith`・`acceptLeftWith`・`acceptRightWith`はConsumerを、
`Union.peekWith`・`peekLeftWith`・`peekRightWith`はUnaryOperatorを返し、StreamやOptionalの操作に渡せます。

## 型を明示する

`Union<Integer, String> result = UnionOf.left(42)`のように代入先で左右の型を指定できます。
呼び出し側で明示する場合は`UnionOf.<Integer, String>left(42)`、または`UnionOf.l(42).<String>r()`を使います。
右側の値では`UnionOf.r("invalid").<Integer>l()`です。
戻り値の`Left<L, R>`と`Right<L, R>`は、それぞれ`Supplier<L>`と`Supplier<R>`でもあり、`get()`で保持する値を取得できます。

## null・例外・値の共有

`UnionOf.left`・`right`に渡す保持値は非nullです。nullは`NullPointerException`になります。
`l(value)`・`r(value)`の型指定用の生成元は、最後の`r()`・`l()`でUnionを作る際に値を確認します。
ここで渡した値がnullの場合も`NullPointerException`になります。

関数・Consumerは、左右それぞれに非nullを指定します。
左右別の関数・処理が実行されるのは保持する側だけです。
関数・処理の例外は呼出元へ伝わり、それまでに行った外部への更新は残ります。
Optionalで結果を受け取る場合も、例外は同じように伝わります。

Unionは生成時の値への参照を保持します。値が可変オブジェクトなら、その変更を取得先でも参照できます。
複数スレッドで使う場合は、保持する値と指定する処理を利用するスレッド間で安全に扱えるようにします。

## API文書と検証

- [Maven／Gradleの独立した導入例](example/)で値の変換、処理の振り分け、左右の型指定を確認できます。
- `./gradlew javadoc`で生成したAPI文書は`build/docs/javadoc/index.html`から参照できます。
- [0.1.1のJavadoc JAR](https://repo.maven.apache.org/maven2/io/github/lambig/Union/0.1.1/Union-0.1.1-javadoc.jar)を展開すると、`index.html`から同じ版のAPI文書を参照できます。公開物にはsources JARも含まれます。
- [開発・検証手順](DEVELOPING.md)では、0.1.0でコンパイルした利用コード、Java 11／25、候補版・公開版の導入確認を扱います。

0.1.1では公開設定・文書・利用例を整え、Lombokをビルド時の依存へ整理しました。
0.1.0の公開メソッドと値・処理の振る舞いを維持します。
