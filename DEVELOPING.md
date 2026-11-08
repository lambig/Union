# 開発・検証

ビルドはJDK 17以上、実行対象はJava 11以上です。
Maven 3.8以上、Python 3、POSIXシェルを使います。Gradleは同梱Wrapperから取得します。
`TEST_JAVA_HOME`を指定すると、試験と利用例をそのJavaで実行できます。
CIはビルドJDK 17と、実行環境Java 11／25の組合せを確認します。

```sh
sh ./verify-consumers.sh candidate
```

単体試験、sources/Javadoc JAR、0.1.0との公開シグネチャ比較、旧版でコンパイルした利用コード、
独立したMaven／Gradle導入例、READMEのプログラムを確認します。
候補版は`build/repository`に生成されます。
導入例は空の一時キャッシュへUnionを取得し、実行時の依存がUnionのJARだけであることも確認します。
結果は`build/consumer-results/candidate`、試験結果は`build/reports/tests/test`に残ります。
JaCoCoのHTML・XMLレポートは`build/reports/jacoco/test`に生成され、CIでは各Javaの試験結果とともに保存されます。

## 導入例の実行

```sh
sh ./gradlew publishMavenJavaPublicationToCandidateRepository
sh ./gradlew -p example -PcandidateRepository="$PWD/build/repository" runExample
mvn -f example/pom.xml -DcandidateRepository="file://$PWD/build/repository" \
  compile dependency:build-classpath -Dmdep.outputFile=target/runtime-classpath.txt
java -cp "example/target/classes:$(cat example/target/runtime-classpath.txt)" example.UnionExample
```

出力:

```text
left=42; right=invalid; joined=[42, invalid]; events=[left:42, right:invalid]
```

次のコマンドでMaven Centralの公開版0.1.1を取得し、同じ利用例を確認できます。

```sh
sh ./verify-consumers.sh public 0.1.1
```

## API文書・公開用bundle

`javadoc`はLombokが生成する公開アクセサを含めたソースからAPI文書を生成します。
sources JARにはリポジトリのソース、Javadoc JARには生成したHTMLが入ります。

公開用のASCII形式秘密鍵を`SIGNING_KEY`、パスフレーズを`SIGNING_PASSWORD`に設定して実行します。

```sh
sh ./gradlew clean check -PreleaseBundle centralBundle
```

`build/central/Union-0.1.1-central.zip`には本体・sources・Javadoc・POM・Gradle Module Metadataと
それぞれの署名・チェックサムが入ります。公開者は対象コミット、検証結果とbundle内容を確認して
Central Portalへ登録し、公開後に`public`の導入確認を行います。
